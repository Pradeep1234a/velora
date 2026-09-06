package com.velora.tracker.presentation.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.presentation.dashboard.getCategoryEmoji
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

    val periodSavingsRate = if (analyticsData.totalIncome > 0) {
        String.format("%.1f", ((analyticsData.netCashFlow.toDouble() / analyticsData.totalIncome) * 100).coerceAtLeast(0.0))
    } else "0.0"

    Column(modifier = Modifier.fillMaxSize()) {
        VeloraStitchHeader(
            title = "Analytics",
            onSettingsClick = { /* Settings */ }
        )
        
        // Segmented Period Filter Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(100.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val ranges = listOf(
                    DateRange.ThisWeek to "Week",
                    DateRange.ThisMonth to "Month",
                    DateRange.ThisYear to "Year",
                    DateRange.AllTime to "All"
                )
                ranges.forEach { (range, label) ->
                    val isSelected = selectedDateRange == range
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
                            .clickable { viewModel.selectDateRange(range) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
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
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. NET CASH FLOW Hero Card from Stitch
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shadowElevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "NET CASH FLOW",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        letterSpacing = 1.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(100.dp),
                                        color = Color(0xFFDCFCE7)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.TrendingUp,
                                                contentDescription = null,
                                                tint = Color(0xFF16803C),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = "$periodSavingsRate% Saved",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF16803C)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = "${if (analyticsData.netCashFlow >= 0) "+" else ""}${MoneyFormatter.format(analyticsData.netCashFlow, currencySymbol, showSign = false)}.00",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (analyticsData.netCashFlow >= 0) Color(0xFF005C55) else Color(0xFFC2410C)
                                )

                                // Inflow & Outflow subcards
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerLow
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFF16803C)))
                                                Text("Inflow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Text(
                                                text = "+${MoneyFormatter.format(analyticsData.totalIncome, currencySymbol, showSign = false)}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF16803C)
                                            )
                                        }
                                    }
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerLow
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFFC2410C)))
                                                Text("Outflow", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Text(
                                                text = "-${MoneyFormatter.format(analyticsData.totalExpenses, currencySymbol, showSign = false)}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFC2410C)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 2. Expenses Breakdown with Donut Chart
                    if (analyticsData.categoryBreakdowns.isNotEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shadowElevation = 1.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Expenses Breakdown",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${analyticsData.categoryBreakdowns.size} Categories",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }

                                    VeloraDonutChart(
                                        breakdowns = analyticsData.categoryBreakdowns,
                                        totalSpentMinorUnits = analyticsData.totalExpenses,
                                        currencySymbol = currencySymbol
                                    )

                                    // 2x2 Legend grid below donut
                                    val topCats = analyticsData.categoryBreakdowns.take(4)
                                    val chunked = topCats.chunked(2)
                                    chunked.forEach { rowCats ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            rowCats.forEach { cat ->
                                                val catColor = try {
                                                    Color(android.graphics.Color.parseColor(cat.categoryColorHex))
                                                } catch (e: Exception) {
                                                    MaterialTheme.colorScheme.primary
                                                }
                                                Row(
                                                    modifier = Modifier.weight(1f),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Box(modifier = Modifier.size(8.dp).clip(androidx.compose.foundation.shape.CircleShape).background(catColor))
                                                    Column {
                                                        Text(
                                                            text = cat.categoryName,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.SemiBold,
                                                            maxLines = 1
                                                        )
                                                        Text(
                                                            text = "${MoneyFormatter.format(cat.totalMinorUnits, currencySymbol, showSign = false)} (${cat.percentage.toInt()}%)",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Spending by Category (High to Low)
                    if (analyticsData.categoryBreakdowns.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Spending by Category",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "High to Low",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        items(analyticsData.categoryBreakdowns) { breakdown ->
                            val emoji = getCategoryEmoji(breakdown.categoryName)
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shadowElevation = 0.5.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = emoji, fontSize = 18.sp)
                                            }
                                            Column {
                                                Text(
                                                    text = breakdown.categoryName,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = "${breakdown.percentage.toInt()}% of total",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.outline
                                                )
                                            }
                                        }
                                        Text(
                                            text = MoneyFormatter.format(breakdown.totalMinorUnits, currencySymbol, showSign = false),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Progress bar
                                    val catColor = try {
                                        Color(android.graphics.Color.parseColor(breakdown.categoryColorHex))
                                    } catch (e: Exception) {
                                        MaterialTheme.colorScheme.primary
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth((breakdown.percentage / 100f).coerceIn(0f, 1f))
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(catColor)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. Income vs Expense Trend Bar Chart
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shadowElevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Income vs. Expense Trend",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFF16803C)))
                                            Text("In", style = MaterialTheme.typography.labelSmall)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Box(modifier = Modifier.size(6.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Color(0xFFC2410C)))
                                            Text("Out", style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }

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
                        }
                    }

                    // 5. Highest Transactions
                    if (analyticsData.topTransactions.isNotEmpty()) {
                        item {
                            VeloraSectionHeader(title = "Highest Transactions")
                        }
                        items(analyticsData.topTransactions) { tx ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shadowElevation = 0.5.dp
                            ) {
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
}

