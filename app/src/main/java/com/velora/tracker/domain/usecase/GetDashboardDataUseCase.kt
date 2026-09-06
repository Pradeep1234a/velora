package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.DashboardData
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.model.toStartEnd
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.ZoneId

class GetDashboardDataUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(dateRange: DateRange): Flow<DashboardData> {
        val (start, end) = dateRange.toStartEnd()
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1

        return combine(
            transactionRepository.getAllTransactions(),
            transactionRepository.getTransactionsByDateRange(startMillis, endMillis),
            categoryRepository.getAllCategories(),
            settingsRepository.getOpeningBalance()
        ) { allTxs, periodTxs, categories, openingBalance ->
            val totalIncome = FinancialCalculator.totalIncome(allTxs)
            val totalExpenses = FinancialCalculator.totalExpenses(allTxs)
            val currentBalance = FinancialCalculator.currentBalance(openingBalance, totalIncome, totalExpenses)

            val periodIncome = FinancialCalculator.totalIncome(periodTxs)
            val periodExpenses = FinancialCalculator.totalExpenses(periodTxs)
            val periodNetCashFlow = FinancialCalculator.netCashFlow(periodIncome, periodExpenses)

            val breakdowns = FinancialCalculator.categoryBreakdown(periodTxs, categories)
            
            DashboardData(
                currentBalance = currentBalance,
                periodIncome = periodIncome,
                periodExpenses = periodExpenses,
                periodNetCashFlow = periodNetCashFlow,
                categoryBreakdowns = breakdowns,
                recentTransactions = allTxs.take(10)
            )
        }
    }
}
