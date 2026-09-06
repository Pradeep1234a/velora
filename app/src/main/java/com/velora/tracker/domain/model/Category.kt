package com.velora.tracker.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: TransactionType,
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
)
