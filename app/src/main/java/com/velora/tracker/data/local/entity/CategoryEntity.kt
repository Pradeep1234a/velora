package com.velora.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: String,
    val isDefault: Boolean,
    val sortOrder: Int
)

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        iconName = iconName,
        colorHex = colorHex,
        type = TransactionType.valueOf(type),
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        iconName = iconName,
        colorHex = colorHex,
        type = type.name,
        isDefault = isDefault,
        sortOrder = sortOrder
    )
}
