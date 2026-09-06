package com.velora.tracker.domain.usecase

import com.velora.tracker.data.ai.CategorizationService
import com.velora.tracker.domain.model.CategorySuggestion
import com.velora.tracker.domain.repository.CategoryRepository
import com.velora.tracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class CategorizeTransactionUseCase(
    private val categorizationService: CategorizationService,
    private val settingsRepository: SettingsRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(title: String, notes: String?): CategorySuggestion? {
        val isEnabled = settingsRepository.isAiCategorizationEnabled().first()
        if (!isEnabled) return null

        val categories = categoryRepository.getAllCategories().first()
        return categorizationService.categorize(title, notes, categories)
    }
}
