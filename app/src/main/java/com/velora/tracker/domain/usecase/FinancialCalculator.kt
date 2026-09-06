package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.CategoryBreakdown
import com.velora.tracker.domain.model.SpendingPoint
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import java.time.format.DateTimeFormatter

object FinancialCalculator {
    fun totalIncome(transactions: List<Transaction>): Long {
        return transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amountMinorUnits }
    }

    fun totalExpenses(transactions: List<Transaction>): Long {
        return transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amountMinorUnits }
    }

    fun netCashFlow(income: Long, expenses: Long): Long = income - expenses

    fun currentBalance(openingBalance: Long, totalIncome: Long, totalExpenses: Long): Long =
        openingBalance + totalIncome - totalExpenses

    fun categoryBreakdown(
        transactions: List<Transaction>,
        categories: List<Category>
    ): List<CategoryBreakdown> {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val totalExp = expenses.sumOf { it.amountMinorUnits }
        
        if (totalExp == 0L) return emptyList()

        val grouped = expenses.groupBy { it.categoryId }
        
        return grouped.map { (categoryId, txs) ->
            val sum = txs.sumOf { it.amountMinorUnits }
            val category = categories.find { it.id == categoryId }
            val percent = (sum.toFloat() / totalExp.toFloat()) * 100f
            
            CategoryBreakdown(
                categoryId = categoryId,
                categoryName = category?.name ?: "Unknown",
                categoryIconName = category?.iconName ?: "help",
                categoryColorHex = category?.colorHex ?: "#9E9E9E",
                totalMinorUnits = sum,
                percentage = percent
            )
        }.sortedByDescending { it.totalMinorUnits }
    }

    fun spendingTrend(transactions: List<Transaction>, granularity: String): List<SpendingPoint> {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        if (expenses.isEmpty()) return emptyList()

        val formatter = when (granularity) {
            "daily" -> DateTimeFormatter.ofPattern("MMM dd")
            "monthly" -> DateTimeFormatter.ofPattern("MMM yyyy")
            else -> DateTimeFormatter.ofPattern("MMM dd")
        }

        val grouped = expenses.groupBy { it.dateTime.format(formatter) }
        
        return grouped.map { (label, txs) ->
            SpendingPoint(
                label = label,
                amountMinorUnits = txs.sumOf { it.amountMinorUnits }
            )
        }.toList()
    }
}
