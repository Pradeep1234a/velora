package com.velora.tracker.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.presentation.dashboard.getCategoryEmoji
import com.velora.tracker.ui.components.MoneyFormatter
import com.velora.tracker.ui.components.VeloraEmptyState
import com.velora.tracker.ui.components.VeloraErrorState
import com.velora.tracker.ui.components.VeloraLoadingState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val selectedType by viewModel.selectedType.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()

    // Color tokens matching the exact HTML reference
    val bgCanvas = Color(0xFFF7FAF9)
    val textDark = Color(0xFF101B3A)
    val textMuted = Color(0xFF71809A)
    val borderLine = Color(0xFFE7EEEC)
    val primaryTeal = Color(0xFF0C9188)
    val incomeGreen = Color(0xFF087F4F)
    val incomeSoft = Color(0xFFE6F8EF)
    val incomeBorder = Color(0xFFCCEFE1)
    val expenseRed = Color(0xFFEF3F16)
    val expenseSoft = Color(0xFFFFF0EB)
    val expenseBorder = Color(0xFFFFE0D6)
    val netCyan = Color(0xFF15939A)
    val netSoft = Color(0xFFE5F6F7)
    val netBorder = Color(0xFFD2EEF0)
    val searchBg = Color(0xFFEEF3F5)

    val inflow = remember(transactions) {
        transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amountMinorUnits }
    }
    val outflow = remember(transactions) {
        transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amountMinorUnits }
    }
    val net = inflow - outflow

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgCanvas)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Transactions",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark,
                    letterSpacing = (-0.7).sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Search Bar & Filter Button
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Field
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(26.dp))
                                .background(searchBg)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = Color(0xFF34435F),
                                    modifier = Modifier.size(20.dp)
                                )
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = viewModel::setSearchQuery,
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                                        color = textDark,
                                        fontSize = 15.sp
                                    ),
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                text = "Search transactions...",
                                                color = textMuted,
                                                fontSize = 15.sp
                                            )
                                        }
                                        innerTextField()
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                if (searchQuery.isNotEmpty()) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Clear",
                                        tint = textMuted,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable { viewModel.setSearchQuery("") }
                                    )
                                }
                            }
                        }

                        // Filter Button with Indicator Dot
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.5.dp, Color(0xFFE2E8ED), RoundedCornerShape(16.dp))
                                .clickable {
                                    // Cycle filter: All -> Income -> Expense -> All
                                    val nextType = when (selectedType) {
                                        null -> TransactionType.INCOME
                                        TransactionType.INCOME -> TransactionType.EXPENSE
                                        TransactionType.EXPENSE -> null
                                    }
                                    viewModel.setTypeFilter(nextType)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Filter",
                                tint = Color(0xFF41516E),
                                modifier = Modifier.size(22.dp)
                            )
                            if (selectedType != null) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-4).dp, y = 4.dp)
                                        .clip(CircleShape)
                                        .background(primaryTeal)
                                        .border(1.5.dp, Color.White, CircleShape)
                                )
                            }
                        }
                    }
                }

                // 2. Summary Metric Cards: Income, Expenses, Net
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Income
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.5.dp, incomeBorder, RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(incomeSoft),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowUpward,
                                            contentDescription = "Income",
                                            tint = incomeGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "Income",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textDark
                                    )
                                }
                                Text(
                                    text = MoneyFormatter.format(inflow, currencySymbol, showSign = false),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = incomeGreen,
                                    letterSpacing = (-0.4).sp
                                )
                            }
                        }

                        // Expenses
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.5.dp, expenseBorder, RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(expenseSoft),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowDownward,
                                            contentDescription = "Expenses",
                                            tint = expenseRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "Expenses",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textDark
                                    )
                                }
                                Text(
                                    text = MoneyFormatter.format(outflow, currencySymbol, showSign = false),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = expenseRed,
                                    letterSpacing = (-0.4).sp
                                )
                            }
                        }

                        // Net
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.5.dp, netBorder, RoundedCornerShape(16.dp))
                                .padding(horizontal = 12.dp, vertical = 14.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(netSoft),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "=",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = netCyan
                                        )
                                    }
                                    Text(
                                        text = "Net",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textDark
                                    )
                                }
                                Text(
                                    text = MoneyFormatter.format(net, currencySymbol, showSign = false),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = netCyan,
                                    letterSpacing = (-0.4).sp
                                )
                            }
                        }
                    }
                }

                // 3. Transactions Grouped by Date
                if (isLoading) {
                    item { VeloraLoadingState() }
                } else if (error != null) {
                    item { VeloraErrorState(message = error ?: "Unknown error") }
                } else if (groupedTransactions.isEmpty()) {
                    item { VeloraEmptyState(message = "No transactions found") }
                } else {
                    groupedTransactions.forEach { (date, txs) ->
                        item {
                            val today = LocalDate.now()
                            val dateLabel = when (date) {
                                today -> "Today  •  ${date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}"
                                today.minusDays(1) -> "Yesterday  •  ${date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}"
                                else -> date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                // Date Group Header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateLabel,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textDark
                                    )
                                    Text(
                                        text = "${txs.size} ${if (txs.size == 1) "entry" else "entries"}",
                                        fontSize = 13.sp,
                                        color = textMuted
                                    )
                                }

                                // Card holding date's transactions
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color.White)
                                        .border(1.dp, borderLine, RoundedCornerShape(18.dp))
                                ) {
                                    Column {
                                        txs.forEachIndexed { index, tx ->
                                            val isExpense = tx.type == TransactionType.EXPENSE
                                            val timeStr = tx.dateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                                            val paymentMethodStr = tx.paymentMethod.name.replace("_", " ")

                                            val (badgeBg, badgeTextColor) = getCategoryBadgeColors(tx.categoryName, isExpense)

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable { onTransactionClick(tx.id) }
                                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    // Circular category icon badge
                                                    Box(
                                                        modifier = Modifier
                                                            .size(46.dp)
                                                            .clip(CircleShape)
                                                            .background(badgeBg),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = getCategoryEmoji(tx.categoryName),
                                                            fontSize = 20.sp
                                                        )
                                                    }

                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = tx.title,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = textDark,
                                                            maxLines = 1
                                                        )
                                                        Text(
                                                            text = "${tx.categoryName}  •  $timeStr  •  $paymentMethodStr",
                                                            fontSize = 12.sp,
                                                            color = textMuted,
                                                            maxLines = 1
                                                        )
                                                    }
                                                }

                                                val sign = if (isExpense) "−" else "+"
                                                val amtColor = if (isExpense) expenseRed else incomeGreen
                                                Text(
                                                    text = "$sign${MoneyFormatter.format(tx.amountMinorUnits, currencySymbol, showSign = false)}",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = amtColor,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }

                                            if (index < txs.size - 1) {
                                                HorizontalDivider(
                                                    color = borderLine,
                                                    thickness = 1.dp,
                                                    modifier = Modifier.padding(horizontal = 14.dp)
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
        }

        // Floating Action Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 88.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(primaryTeal)
                .clickable(onClick = onAddTransaction),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Transaction",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun getCategoryBadgeColors(categoryName: String, isExpense: Boolean): Pair<Color, Color> {
    val lower = categoryName.lowercase()
    return when {
        lower.contains("bill") || lower.contains("jio") || lower.contains("broadband") || lower.contains("electric") ->
            Color(0xFFEDF2FF) to Color(0xFF2D67DB)
        lower.contains("food") || lower.contains("dinner") || lower.contains("swiggy") || lower.contains("zomato") ->
            Color(0xFFFFF0EB) to Color(0xFFEF3F16)
        lower.contains("shop") || lower.contains("amazon") ->
            Color(0xFFEEE8FF) to Color(0xFF7138E7)
        lower.contains("transport") || lower.contains("uber") || lower.contains("ola") ->
            Color(0xFFE6F7F7) to Color(0xFF0A999A)
        lower.contains("rent") || lower.contains("home") || lower.contains("house") ->
            Color(0xFFFFF0E9) to Color(0xFFE05B2C)
        !isExpense || lower.contains("salary") || lower.contains("income") ->
            Color(0xFFE6F8EF) to Color(0xFF0A8858)
        lower.contains("grocer") || lower.contains("basket") ->
            Color(0xFFFFF5DF) to Color(0xFFEE9C00)
        else ->
            Color(0xFFEEF3F5) to Color(0xFF475569)
    }
}
