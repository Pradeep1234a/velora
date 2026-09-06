package com.velora.tracker.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.*
import com.velora.tracker.ui.theme.Spacing
import java.time.format.DateTimeFormatter
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel,
    onTransactionClick: (Long) -> Unit,
    onAddTransaction: () -> Unit
) {
    val groupedTransactions by viewModel.groupedTransactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val isFilterSheetVisible by viewModel.isFilterSheetVisible.collectAsState()
    
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    Column(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            VeloraSearchBar(
                query = searchQuery,
                onQueryChange = viewModel::setSearchQuery,
                onClose = viewModel::toggleSearch
            )
        } else {
            VeloraTopBar(
                title = "Transactions",
                onSearchClick = viewModel::toggleSearch,
                onFilterClick = viewModel::showFilterSheet
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.medium, vertical = Spacing.small),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            FilterChip(
                selected = selectedType == null,
                onClick = { viewModel.setTypeFilter(null) },
                label = { Text("All") }
            )
            FilterChip(
                selected = selectedType == TransactionType.INCOME,
                onClick = { viewModel.setTypeFilter(TransactionType.INCOME) },
                label = { Text("Income") }
            )
            FilterChip(
                selected = selectedType == TransactionType.EXPENSE,
                onClick = { viewModel.setTypeFilter(TransactionType.EXPENSE) },
                label = { Text("Expense") }
            )
        }

        when {
            isLoading -> VeloraLoadingState()
            error != null -> VeloraErrorState(message = error ?: "Unknown error")
            groupedTransactions.isEmpty() -> VeloraEmptyState(message = "No transactions found")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Spacing.large)
                ) {
                    groupedTransactions.forEach { (date, transactions) ->
                        item {
                            val headerText = when (date) {
                                LocalDate.now() -> "Today, ${date.format(dateFormatter)}"
                                LocalDate.now().minusDays(1) -> "Yesterday, ${date.format(dateFormatter)}"
                                else -> date.format(dateFormatter)
                            }
                            VeloraDateHeader(text = headerText)
                        }
                        
                        items(transactions) { transaction ->
                            VeloraTransactionRow(
                                transaction = transaction,
                                formattedTime = transaction.dateTime.format(timeFormatter),
                                onClick = { onTransactionClick(transaction.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isFilterSheetVisible) {
        val categories by viewModel.categories.collectAsState()
        val selectedCategoryIds by viewModel.selectedCategoryIds.collectAsState()
        val sortOrder by viewModel.sortOrder.collectAsState()

        VeloraFilterSheet(
            onDismiss = viewModel::hideFilterSheet,
            onApply = viewModel::applyFilters,
            onReset = viewModel::resetFilters,
            isVisible = true,
            selectedType = selectedType?.name,
            onTypeSelected = { typeName ->
                viewModel.setTypeFilter(typeName?.let { TransactionType.valueOf(it) })
            },
            selectedCategoryIds = selectedCategoryIds,
            onCategorySelected = viewModel::setCategoryFilter,
            categories = categories,
            sortOrder = sortOrder.name,
            onSortOrderChanged = { sortName ->
                viewModel.setSortOrder(com.velora.tracker.domain.usecase.SortOrder.valueOf(sortName))
            }
        )
    }
}
