package com.velora.tracker.presentation.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.velora.tracker.VeloraApplication
import com.velora.tracker.data.ai.UserPreferenceLearningStore
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.CategorySource
import com.velora.tracker.domain.model.CategorySuggestion
import com.velora.tracker.domain.model.PaymentMethod
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.repository.TransactionRepository
import com.velora.tracker.domain.usecase.AddTransactionUseCase
import com.velora.tracker.domain.usecase.CategorizeTransactionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categorizeTransactionUseCase: CategorizeTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val settingsRepository: SettingsRepository,
    private val userPreferenceLearningStore: UserPreferenceLearningStore
) : ViewModel() {

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType: StateFlow<TransactionType> = _transactionType.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _selectedTime = MutableStateFlow(LocalTime.now())
    val selectedTime: StateFlow<LocalTime> = _selectedTime.asStateFlow()

    private val _notes = MutableStateFlow("")
    val notes: StateFlow<String> = _notes.asStateFlow()

    private val _paymentMethod = MutableStateFlow(PaymentMethod.CASH)
    val paymentMethod: StateFlow<PaymentMethod> = _paymentMethod.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _categorySuggestion = MutableStateFlow<CategorySuggestion?>(null)
    val categorySuggestion: StateFlow<CategorySuggestion?> = _categorySuggestion.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveResult = MutableSharedFlow<SaveResult>()
    val saveResult = _saveResult.asSharedFlow()

    private var allCategories = listOf<Category>()
    private var isEditMode = false
    private var editTransactionId: Long? = null
    private var debounceJob: Job? = null
    private var categorySource = CategorySource.USER

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "₹"
        )

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { cats ->
                allCategories = cats
                filterCategories()
            }
        }
    }

    fun setTransactionType(type: TransactionType) {
        _transactionType.value = type
        _selectedCategoryId.value = null
        filterCategories()
    }

    private fun filterCategories() {
        _categories.value = allCategories.filter { it.type == _transactionType.value }
    }

    fun setAmount(amountStr: String) {
        if (amountStr.isEmpty() || amountStr.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
            _amount.value = amountStr
        }
    }

    fun setTitle(titleStr: String) {
        _title.value = titleStr
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500)
            if (titleStr.isNotBlank() && _selectedCategoryId.value == null) {
                val suggestion = categorizeTransactionUseCase(titleStr, _notes.value)
                _categorySuggestion.value = suggestion
            }
        }
    }

    fun selectCategory(categoryId: Long) {
        _selectedCategoryId.value = categoryId
        _categorySuggestion.value = null
        categorySource = CategorySource.USER
        viewModelScope.launch {
            if (_title.value.isNotBlank()) {
                userPreferenceLearningStore.recordPreference(_title.value, categoryId)
            }
        }
    }

    fun acceptAiSuggestion() {
        _categorySuggestion.value?.let {
            _selectedCategoryId.value = it.categoryId
            categorySource = it.source
            viewModelScope.launch {
                if (_title.value.isNotBlank()) {
                    userPreferenceLearningStore.recordPreference(_title.value, it.categoryId)
                }
            }
        }
        _categorySuggestion.value = null
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setTime(time: LocalTime) {
        _selectedTime.value = time
    }

    fun setNotes(notes: String) {
        _notes.value = notes
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _paymentMethod.value = method
    }

    fun loadTransaction(id: Long) {
        isEditMode = true
        editTransactionId = id
        // In a full implementation, fetch the transaction and populate fields.
        // For now, we simulate basic setup.
    }

    fun saveTransaction() {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val parsedAmount = parseAmount(_amount.value)
                if (parsedAmount <= 0) {
                    _saveResult.emit(SaveResult.Error("Amount must be greater than 0"))
                    _isSaving.value = false
                    return@launch
                }
                
                if (_title.value.isBlank()) {
                    _saveResult.emit(SaveResult.Error("Title cannot be empty"))
                    _isSaving.value = false
                    return@launch
                }
                
                val categoryId = _selectedCategoryId.value
                if (categoryId == null) {
                    _saveResult.emit(SaveResult.Error("Please select a category"))
                    _isSaving.value = false
                    return@launch
                }

                val dateTime = LocalDateTime.of(_selectedDate.value, _selectedTime.value)
                val tx = Transaction(
                    id = editTransactionId ?: 0L,
                    amountMinorUnits = parsedAmount,
                    type = _transactionType.value,
                    title = _title.value,
                    categoryId = categoryId,
                    dateTime = dateTime,
                    notes = _notes.value,
                    paymentMethod = _paymentMethod.value,
                    categorySource = categorySource
                )

                if (isEditMode) {
                    // Assuming updateTransactionUseCase is injected
                    // app.updateTransactionUseCase(tx)
                } else {
                    addTransactionUseCase(tx)
                }
                _saveResult.emit(SaveResult.Success)
            } catch (e: Exception) {
                _saveResult.emit(SaveResult.Error(e.message ?: "Failed to save transaction"))
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun parseAmount(amountStr: String): Long {
        if (amountStr.isBlank()) return 0L
        return try {
            val parts = amountStr.split(".")
            val major = parts[0].toLongOrNull() ?: 0L
            val minor = if (parts.size > 1) {
                parts[1].padEnd(2, '0').substring(0, 2).toLongOrNull() ?: 0L
            } else 0L
            (major * 100) + minor
        } catch (e: Exception) {
            0L
        }
    }

    sealed class SaveResult {
        object Success : SaveResult()
        data class Error(val message: String) : SaveResult()
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AddTransactionViewModel(
                    app.addTransactionUseCase,
                    app.categorizeTransactionUseCase,
                    app.categoryRepository,
                    app.transactionRepository,
                    app.settingsRepository,
                    app.userPreferenceLearningStore
                ) as T
            }
        }
    }
}
