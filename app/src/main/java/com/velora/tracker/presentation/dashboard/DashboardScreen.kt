package com.velora.tracker.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.*
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
        VeloraStitchHeader(
            title = "Dashboard",
            onSettingsClick = onSettingsClick
        )

        if (error != null) {
            VeloraEmptyState(
                icon = Icons.Filled.Warning,
                title = "Error",
                message = error ?: "Unknown error"
            )
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 96.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Month & Reconciled Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "This Month",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    text = "Live reconciled",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Hero Current Balance Card
                    VeloraStitchBalanceCard(
                        amountMinorUnits = dashboardData.currentBalance,
                        netThisMonthMinorUnits = dashboardData.periodNetCashFlow,
                        baseBalanceMinorUnits = (dashboardData.currentBalance - dashboardData.periodNetCashFlow).coerceAtLeast(0L),
                        currencySymbol = currencySymbol
                    )

                    // Twin Income & Expense Cards
                    VeloraTwinMetricCards(
                        incomeMinorUnits = dashboardData.periodIncome,
                        expenseMinorUnits = dashboardData.periodExpenses,
                        currencySymbol = currencySymbol
                    )

                    // Net Cash Flow Progress Bar Card
                    VeloraNetCashFlowCard(
                        incomeMinorUnits = dashboardData.periodIncome,
                        expenseMinorUnits = dashboardData.periodExpenses,
                        netMinorUnits = dashboardData.periodNetCashFlow,
                        currencySymbol = currencySymbol
                    )
                }
            }

            // Spending Categories 2x2 Grid
            if (dashboardData.categoryBreakdowns.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Spending Categories",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "See all",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val items = dashboardData.categoryBreakdowns.take(4)
                        val chunked = items.chunked(2)
                        chunked.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { cat ->
                                    val emoji = getCategoryEmoji(cat.categoryName)
                                    VeloraCategoryGridCard(
                                        icon = emoji,
                                        name = cat.categoryName,
                                        amountMinorUnits = cat.totalMinorUnits,
                                        percentage = cat.percentage,
                                        colorHex = cat.categoryColorHex,
                                        currencySymbol = currencySymbol,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Recent Transactions
            item {
                Spacer(modifier = Modifier.height(16.dp))
                VeloraSectionHeader(
                    title = "Recent Transactions",
                    action = "View all",
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
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 0.5.dp
                    ) {
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
}

fun getCategoryEmoji(name: String): String {
    val lower = name.lowercase()
    return when {
        lower.contains("food") || lower.contains("dining") || lower.contains("restaurant") -> "🍔"
        lower.contains("grocer") -> "🛒"
        lower.contains("house") || lower.contains("rent") || lower.contains("home") -> "🏠"
        lower.contains("transport") || lower.contains("uber") || lower.contains("taxi") -> "🚗"
        lower.contains("shop") -> "🛍️"
        lower.contains("health") || lower.contains("med") -> "💊"
        lower.contains("entertain") || lower.contains("movie") -> "🎬"
        lower.contains("bill") || lower.contains("util") -> "💡"
        lower.contains("salary") || lower.contains("work") -> "💼"
        lower.contains("coffee") || lower.contains("cafe") -> "☕"
        else -> "💰"
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
