package com.velora.tracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType

@Composable
fun VeloraTransactionRow(
    transaction: Transaction,
    formattedTime: String = "",
    currencySymbol: String = "₹",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    VeloraTransactionRow(
        title = transaction.title,
        categoryName = transaction.categoryName.ifEmpty { "General" },
        categoryIconName = transaction.categoryIconName.ifEmpty { "more_horiz" },
        categoryColorHex = transaction.categoryColorHex.ifEmpty { "#90A4AE" },
        amountMinorUnits = transaction.amountMinorUnits,
        isExpense = transaction.type == TransactionType.EXPENSE,
        dateTimeFormatted = if (formattedTime.isNotEmpty()) formattedTime else transaction.dateTime.toString(),
        currencySymbol = currencySymbol,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun VeloraTransactionRow(
    title: String,
    categoryName: String,
    categoryIconName: String,
    categoryColorHex: String,
    amountMinorUnits: Long,
    isExpense: Boolean,
    dateTimeFormatted: String,
    currencySymbol: String = "₹",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 72.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VeloraCategoryIcon(
            iconName = categoryIconName,
            colorHex = categoryColorHex,
            size = 40.dp
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$categoryName · $dateTimeFormatted",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        VeloraAmountText(
            amountMinorUnits = amountMinorUnits,
            isExpense = isExpense,
            currencySymbol = currencySymbol,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
