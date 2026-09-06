package com.velora.tracker.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.R
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.MoneyFormatter
import com.velora.tracker.ui.components.VeloraEmptyState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onTransactionClick: (Long) -> Unit,
    onSeeAllTransactions: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddExpense: () -> Unit = {},
    onAddIncome: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {},
    onNavigateToCategories: () -> Unit = {}
) {
    val dashboardData by viewModel.dashboardData.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val error by viewModel.error.collectAsState()

    var isBalanceVisible by remember { mutableStateOf(true) }

    val bgCanvas = Color(0xFFF9FBFC)
    val textMain = Color(0xFF17212B)
    val textMuted = Color(0xFF667582)
    val textSubtle = Color(0xFF8A99A8)
    val borderLine = Color(0xFFE4EAED)
    val primaryTeal = Color(0xFF0F8F84)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgCanvas)
    ) {
        // Top App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_velora_logo),
                    contentDescription = "Velora Logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Text(
                    text = "Velora",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = textMain,
                    letterSpacing = (-0.3).sp
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2F6))
                    .clickable(onClick = onSettingsClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        if (error != null) {
            VeloraEmptyState(
                icon = Icons.Filled.Warning,
                title = "Error",
                message = error ?: "Unknown error"
            )
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Current Balance Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(144.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF064E48), Color(0xFF0B8C83)),
                                start = Offset(0f, 0f),
                                end = Offset(800f, 300f)
                            )
                        )
                ) {
                    // Soft decorative botanical leaves in background
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val leafPath1 = Path().apply {
                            moveTo(size.width * 0.70f, size.height * 0.95f)
                            cubicTo(
                                size.width * 0.65f, size.height * 0.40f,
                                size.width * 0.80f, size.height * 0.20f,
                                size.width * 0.96f, size.height * 0.25f
                            )
                            cubicTo(
                                size.width * 0.98f, size.height * 0.65f,
                                size.width * 0.85f, size.height * 0.85f,
                                size.width * 0.70f, size.height * 0.95f
                            )
                            close()
                        }
                        drawPath(leafPath1, color = Color(0xFF14B8A6).copy(alpha = 0.28f))

                        val leafPath2 = Path().apply {
                            moveTo(size.width * 0.82f, size.height * 0.95f)
                            cubicTo(
                                size.width * 0.78f, size.height * 0.65f,
                                size.width * 0.88f, size.height * 0.50f,
                                size.width * 0.98f, size.height * 0.60f
                            )
                            cubicTo(
                                size.width * 0.96f, size.height * 0.85f,
                                size.width * 0.88f, size.height * 0.95f,
                                size.width * 0.82f, size.height * 0.95f
                            )
                            close()
                        }
                        drawPath(leafPath2, color = Color(0xFF10B981).copy(alpha = 0.35f))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Balance",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.90f)
                            )
                            IconButton(
                                onClick = { isBalanceVisible = !isBalanceVisible },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBalanceVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle Balance Visibility",
                                    tint = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isBalanceVisible) {
                                MoneyFormatter.format(dashboardData.currentBalance, currencySymbol, showSign = false)
                            } else {
                                "$currencySymbol••••••"
                            },
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Text(
                                text = "Updated today, 9:41 AM",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // 2. Twin Metric Cards: Income & Expenses
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Income Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFEFFAF3))
                            .border(1.dp, Color(0xFFD4F3E2), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFC7F7D8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = "Income",
                                    tint = Color(0xFF16803C),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Income",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textMuted
                                )
                                Text(
                                    text = MoneyFormatter.format(dashboardData.periodIncome, currencySymbol, showSign = false),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                                Text(
                                    text = "This month",
                                    fontSize = 11.sp,
                                    color = textSubtle
                                )
                            }
                        }
                    }

                    // Expenses Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFFFF3EE))
                            .border(1.dp, Color(0xFFFFDFD5), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFDFD5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDownward,
                                    contentDescription = "Expenses",
                                    tint = Color(0xFFD94828),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Expenses",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textMuted
                                )
                                Text(
                                    text = MoneyFormatter.format(dashboardData.periodExpenses, currencySymbol, showSign = false),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                                Text(
                                    text = "This month",
                                    fontSize = 11.sp,
                                    color = textSubtle
                                )
                            }
                        }
                    }
                }
            }

            // 3. Quick Actions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMain
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Add Expense
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE9FAF5))
                                .clickable(onClick = onAddExpense),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0F8F84)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Add Expense",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "Add Expense",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                            }
                        }

                        // Add Income
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE9FAF5))
                                .clickable(onClick = onAddIncome),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF16803C)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowUpward,
                                        contentDescription = "Add Income",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "Add Income",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                            }
                        }

                        // Analytics
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFEEF5FF))
                                .clickable(onClick = onNavigateToAnalytics),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFDCE9FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.BarChart,
                                        contentDescription = "Analytics",
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "Analytics",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                            }
                        }

                        // Categories
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(78.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF4EEFF))
                                .clickable(onClick = onNavigateToCategories),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEBE0FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.GridView,
                                        contentDescription = "Categories",
                                        tint = Color(0xFF7C3AED),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Text(
                                    text = "Categories",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textMain
                                )
                            }
                        }
                    }
                }
            }

            // 4. Spending Categories
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Spending Categories",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMain
                        )
                        Text(
                            text = "See all  ›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTeal,
                            modifier = Modifier.clickable(onClick = onNavigateToCategories)
                        )
                    }

                    // 2x2 Category Grid
                    val categories = if (dashboardData.categoryBreakdowns.isNotEmpty()) {
                        dashboardData.categoryBreakdowns.take(4)
                    } else {
                        emptyList()
                    }

                    if (categories.isNotEmpty()) {
                        val chunked = categories.chunked(2)
                        chunked.forEach { rowCats ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowCats.forEach { cat ->
                                    val catColor = parseHexColor(cat.categoryColorHex, Color(0xFFF97316))
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.White)
                                            .border(1.dp, borderLine, RoundedCornerShape(16.dp))
                                            .padding(14.dp)
                                    ) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(38.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(catColor.copy(alpha = 0.15f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = getCategoryEmoji(cat.categoryName),
                                                        fontSize = 18.sp
                                                    )
                                                }
                                                Column {
                                                    Text(
                                                        text = cat.categoryName,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = textMain
                                                    )
                                                    Text(
                                                        text = MoneyFormatter.format(cat.totalMinorUnits, currencySymbol, showSign = false),
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = textMain
                                                    )
                                                }
                                            }

                                            // Progress Bar
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(4.dp)
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(Color(0xFFE4EAED))
                                                ) {
                                                    val pctFraction = (cat.percentage / 100f).coerceIn(0f, 1f)
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(pctFraction)
                                                            .fillMaxHeight()
                                                            .clip(RoundedCornerShape(2.dp))
                                                            .background(catColor)
                                                    )
                                                }
                                                Text(
                                                    text = "${cat.percentage}%",
                                                    fontSize = 11.sp,
                                                    color = textMuted,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                                if (rowCats.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 5. Recent Transactions
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Transactions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMain
                        )
                        Text(
                            text = "View all  ›",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTeal,
                            modifier = Modifier.clickable(onClick = onSeeAllTransactions)
                        )
                    }

                    if (dashboardData.recentTransactions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White)
                                .border(1.dp, borderLine, RoundedCornerShape(18.dp))
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No recent transactions",
                                fontSize = 14.sp,
                                color = textMuted
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White)
                                .border(1.dp, borderLine, RoundedCornerShape(18.dp))
                        ) {
                            Column {
                                val txList = dashboardData.recentTransactions.take(5)
                                txList.forEachIndexed { index, tx ->
                                    val isExpense = tx.type == TransactionType.EXPENSE
                                    val catColor = parseHexColor(tx.categoryColorHex, if (isExpense) Color(0xFFD94828) else Color(0xFF16803C))

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
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(catColor.copy(alpha = 0.12f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = getCategoryEmoji(tx.categoryName),
                                                    fontSize = 18.sp
                                                )
                                            }
                                            Column {
                                                Text(
                                                    text = tx.title,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = textMain
                                                )
                                                Text(
                                                    text = "${tx.categoryName} · ${formatTransactionDateTime(tx.dateTime)}",
                                                    fontSize = 11.sp,
                                                    color = textMuted
                                                )
                                            }
                                        }

                                        val sign = if (isExpense) "−" else "+"
                                        val amtColor = if (isExpense) Color(0xFFD94828) else Color(0xFF16803C)
                                        Text(
                                            text = "$sign${MoneyFormatter.format(tx.amountMinorUnits, currencySymbol, showSign = false)}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = amtColor
                                        )
                                    }

                                    if (index < txList.size - 1) {
                                        HorizontalDivider(color = borderLine, thickness = 1.dp)
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

private fun parseHexColor(hex: String, fallback: Color): Color {
    return try {
        val clean = hex.removePrefix("#")
        val colorInt = clean.toLong(16)
        if (clean.length == 6) {
            Color(colorInt or 0xFF000000)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        fallback
    }
}

fun getCategoryEmoji(name: String): String {
    val lower = name.lowercase()
    return when {
        lower.contains("food") || lower.contains("dinner") || lower.contains("swiggy") || lower.contains("zomato") || lower.contains("dining") -> "🍽️"
        lower.contains("grocer") -> "🛒"
        lower.contains("rent") || lower.contains("house") || lower.contains("home") -> "🏠"
        lower.contains("bill") || lower.contains("jio") || lower.contains("broadband") || lower.contains("wifi") || lower.contains("electric") -> "🚗"
        lower.contains("transport") || lower.contains("uber") || lower.contains("taxi") -> "🚗"
        lower.contains("shop") -> "🛍️"
        lower.contains("health") || lower.contains("med") -> "💊"
        lower.contains("entertain") || lower.contains("movie") -> "🎬"
        lower.contains("salary") || lower.contains("income") -> "💼"
        lower.contains("coffee") || lower.contains("cafe") -> "☕"
        else -> "💰"
    }
}

fun formatTransactionDateTime(dateTime: LocalDateTime): String {
    val today = LocalDate.now()
    val date = dateTime.toLocalDate()
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    
    return when {
        date == today -> "Today, ${dateTime.format(timeFormatter)}"
        date == today.minusDays(1) -> "Yesterday, ${dateTime.format(timeFormatter)}"
        else -> "${dateTime.format(DateTimeFormatter.ofPattern("MMM d"))}, ${dateTime.format(timeFormatter)}"
    }
}
