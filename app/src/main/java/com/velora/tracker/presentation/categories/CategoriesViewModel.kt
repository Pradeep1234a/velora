package com.velora.tracker.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.usecase.ManageCategoryUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoryWithStats(
    val category: Category,
    val transactionCount: Int,
    val totalAmount: Long
)

data class CategoryEditState(
    val isNew: Boolean,
    val categoryType: TransactionType,
    val categoryId: Long? = null,
    val name: String = "",
    val selectedIconName: String = "more_horiz",
    val selectedColorHex: String = "#90A4AE"
)

data class DeleteCategoryState(
    val categoryId: Long,
    val categoryName: String,
    val transactionCount: Int,
    val availableCategories: List<Category>,
    val selectedMoveToCategoryId: Long? = null
)

class CategoriesViewModel(
    private val manageCategoryUseCase: ManageCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _showEditDialog = MutableStateFlow<CategoryEditState?>(null)
    val showEditDialog: StateFlow<CategoryEditState?> = _showEditDialog.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow<DeleteCategoryState?>(null)
    val showDeleteDialog: StateFlow<DeleteCategoryState?> = _showDeleteDialog.asStateFlow()

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₹")

    val expenseCategories: StateFlow<List<CategoryWithStats>> = categoryRepository.getAllCategories()
        .map { categories ->
            categories.filter { it.type == TransactionType.EXPENSE }
                .map { category ->
                    val stats = manageCategoryUseCase.getCategoryStats(category.id)
                    CategoryWithStats(category, stats.transactionCount, stats.totalAmount)
                }
        }
        .onEach { _isLoading.value = false }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incomeCategories: StateFlow<List<CategoryWithStats>> = categoryRepository.getAllCategories()
        .map { categories ->
            categories.filter { it.type == TransactionType.INCOME }
                .map { category ->
                    val stats = manageCategoryUseCase.getCategoryStats(category.id)
                    CategoryWithStats(category, stats.transactionCount, stats.totalAmount)
                }
        }
        .catch { _error.value = it.message }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun showAddCategory(type: TransactionType) {
        _showEditDialog.value = CategoryEditState(isNew = true, categoryType = type)
    }

    fun showEditCategory(category: Category) {
        _showEditDialog.value = CategoryEditState(
            isNew = false,
            categoryType = category.type,
            categoryId = category.id,
            name = category.name,
            selectedIconName = category.iconName,
            selectedColorHex = category.colorHex
        )
    }

    fun dismissEditDialog() {
        _showEditDialog.value = null
    }

    fun updateEditState(state: CategoryEditState) {
        _showEditDialog.value = state
    }

    fun saveCategory() {
        val state = _showEditDialog.value ?: return
        if (state.name.isBlank()) return
        
        viewModelScope.launch {
            try {
                if (state.isNew) {
                    manageCategoryUseCase.createCategory(
                        name = state.name,
                        type = state.categoryType,
                        iconName = state.selectedIconName,
                        colorHex = state.selectedColorHex
                    )
                } else {
                    manageCategoryUseCase.updateCategory(
                        id = state.categoryId!!,
                        name = state.name,
                        iconName = state.selectedIconName,
                        colorHex = state.selectedColorHex
                    )
                }
                dismissEditDialog()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun showDeleteCategory(category: Category) {
        viewModelScope.launch {
            val stats = manageCategoryUseCase.getCategoryStats(category.id)
            val available = categoryRepository.getAllCategories().first().filter { it.type == category.type && it.id != category.id }
            
            _showDeleteDialog.value = DeleteCategoryState(
                categoryId = category.id,
                categoryName = category.name,
                transactionCount = stats.transactionCount,
                availableCategories = available
            )
        }
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = null
    }

    fun selectMoveToCategory(categoryId: Long) {
        _showDeleteDialog.value = _showDeleteDialog.value?.copy(selectedMoveToCategoryId = categoryId)
    }

    fun confirmDeleteCategory() {
        val state = _showDeleteDialog.value ?: return
        viewModelScope.launch {
            try {
                manageCategoryUseCase.deleteCategory(state.categoryId, state.selectedMoveToCategoryId)
                dismissDeleteDialog()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CategoriesViewModel(
                    app.manageCategoryUseCase,
                    app.categoryRepository,
                    app.settingsRepository
                ) as T
            }
        }
    }
}
