package com.velora.tracker.data.ai

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.CategorySuggestion

interface CategorizationService {
    suspend fun categorize(title: String, notes: String?, categories: List<Category>): CategorySuggestion?
}
