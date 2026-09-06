package com.velora.tracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.R
import com.velora.tracker.ui.theme.Spacing

@Composable
fun VeloraStitchHeader(
    title: String,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_velora_logo),
                contentDescription = "Velora Logo",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column {
                Text(
                    text = "Velora",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onSettingsClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun VeloraStitchBalanceCard(
    amountMinorUnits: Long,
    netThisMonthMinorUnits: Long,
    baseBalanceMinorUnits: Long,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CURRENT BALANCE",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                IconButton(
                    onClick = { isVisible = !isVisible },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "Toggle Visibility",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (isVisible) {
                val formatted = MoneyFormatter.format(amountMinorUnits, currencySymbol, showSign = false)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formatted,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = ".00",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }
            } else {
                Text(
                    text = "$currencySymbol ••••••",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                val netText = MoneyFormatter.format(netThisMonthMinorUnits, currencySymbol, showSign = true)
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF16803C),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Net $netText this month",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16803C)
                        )
                    }
                }
                Text(
                    text = "from ${MoneyFormatter.formatCompact(baseBalanceMinorUnits, currencySymbol)} base",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun VeloraTwinMetricCards(
    incomeMinorUnits: Long,
    expenseMinorUnits: Long,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Income Card (Soft Mint)
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFF0FDF4),
            shadowElevation = 0.5.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = null,
                            tint = Color(0xFF16803C),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "+100%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF15803D)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "+${MoneyFormatter.format(incomeMinorUnits, currencySymbol, showSign = false)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF16803C)
                )
                Text(
                    text = "Total Income",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF15803D)
                )
            }
        }

        // Expense Card (Soft Peach)
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFF7ED),
            shadowElevation = 0.5.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEDD5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = Color(0xFFC2410C),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "-14.3%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9A3412)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "-${MoneyFormatter.format(expenseMinorUnits, currencySymbol, showSign = false)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC2410C)
                )
                Text(
                    text = "Total Expenses",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF9A3412)
                )
            }
        }
    }
}

@Composable
fun VeloraNetCashFlowCard(
    incomeMinorUnits: Long,
    expenseMinorUnits: Long,
    netMinorUnits: Long,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val total = (incomeMinorUnits + expenseMinorUnits).coerceAtLeast(1L)
    val incomePct = ((incomeMinorUnits.toDouble() / total) * 100).toInt().coerceIn(0, 100)
    val expensePct = (100 - incomePct).coerceIn(0, 100)
    val savingsRate = if (incomeMinorUnits > 0) {
        String.format("%.1f", ((netMinorUnits.toDouble() / incomeMinorUnits) * 100).coerceAtLeast(0.0))
    } else "0.0"

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF16803C)))
                    Text(
                        text = "Income ${MoneyFormatter.format(incomeMinorUnits, currencySymbol, showSign = false)} ($incomePct%)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16803C)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFC2410C)))
                    Text(
                        text = "Expense ${MoneyFormatter.format(expenseMinorUnits, currencySymbol, showSign = false)} ($expensePct%)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC2410C)
                    )
                }
            }

            // Dual Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                if (incomePct > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(incomePct.toFloat().coerceAtLeast(0.01f))
                            .background(Color(0xFF16803C))
                    )
                }
                if (expensePct > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(expensePct.toFloat().coerceAtLeast(0.01f))
                            .background(Color(0xFFC2410C))
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Net Cash Flow",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${MoneyFormatter.format(netMinorUnits, currencySymbol, showSign = false)} saved ($savingsRate% savings rate)",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun VeloraCategoryGridCard(
    icon: String,
    name: String,
    amountMinorUnits: Long,
    percentage: Float,
    colorHex: String,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val barColor = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        shadowElevation = 0.5.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 18.sp)
                }
                Text(
                    text = "${percentage.toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = MoneyFormatter.format(amountMinorUnits, currencySymbol, showSign = false),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
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
                        .fillMaxWidth((percentage / 100f).coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(3.dp))
                        .background(barColor)
                )
            }
        }
    }
}
