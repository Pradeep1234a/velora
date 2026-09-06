package com.velora.tracker.ui.theme

import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF005C55)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF0F766E)
val onPrimaryContainerLight = Color(0xFFA3FAEF)
val secondaryLight = Color(0xFF006B5F)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFF6DF5E1)
val onSecondaryContainerLight = Color(0xFF006F64)
val tertiaryLight = Color(0xFF005A6A)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFF007488)
val onTertiaryContainerLight = Color(0xFFC6F2FF)
val surfaceLight = Color(0xFFF8F9FF)
val onSurfaceLight = Color(0xFF0B1C30)
val surfaceVariantLight = Color(0xFFD3E4FE)
val onSurfaceVariantLight = Color(0xFF3E4947)
val outlineLight = Color(0xFF6E7977)
val outlineVariantLight = Color(0xFFBDC9C6)
val backgroundLight = Color(0xFFF8F9FF)
val onBackgroundLight = Color(0xFF0B1C30)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val inverseSurfaceLight = Color(0xFF213145)
val inverseOnSurfaceLight = Color(0xFFEAF1FF)
val inversePrimaryLight = Color(0xFF80D5CB)
val scrimLight = Color(0xFF000000)
val surfaceTintLight = Color(0xFF006A63)

val primaryDark = Color(0xFF80D5CB)
val onPrimaryDark = Color(0xFF003732)
val primaryContainerDark = Color(0xFF00504A)
val onPrimaryContainerDark = Color(0xFF9CF2E8)
val secondaryDark = Color(0xFF4FDBC8)
val onSecondaryDark = Color(0xFF003731)
val secondaryContainerDark = Color(0xFF005048)
val onSecondaryContainerDark = Color(0xFF71F8E4)
val tertiaryDark = Color(0xFF4CD7F6)
val onTertiaryDark = Color(0xFF00353E)
val tertiaryContainerDark = Color(0xFF004E5C)
val onTertiaryContainerDark = Color(0xFFACEDFF)
val surfaceDark = Color(0xFF0E1720)
val onSurfaceDark = Color(0xFFE2EAF4)
val surfaceVariantDark = Color(0xFF253342)
val onSurfaceVariantDark = Color(0xFFB0BCC8)
val outlineDark = Color(0xFF7B8B9B)
val outlineVariantDark = Color(0xFF2D3C4C)
val backgroundDark = Color(0xFF0E1720)
val onBackgroundDark = Color(0xFFE2EAF4)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val inverseSurfaceDark = Color(0xFFE2EAF4)
val inverseOnSurfaceDark = Color(0xFF213145)
val inversePrimaryDark = Color(0xFF005C55)
val scrimDark = Color(0xFF000000)
val surfaceTintDark = Color(0xFF80D5CB)

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
