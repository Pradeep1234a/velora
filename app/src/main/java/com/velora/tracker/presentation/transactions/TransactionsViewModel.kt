package com.velora.tracker.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.usecase.GetTransactionsUseCase
import com.velora.tracker.domain.usecase.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val searchQuery = MutableStateFlow("")
    val isSearchActive = MutableStateFlow(false)
    val selectedType = MutableStateFlow<TransactionType?>(null)
    val selectedCategoryIds = MutableStateFlow<Set<Long>>(emptySet())
    val sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val isFilterSheetVisible = MutableStateFlow(false)

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₹")

    val categories: StateFlow<List<Category>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = combine(
        searchQuery,
        selectedType,
        selectedCategoryIds,
        sortOrder
    ) { query, type, catIds, sort ->
        getTransactionsUseCase(
            dateRange = null,
            searchQuery = query.ifBlank { null },
            categoryId = catIds.firstOrNull(),
            type = type,
            sortOrder = sort
        )
    }.flatMapLatest { it }
        .catch { e -> _error.value = e.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val groupedTransactions: StateFlow<Map<LocalDate, List<Transaction>>> = transactions
        .map { list -> list.groupBy { it.dateTime.toLocalDate() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun toggleSearch() {
        isSearchActive.value = !isSearchActive.value
        if (!isSearchActive.value) {
            searchQuery.value = ""
        }
    }

    fun setTypeFilter(type: TransactionType?) {
        selectedType.value = type
    }

    fun setCategoryFilter(categoryIds: Set<Long>) {
        selectedCategoryIds.value = categoryIds
    }

    fun setSortOrder(order: SortOrder) {
        sortOrder.value = order
    }

    fun showFilterSheet() {
        isFilterSheetVisible.value = true
    }

    fun hideFilterSheet() {
        isFilterSheetVisible.value = false
    }

    fun resetFilters() {
        selectedType.value = null
        selectedCategoryIds.value = emptySet()
        sortOrder.value = SortOrder.NEWEST
    }

    fun applyFilters() {
        hideFilterSheet()
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionsViewModel(
                    app.getTransactionsUseCase,
                    app.categoryRepository,
                    app.settingsRepository
                ) as T
            }
        }
    }
}
