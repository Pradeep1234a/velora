package com.velora.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.velora.tracker.domain.model.CategorySource
import com.velora.tracker.domain.model.PaymentMethod
import com.velora.tracker.domain.model.Transaction
import com.velora.tracker.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amountMinorUnits: Long,
    val type: String, // "INCOME" or "EXPENSE"
    val title: String,
    val categoryId: Long,
    val dateTimeMillis: Long,
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int,
    val minute: Int,
    val notes: String,
    val paymentMethod: String,
    val createdAt: Long,
    val updatedAt: Long,
    val aiSuggestedCategoryId: Long?,
    val aiConfidence: Float?,
    val categorySource: String // "USER", "AI", "RULE"
)

fun TransactionEntity.toDomain(categoryName: String, categoryIconName: String, categoryColorHex: String): Transaction {
    return Transaction(
        id = id,
        amountMinorUnits = amountMinorUnits,
        type = TransactionType.valueOf(type),
        title = title,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryIconName = categoryIconName,
        categoryColorHex = categoryColorHex,
        dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTimeMillis), ZoneId.systemDefault()),
        notes = notes,
        paymentMethod = try { PaymentMethod.valueOf(paymentMethod) } catch(e: Exception) { PaymentMethod.OTHER },
        createdAt = createdAt,
        updatedAt = updatedAt,
        aiSuggestedCategoryId = aiSuggestedCategoryId,
        aiConfidence = aiConfidence,
        categorySource = try { CategorySource.valueOf(categorySource) } catch(e: Exception) { CategorySource.USER }
    )
}

fun Transaction.toEntity(): TransactionEntity {
    val zdt = dateTime.atZone(ZoneId.systemDefault())
    return TransactionEntity(
        id = id,
        amountMinorUnits = amountMinorUnits,
        type = type.name,
        title = title,
        categoryId = categoryId,
        dateTimeMillis = zdt.toInstant().toEpochMilli(),
        year = dateTime.year,
        month = dateTime.monthValue,
        dayOfMonth = dateTime.dayOfMonth,
        hour = dateTime.hour,
        minute = dateTime.minute,
        notes = notes,
        paymentMethod = paymentMethod.name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        aiSuggestedCategoryId = aiSuggestedCategoryId,
        aiConfidence = aiConfidence,
        categorySource = categorySource.name
    )
}
