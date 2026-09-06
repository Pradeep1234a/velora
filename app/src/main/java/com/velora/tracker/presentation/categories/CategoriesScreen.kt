package com.velora.tracker.presentation.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.*

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    onNavigateBack: () -> Unit
) {
    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showEditDialog by viewModel.showEditDialog.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()

    Scaffold(
        topBar = {
            VeloraTopBar(
                title = "Categories",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { viewModel.showAddCategory(TransactionType.EXPENSE) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                VeloraLoadingState()
            } else if (error != null) {
                VeloraErrorState(error = error ?: "Unknown error", onRetry = { /* Retry logic */ })
            } else if (expenseCategories.isEmpty() && incomeCategories.isEmpty()) {
                VeloraEmptyState(
                    title = "No categories",
                    message = "Add a category to start organizing your transactions."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (expenseCategories.isNotEmpty()) {
                        item { VeloraSectionHeader(title = "Expense Categories") }
                        items(expenseCategories, key = { it.category.id }) { catStats ->
                            VeloraCategoryRow(
                                category = catStats.category,
                                transactionCount = catStats.transactionCount,
                                totalAmount = catStats.totalAmount,
                                currencySymbol = currencySymbol,
                                modifier = Modifier.clickable { viewModel.showEditCategory(catStats.category) }
                            )
                        }
                    }

                    if (incomeCategories.isNotEmpty()) {
                        item { VeloraSectionHeader(title = "Income Categories") }
                        items(incomeCategories, key = { it.category.id }) { catStats ->
                            VeloraCategoryRow(
                                category = catStats.category,
                                transactionCount = catStats.transactionCount,
                                totalAmount = catStats.totalAmount,
                                currencySymbol = currencySymbol,
                                modifier = Modifier.clickable { viewModel.showEditCategory(catStats.category) }
                            )
                        }
                    }
                }
            }
        }
    }

    showEditDialog?.let { state ->
        CategoryEditDialog(
            state = state,
            onStateChange = viewModel::updateEditState,
            onSave = viewModel::saveCategory,
            onDismiss = viewModel::dismissEditDialog
        )
    }

    showDeleteDialog?.let { state ->
        DeleteCategoryDialog(
            state = state,
            onSelectMoveToCategory = viewModel::selectMoveToCategory,
            onConfirm = viewModel::confirmDeleteCategory,
            onDismiss = viewModel::dismissDeleteDialog
        )
    }
}
