package com.velora.tracker.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.repository.TransactionRepository
import com.velora.tracker.domain.usecase.DeleteTransactionUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction: StateFlow<Transaction?> = _transaction.asStateFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()

    val currencySymbol: StateFlow<String> = flow { emit("$") }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "$")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val showDeleteDialog = MutableStateFlow(false)

    private val _deleteResult = MutableSharedFlow<Boolean>()
    val deleteResult: SharedFlow<Boolean> = _deleteResult.asSharedFlow()

    fun loadTransaction(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // val t = transactionRepository.getTransactionById(id)
                // _transaction.value = t
                // if (t != null) {
                //     _category.value = categoryRepository.getCategoryById(t.categoryId)
                // }
            } catch (e: Exception) {
                // handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun showDeleteConfirmation() {
        showDeleteDialog.value = true
    }

    fun dismissDeleteConfirmation() {
        showDeleteDialog.value = false
    }

    fun deleteTransaction() {
        val currentTransaction = _transaction.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            deleteTransactionUseCase(currentTransaction.id)
            _isLoading.value = false
            showDeleteDialog.value = false
            _deleteResult.emit(true)
        }
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionDetailViewModel(
                    app.transactionRepository,
                    app.categoryRepository,
                    app.deleteTransactionUseCase,
                    app.settingsRepository
                ) as T
            }
        }
    }
}
