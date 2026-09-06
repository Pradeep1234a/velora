package com.velora.tracker.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.MoneyFormatter
import com.velora.tracker.ui.components.VeloraCategoryProgressBar
import com.velora.tracker.ui.components.VeloraDashboardTopBar
import com.velora.tracker.ui.components.VeloraEmptyState
import com.velora.tracker.ui.components.VeloraMetricCard
import com.velora.tracker.ui.components.VeloraSectionHeader
import com.velora.tracker.ui.components.VeloraSummaryCard
import com.velora.tracker.ui.components.VeloraTransactionRow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTransactionClick: (Long) -> Unit,
    onSeeAllTransactions: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val dashboardData by viewModel.dashboardData.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        VeloraDashboardTopBar(onSettingsClick = onSettingsClick)

        if (error != null) {
            VeloraEmptyState(
                icon = Icons.Filled.Warning,
                title = "Error",
                message = error ?: "Unknown error"
            )
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    VeloraSummaryCard(
                        label = "Current Balance",
                        amountMinorUnits = dashboardData.currentBalance,
                        currencySymbol = currencySymbol
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        VeloraMetricCard(
                            label = "Income",
                            amountMinorUnits = dashboardData.periodIncome,
                            isIncome = true,
                            currencySymbol = currencySymbol,
                            modifier = Modifier.weight(1f)
                        )
                        VeloraMetricCard(
                            label = "Expenses",
                            amountMinorUnits = dashboardData.periodExpenses,
                            isIncome = false,
                            currencySymbol = currencySymbol,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Net Cash Flow",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = MoneyFormatter.format(dashboardData.periodNetCashFlow, currencySymbol, showSign = true),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (dashboardData.categoryBreakdowns.isNotEmpty()) {
                item {
                    VeloraSectionHeader(title = "Spending Overview")
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                items(dashboardData.categoryBreakdowns) { breakdown ->
                    VeloraCategoryProgressBar(
                        categoryName = breakdown.categoryName,
                        categoryIconName = breakdown.categoryIconName,
                        categoryColorHex = breakdown.categoryColorHex,
                        amountMinorUnits = breakdown.totalMinorUnits,
                        percentage = breakdown.percentage,
                        currencySymbol = currencySymbol,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            item {
                VeloraSectionHeader(
                    title = "Recent Transactions",
                    action = "See All",
                    onActionClick = onSeeAllTransactions
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (dashboardData.recentTransactions.isEmpty()) {
                item {
                    Text(
                        text = "No recent transactions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(dashboardData.recentTransactions) { tx ->
                    VeloraTransactionRow(
                        title = tx.title,
                        categoryName = tx.categoryName,
                        categoryIconName = tx.categoryIconName,
                        categoryColorHex = tx.categoryColorHex,
                        amountMinorUnits = tx.amountMinorUnits,
                        isExpense = tx.type == TransactionType.EXPENSE,
                        dateTimeFormatted = formatTransactionDateTime(tx.dateTime),
                        currencySymbol = currencySymbol,
                        onClick = { onTransactionClick(tx.id) }
                    )
                }
            }
        }
    }
}

fun formatTransactionDateTime(dateTime: LocalDateTime): String {
    val today = LocalDate.now()
    val date = dateTime.toLocalDate()
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    
    return when {
        date == today -> "Today · ${dateTime.format(timeFormatter)}"
        date == today.minusDays(1) -> "Yesterday · ${dateTime.format(timeFormatter)}"
        else -> "${dateTime.format(DateTimeFormatter.ofPattern("MMM d"))} · ${dateTime.format(timeFormatter)}"
    }
}
