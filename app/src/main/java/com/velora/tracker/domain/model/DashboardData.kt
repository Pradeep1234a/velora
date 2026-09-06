package com.velora.tracker.domain.model

data class DashboardData(
    val currentBalance: Long = 0, // opening + all income - all expenses (cumulative)
    val periodIncome: Long = 0,
    val periodExpenses: Long = 0,
    val periodNetCashFlow: Long = 0,
    val categoryBreakdowns: List<CategoryBreakdown> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
)

data class CategoryBreakdown(
    val categoryId: Long,
    val categoryName: String,
    val categoryIconName: String,
    val categoryColorHex: String,
    val totalMinorUnits: Long,
    val percentage: Float // 0..100
)
