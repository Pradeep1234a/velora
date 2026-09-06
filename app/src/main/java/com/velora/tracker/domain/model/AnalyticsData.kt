package com.velora.tracker.domain.model

data class AnalyticsData(
    val totalIncome: Long = 0,
    val totalExpenses: Long = 0,
    val netCashFlow: Long = 0,
    val categoryBreakdowns: List<CategoryBreakdown> = emptyList(),
    val spendingTrend: List<SpendingPoint> = emptyList(),
    val topTransactions: List<Transaction> = emptyList()
)

data class SpendingPoint(
    val label: String, // "Mon", "Sep 1", "Jan", etc.
    val amountMinorUnits: Long
)
