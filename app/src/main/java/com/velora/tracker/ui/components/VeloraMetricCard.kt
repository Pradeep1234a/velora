package com.velora.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.ui.theme.veloraColors

@Composable
fun VeloraMetricCard(
    label: String,
    amountMinorUnits: Long,
    isIncome: Boolean,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val containerColor = if (isIncome) MaterialTheme.veloraColors.incomeContainer else MaterialTheme.veloraColors.expenseContainer
    val contentColor = if (isIncome) MaterialTheme.veloraColors.income else MaterialTheme.veloraColors.expense
    val icon = if (isIncome) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = MoneyFormatter.formatCompact(amountMinorUnits, currencySymbol),
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor
            )
        }
    }
}
