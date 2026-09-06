package com.velora.tracker.presentation.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.*
import com.velora.tracker.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onTransactionClick: (Long) -> Unit
) {
    val analyticsData by viewModel.analyticsData.collectAsState()
    val selectedDateRange by viewModel.selectedDateRange.collectAsState()
    val trendGranularity by viewModel.trendGranularity.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        VeloraTopBar(title = "Analytics")
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            modifier = Modifier.padding(vertical = Spacing.small)
        ) {
            item {
                FilterChip(
                    selected = selectedDateRange == DateRange.ThisWeek,
                    onClick = { viewModel.selectDateRange(DateRange.ThisWeek) },
                    label = { Text("This Week") }
                )
            }
            item {
                FilterChip(
                    selected = selectedDateRange == DateRange.ThisMonth,
                    onClick = { viewModel.selectDateRange(DateRange.ThisMonth) },
                    label = { Text("This Month") }
                )
            }
            item {
                FilterChip(
                    selected = selectedDateRange == DateRange.LastMonth,
                    onClick = { viewModel.selectDateRange(DateRange.LastMonth) },
                    label = { Text("Last Month") }
                )
            }
            item {
                FilterChip(
                    selected = selectedDateRange == DateRange.ThisYear,
                    onClick = { viewModel.selectDateRange(DateRange.ThisYear) },
                    label = { Text("This Year") }
                )
            }
        }

        when {
            isLoading -> VeloraLoadingState()
            error != null -> VeloraErrorState(message = error ?: "Unknown error")
            analyticsData.totalIncome == 0L && analyticsData.totalExpenses == 0L && analyticsData.topTransactions.isEmpty() -> {
                VeloraEmptyState(
                    title = "Not enough data yet",
                    message = "Add a few more transactions to see your spending patterns."
                )
            }
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = Spacing.medium, vertical = Spacing.small),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Income & Expense summary cards
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                        ) {
                            VeloraMetricCard(
                                label = "Income",
                                amountMinorUnits = analyticsData.totalIncome,
                                isIncome = true,
                                currencySymbol = currencySymbol,
                                modifier = Modifier.weight(1f)
                            )
                            VeloraMetricCard(
                                label = "Expenses",
                                amountMinorUnits = analyticsData.totalExpenses,
                                isIncome = false,
                                currencySymbol = currencySymbol,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Net Cash Flow card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(Spacing.medium),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Net Cash Flow",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = MoneyFormatter.format(analyticsData.netCashFlow, currencySymbol, showSign = true),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // 2. Spending Trend Chart
                    item {
                        VeloraChartCard(
                            title = "Spending Trend",
                            actions = {
                                listOf("daily" to "Daily", "weekly" to "Weekly", "monthly" to "Monthly").forEach { (key, label) ->
                                    FilterChip(
                                        selected = trendGranularity == key,
                                        onClick = { viewModel.setTrendGranularity(key) },
                                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        ) {
                            if (analyticsData.spendingTrend.isEmpty()) {
                                Text(
                                    text = "No spending data for this period",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 24.dp)
                                )
                            } else {
                                val maxVal = analyticsData.spendingTrend.maxOfOrNull { it.amountMinorUnits }?.toFloat() ?: 1f
                                val normalizedData = analyticsData.spendingTrend.map { point ->
                                    val ratio = if (maxVal > 0f) (point.amountMinorUnits.toFloat() / maxVal).coerceIn(0.05f, 1f) else 0f
                                    point.label to ratio
                                }
                                VeloraBarChart(
                                    data = normalizedData,
                                    barColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // 3. Category Breakdown
                    if (analyticsData.categoryBreakdowns.isNotEmpty()) {
                        item {
                            VeloraSectionHeader(title = "Category Breakdown")
                        }
                        items(analyticsData.categoryBreakdowns) { breakdown ->
                            VeloraCategoryProgressBar(
                                categoryName = breakdown.categoryName,
                                categoryIconName = breakdown.categoryIconName,
                                categoryColorHex = breakdown.categoryColorHex,
                                amountMinorUnits = breakdown.totalMinorUnits,
                                percentage = breakdown.percentage,
                                currencySymbol = currencySymbol
                            )
                        }
                    }

                    // 4. Highest Transactions
                    if (analyticsData.topTransactions.isNotEmpty()) {
                        item {
                            VeloraSectionHeader(title = "Highest Transactions")
                        }
                        items(analyticsData.topTransactions) { tx ->
                            VeloraTransactionRow(
                                transaction = tx,
                                currencySymbol = currencySymbol,
                                onClick = { onTransactionClick(tx.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
