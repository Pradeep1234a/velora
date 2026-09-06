package com.velora.tracker.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.velora.tracker.ui.theme.veloraColors

@Composable
fun VeloraAmountText(
    amountMinorUnits: Long,
    isExpense: Boolean,
    currencySymbol: String = "₹",
    style: TextStyle = MaterialTheme.typography.titleSmall,
    modifier: Modifier = Modifier
) {
    val formattedAmount = MoneyFormatter.format(
        amountMinorUnits = amountMinorUnits,
        currencySymbol = currencySymbol,
        showSign = true,
        isExpense = isExpense
    )
    
    val color = if (isExpense) {
        MaterialTheme.veloraColors.expense
    } else {
        MaterialTheme.veloraColors.income
    }
    
    Text(
        text = formattedAmount,
        style = style,
        color = color,
        modifier = modifier
    )
}
