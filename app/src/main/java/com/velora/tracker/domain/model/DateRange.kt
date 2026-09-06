package com.velora.tracker.domain.model

import java.time.LocalDate

sealed class DateRange {
    data object ThisWeek : DateRange()
    data object ThisMonth : DateRange()
    data object LastMonth : DateRange()
    data object ThisYear : DateRange()
    data object AllTime : DateRange()
    data class Custom(val start: LocalDate, val end: LocalDate) : DateRange()
}

fun DateRange.toStartEnd(): Pair<LocalDate, LocalDate> {
    val today = LocalDate.now()
    return when (this) {
        is DateRange.ThisWeek -> {
            val start = today.minusDays(today.dayOfWeek.value.toLong() - 1)
            Pair(start, today)
        }
        is DateRange.ThisMonth -> {
            val start = today.withDayOfMonth(1)
            Pair(start, today)
        }
        is DateRange.LastMonth -> {
            val start = today.minusMonths(1).withDayOfMonth(1)
            val end = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth())
            Pair(start, end)
        }
        is DateRange.ThisYear -> {
            val start = today.withDayOfYear(1)
            Pair(start, today)
        }
        is DateRange.AllTime -> {
            Pair(LocalDate.MIN, LocalDate.MAX)
        }
        is DateRange.Custom -> {
            Pair(start, end)
        }
    }
}
