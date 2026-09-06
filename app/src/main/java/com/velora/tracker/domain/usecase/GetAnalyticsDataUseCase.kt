package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.AnalyticsData
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.model.toStartEnd
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.ZoneId

class GetAnalyticsDataUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(dateRange: DateRange, trendGranularity: String): Flow<AnalyticsData> {
        val (start, end) = dateRange.toStartEnd()
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        return combine(
            transactionRepository.getTransactionsByDateRange(startMillis, endMillis),
            categoryRepository.getAllCategories()
        ) { periodTxs, categories ->
            val periodIncome = FinancialCalculator.totalIncome(periodTxs)
            val periodExpenses = FinancialCalculator.totalExpenses(periodTxs)
            val periodNetCashFlow = FinancialCalculator.netCashFlow(periodIncome, periodExpenses)

            val breakdowns = FinancialCalculator.categoryBreakdown(periodTxs, categories)
            val trends = FinancialCalculator.spendingTrend(periodTxs, trendGranularity)
            
            val topTxs = periodTxs.filter { it.type == TransactionType.EXPENSE }
                .sortedByDescending { it.amountMinorUnits }
                .take(5)

            AnalyticsData(
                totalIncome = periodIncome,
                totalExpenses = periodExpenses,
                netCashFlow = periodNetCashFlow,
                categoryBreakdowns = breakdowns,
                spendingTrend = trends,
                topTransactions = topTxs
            )
        }
    }
}
