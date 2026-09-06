package com.velora.tracker.domain.model

data class CategorySuggestion(
    val categoryId: Long,
    val categoryName: String,
    val confidence: Float, // 0.0..1.0
    val source: CategorySource
)
