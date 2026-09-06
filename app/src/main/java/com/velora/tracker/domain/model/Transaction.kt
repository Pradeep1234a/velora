package com.velora.tracker.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val amountMinorUnits: Long, // stored in paise, always positive
    val type: TransactionType,
    val title: String,
    val categoryId: Long,
    val categoryName: String = "",
    val categoryIconName: String = "",
    val categoryColorHex: String = "",
    val dateTime: LocalDateTime,
    val notes: String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val aiSuggestedCategoryId: Long? = null,
    val aiConfidence: Float? = null,
    val categorySource: CategorySource = CategorySource.USER
)
