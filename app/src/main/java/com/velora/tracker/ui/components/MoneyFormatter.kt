package com.velora.tracker.ui.components

import java.text.NumberFormat
import java.util.Locale

object MoneyFormatter {
    fun format(amountMinorUnits: Long, currencySymbol: String = "₹", showSign: Boolean = false, isExpense: Boolean = false): String {
        val major = amountMinorUnits / 100
        val minor = amountMinorUnits % 100
        
        val numberStr = if (minor == 0L) {
            formatWithGrouping(major)
        } else {
            "${formatWithGrouping(major)}.${minor.toString().padStart(2, '0')}"
        }
        
        val sign = when {
            !showSign -> ""
            isExpense -> "− "  // Unicode minus U+2212
            else -> "+ "
        }
        
        return "$sign$currencySymbol$numberStr"
    }
    
    private fun formatWithGrouping(value: Long): String {
        val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
        return format.format(value)
    }
    
    fun formatCompact(amountMinorUnits: Long, currencySymbol: String = "₹"): String {
        val major = amountMinorUnits / 100
        return when {
            major >= 10000000 -> "$currencySymbol${major / 10000000}Cr"
            major >= 100000 -> "$currencySymbol${major / 100000}L"
            major >= 1000 -> "$currencySymbol${major / 1000}K"
            else -> format(amountMinorUnits, currencySymbol)
        }
    }
}
