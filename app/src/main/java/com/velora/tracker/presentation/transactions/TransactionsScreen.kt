package com.velora.tracker.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.presentation.dashboard.getCategoryEmoji
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

    val transactions by viewModel.transactions.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val inflow = remember(transactions) {
        transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amountMinorUnits }
    }
    val outflow = remember(transactions) {
        transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amountMinorUnits }
    }
    val net = inflow - outflow

    Column(modifier = Modifier.fillMaxSize()) {
        VeloraStitchHeader(
            title = "Transactions",
            onSettingsClick = { /* Settings / Notifications */ }
        )

        // Inflow / Outflow / Net 3-card row from Stitch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Inflow
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 0.5.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = Color(0xFF16803C),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(text = "Inflow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = "+${MoneyFormatter.format(inflow, currencySymbol, showSign = false)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16803C)
                    )
                }
            }

            // Outflow
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 0.5.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = null,
                            tint = Color(0xFFC2410C),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(text = "Outflow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = "-${MoneyFormatter.format(outflow, currencySymbol, showSign = false)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC2410C)
                    )
                }
            }

            // Net
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 0.5.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalanceWallet,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(text = "Net", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = "${if (net >= 0) "+" else ""}${MoneyFormatter.format(net, currencySymbol, showSign = false)}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (net >= 0) Color(0xFF005C55) else Color(0xFFC2410C)
                    )
                }
            }
        }

        // Search Bar from Stitch
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shadowElevation = 0.5.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
                androidx.compose.foundation.text.BasicTextField(
                    value = searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search by title, category, note...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        innerTextField()
                    }
                )
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.setSearchQuery("") },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }

        // Filter chips carousel
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedType == null,
                    onClick = { viewModel.setTypeFilter(null) },
                    label = { Text("✓ All") },
                    shape = RoundedCornerShape(100.dp)
                )
            }
            item {
                FilterChip(
                    selected = selectedType == TransactionType.INCOME,
                    onClick = { viewModel.setTypeFilter(TransactionType.INCOME) },
                    label = { Text("↗ Income") },
                    shape = RoundedCornerShape(100.dp)
                )
            }
            item {
                FilterChip(
                    selected = selectedType == TransactionType.EXPENSE,
                    onClick = { viewModel.setTypeFilter(TransactionType.EXPENSE) },
                    label = { Text("↘ Expense") },
                    shape = RoundedCornerShape(100.dp)
                )
            }
            items(categories.take(6)) { cat ->
                val emoji = getCategoryEmoji(cat.name)
                FilterChip(
                    selected = viewModel.selectedCategoryIds.value.contains(cat.id),
                    onClick = { viewModel.setCategoryFilter(setOf(cat.id)) },
                    label = { Text("$emoji ${cat.name}") },
                    shape = RoundedCornerShape(100.dp)
                )
            }
        }

        when {
            isLoading -> VeloraLoadingState()
            error != null -> VeloraErrorState(message = error ?: "Unknown error")
            groupedTransactions.isEmpty() -> VeloraEmptyState(message = "No transactions found")
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    groupedTransactions.forEach { (date, txs) ->
                        item {
                            val headerText = when (date) {
                                LocalDate.now() -> "TODAY — ${date.format(DateTimeFormatter.ofPattern("d MMM")).uppercase()}"
                                LocalDate.now().minusDays(1) -> "YESTERDAY — ${date.format(DateTimeFormatter.ofPattern("d MMM")).uppercase()}"
                                else -> date.format(DateTimeFormatter.ofPattern("d MMM")).uppercase()
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = headerText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "${txs.size} entries",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                        
                        items(txs) { transaction ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shadowElevation = 0.5.dp
                            ) {
                                VeloraTransactionRow(
                                    transaction = transaction,
                                    formattedTime = "${transaction.dateTime.format(timeFormatter)} · ${transaction.paymentMethod.name.replace("_", " ")}",
                                    currencySymbol = currencySymbol,
                                    onClick = { onTransactionClick(transaction.id) }
                                )
                            }
                        }
                    }

                    // Verified footer pill
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Balances reconciled & verified",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
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
