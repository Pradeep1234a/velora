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

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight

@Composable
fun VeloraMetricCard(
    label: String,
    amountMinorUnits: Long,
    isIncome: Boolean,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    val cardBg = if (isIncome) MaterialTheme.veloraColors.incomeCard else MaterialTheme.veloraColors.expenseCard
    val badgeBg = if (isIncome) MaterialTheme.veloraColors.incomeContainer else MaterialTheme.veloraColors.expenseContainer
    val contentColor = if (isIncome) MaterialTheme.veloraColors.income else MaterialTheme.veloraColors.expense
    val labelColor = if (isIncome) MaterialTheme.veloraColors.incomeLabel else MaterialTheme.veloraColors.expenseLabel
    val icon = if (isIncome) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = cardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${if (isIncome) "+" else "-"}${MoneyFormatter.formatCompact(amountMinorUnits, currencySymbol)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = labelColor
            )
        }
    }
}
