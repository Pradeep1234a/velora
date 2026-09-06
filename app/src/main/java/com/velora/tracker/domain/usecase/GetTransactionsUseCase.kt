package com.velora.tracker.domain.usecase

import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.domain.model.toStartEnd
import com.velora.tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId

enum class SortOrder { NEWEST, OLDEST, HIGHEST_AMOUNT, LOWEST_AMOUNT }

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        dateRange: DateRange?,
        searchQuery: String?,
        categoryId: Long?,
        type: TransactionType?,
        sortOrder: SortOrder
    ): Flow<List<Transaction>> {
        val flow = if (searchQuery.isNullOrBlank()) {
            if (dateRange != null) {
                val (start, end) = dateRange.toStartEnd()
                val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val endMillis = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
                transactionRepository.getTransactionsByDateRange(startMillis, endMillis)
            } else {
                transactionRepository.getAllTransactions()
            }
        } else {
            transactionRepository.searchTransactions(searchQuery)
        }

        return flow.map { txs ->
            var filtered = txs
            if (categoryId != null) {
                filtered = filtered.filter { it.categoryId == categoryId }
            }
            if (type != null) {
                filtered = filtered.filter { it.type == type }
            }
            if (!searchQuery.isNullOrBlank() && dateRange != null) {
                val (start, end) = dateRange.toStartEnd()
                val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val endMillis = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1
                filtered = filtered.filter { 
                    val m = it.dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    m in startMillis..endMillis
                }
            }

            when (sortOrder) {
                SortOrder.NEWEST -> filtered.sortedByDescending { it.dateTime }
                SortOrder.OLDEST -> filtered.sortedBy { it.dateTime }
                SortOrder.HIGHEST_AMOUNT -> filtered.sortedByDescending { it.amountMinorUnits }
                SortOrder.LOWEST_AMOUNT -> filtered.sortedBy { it.amountMinorUnits }
            }
        }
    }
}
