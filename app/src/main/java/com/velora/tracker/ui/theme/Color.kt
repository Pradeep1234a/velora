package com.velora.tracker.ui.theme

import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF1B6B52)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFA8F5D4)
val onPrimaryContainerLight = Color(0xFF002117)
val secondaryLight = Color(0xFF4B635A)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFCDE9DC)
val onSecondaryContainerLight = Color(0xFF072018)
val tertiaryLight = Color(0xFF406376)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFC3E8FE)
val onTertiaryContainerLight = Color(0xFF001E2D)
val surfaceLight = Color(0xFFF9FAF7)
val onSurfaceLight = Color(0xFF191C1A)
val surfaceVariantLight = Color(0xFFDBE5DD)
val onSurfaceVariantLight = Color(0xFF404943)
val outlineLight = Color(0xFF707973)
val outlineVariantLight = Color(0xFFBFC9C1)
val backgroundLight = Color(0xFFF9FAF7)
val onBackgroundLight = Color(0xFF191C1A)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val inverseSurfaceLight = Color(0xFF2E312F)
val inverseOnSurfaceLight = Color(0xFFF0F1EE)
val inversePrimaryLight = Color(0xFF8DD8B8)
val scrimLight = Color(0xFF000000)
val surfaceTintLight = Color(0xFF1B6B52)

val primaryDark = Color(0xFF8DD8B8)
val onPrimaryDark = Color(0xFF00382A)
val primaryContainerDark = Color(0xFF00513D)
val onPrimaryContainerDark = Color(0xFFA8F5D4)
val secondaryDark = Color(0xFFB1CDC1)
val onSecondaryDark = Color(0xFF1D352C)
val secondaryContainerDark = Color(0xFF344C42)
val onSecondaryContainerDark = Color(0xFFCDE9DC)
val tertiaryDark = Color(0xFFA7CCE2)
val onTertiaryDark = Color(0xFF0B3446)
val tertiaryContainerDark = Color(0xFF264B5E)
val onTertiaryContainerDark = Color(0xFFC3E8FE)
val surfaceDark = Color(0xFF111412)
val onSurfaceDark = Color(0xFFE1E3DF)
val surfaceVariantDark = Color(0xFF404943)
val onSurfaceVariantDark = Color(0xFFBFC9C1)
val outlineDark = Color(0xFF8A938C)
val outlineVariantDark = Color(0xFF404943)
val backgroundDark = Color(0xFF111412)
val onBackgroundDark = Color(0xFFE1E3DF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val inverseSurfaceDark = Color(0xFFE1E3DF)
val inverseOnSurfaceDark = Color(0xFF2E312F)
val inversePrimaryDark = Color(0xFF1B6B52)
val scrimDark = Color(0xFF000000)
val surfaceTintDark = Color(0xFF8DD8B8)

object VeloraFinancialColors {
    val incomeLightColor = Color(0xFF2E7D32)
    val incomeContainerLight = Color(0xFFE8F5E9)
    val expenseLightColor = Color(0xFFC62828)
    val expenseContainerLight = Color(0xFFFFEBEE)
    
    val incomeDarkColor = Color(0xFF81C784)
    val incomeContainerDark = Color(0xFF1B3A1E)
    val expenseDarkColor = Color(0xFFEF9A9A)
    val expenseContainerDark = Color(0xFF3E1C1C)
}

object VeloraCategoryColors {
    val colors = listOf(
        Color(0xFFE57373), // Food - Coral
        Color(0xFFFFB74D), // Groceries - Amber
        Color(0xFF4DB6AC), // Transport - Teal
        Color(0xFF7986CB), // Bills - Indigo
        Color(0xFFF06292), // Shopping - Pink
        Color(0xFF4DD0E1), // Entertainment - Cyan
        Color(0xFF81C784), // Health - Green
        Color(0xFF9575CD), // Education - DeepPurple
        Color(0xFFFF8A65), // Travel - Orange
        Color(0xFF64B5F6), // Subscriptions - Blue
        Color(0xFFAED581), // Fuel - LimeGreen
        Color(0xFFA1887F), // Rent - Brown
        Color(0xFF90A4AE), // Other expense - BlueGrey
        Color(0xFF66BB6A), // Salary - Emerald
        Color(0xFF42A5F5), // Freelance - SkyBlue
        Color(0xFFFFC107), // Business - Gold
        Color(0xFFAB47BC), // Investment - Violet
        Color(0xFFEC407A), // Gift - Rose
        Color(0xFF26A69A), // Refund - LightTeal
        Color(0xFFBDBDBD), // Other income - Grey
    )
    
    fun fromHex(hex: String): Color = Color(android.graphics.Color.parseColor(hex))
}
