package com.velora.tracker.data.ai

import com.velora.tracker.domain.model.Category
import com.velora.tracker.domain.model.CategorySource
import com.velora.tracker.domain.model.CategorySuggestion

class RuleBasedCategorizationService(
    private val userPreferenceLearningStore: UserPreferenceLearningStore
) : CategorizationService {
    
    override suspend fun categorize(
        title: String,
        notes: String?,
        categories: List<Category>
    ): CategorySuggestion? {
        val learnedCatId = userPreferenceLearningStore.getPreference(title)
        if (learnedCatId != null) {
            val cat = categories.find { it.id == learnedCatId }
            if (cat != null) {
                return CategorySuggestion(
                    categoryId = cat.id,
                    categoryName = cat.name,
                    confidence = 0.95f,
                    source = CategorySource.USER
                )
            }
        }

        val text = "$title ${notes ?: ""}".lowercase()

        val keywordMap = mapOf(
            "Food" to listOf("swiggy", "zomato", "restaurant", "lunch", "dinner", "breakfast", "cafe", "food court"),
            "Groceries" to listOf("grocery", "supermarket", "bigbasket", "blinkit", "dmart"),
            "Shopping" to listOf("amazon", "flipkart", "myntra", "shopping", "mall", "store"),
            "Transport" to listOf("uber", "ola", "lyft", "taxi", "cab", "metro", "bus", "train", "transport"),
            "Fuel" to listOf("petrol", "diesel", "fuel", "gas station", "hp", "iocl"),
            "Bills" to listOf("electricity", "water bill", "gas bill", "phone bill", "internet", "wifi", "broadband", "bill"),
            "Rent" to listOf("rent", "lease", "housing"),
            "Entertainment" to listOf("movie", "cinema", "netflix", "hotstar", "prime video", "theater", "concert", "game", "spotify"),
            "Health" to listOf("doctor", "hospital", "pharmacy", "medicine", "health", "gym", "fitness", "apollo"),
            "Education" to listOf("course", "tuition", "book", "education", "school", "college", "udemy", "coursera"),
            "Travel" to listOf("flight", "hotel", "travel", "trip", "airbnb", "booking", "makemytrip"),
            "Subscriptions" to listOf("subscription", "membership", "premium", "plan"),
            "Salary" to listOf("salary", "payroll", "wages"),
            "Freelance" to listOf("freelance", "consulting", "contract", "gig"),
            "Business" to listOf("business", "client", "revenue"),
            "Investment" to listOf("dividend", "interest", "investment", "stocks", "mutual fund", "returns"),
            "Gift" to listOf("gift", "present", "birthday"),
            "Refund" to listOf("refund", "return", "cashback", "reimbursement")
        )

        for ((catName, keywords) in keywordMap) {
            if (keywords.any { text.contains(it) }) {
                val cat = categories.find { it.name.equals(catName, ignoreCase = true) }
                if (cat != null) {
                    return CategorySuggestion(
                        categoryId = cat.id,
                        categoryName = cat.name,
                        confidence = 0.85f,
                        source = CategorySource.RULE
                    )
                }
            }
        }

        return null
    }
}
