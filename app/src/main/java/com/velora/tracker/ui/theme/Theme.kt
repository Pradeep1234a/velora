package com.velora.tracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val VeloraLightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    scrim = scrimLight,
    surfaceTint = surfaceTintLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight
)

private val VeloraDarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    scrim = scrimDark,
    surfaceTint = surfaceTintDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark
)

data class VeloraColors(
    val income: Color,
    val incomeContainer: Color,
    val incomeCard: Color,
    val incomeLabel: Color,
    val expense: Color,
    val expenseContainer: Color,
    val expenseCard: Color,
    val expenseLabel: Color,
    val isDark: Boolean
)

val LocalVeloraColors = staticCompositionLocalOf { 
    VeloraColors(
        income = VeloraFinancialColors.incomeLightColor,
        incomeContainer = VeloraFinancialColors.incomeContainerLight,
        incomeCard = VeloraFinancialColors.incomeCardLight,
        incomeLabel = VeloraFinancialColors.incomeLabelLight,
        expense = VeloraFinancialColors.expenseLightColor,
        expenseContainer = VeloraFinancialColors.expenseContainerLight,
        expenseCard = VeloraFinancialColors.expenseCardLight,
        expenseLabel = VeloraFinancialColors.expenseLabelLight,
        isDark = false
    )
}

val MaterialTheme.spacing: VeloraSpacing
    @Composable @ReadOnlyComposable
    get() = LocalVeloraSpacing.current

val MaterialTheme.veloraColors: VeloraColors
    @Composable @ReadOnlyComposable
    get() = LocalVeloraColors.current

@Composable
fun VeloraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> VeloraDarkColorScheme
        else -> VeloraLightColorScheme
    }
    
    val veloraColors = if (darkTheme) {
        VeloraColors(
            income = VeloraFinancialColors.incomeDarkColor,
            incomeContainer = VeloraFinancialColors.incomeContainerDark,
            incomeCard = VeloraFinancialColors.incomeCardDark,
            incomeLabel = VeloraFinancialColors.incomeLabelDark,
            expense = VeloraFinancialColors.expenseDarkColor,
            expenseContainer = VeloraFinancialColors.expenseContainerDark,
            expenseCard = VeloraFinancialColors.expenseCardDark,
            expenseLabel = VeloraFinancialColors.expenseLabelDark,
            isDark = true
        )
    } else {
        VeloraColors(
            income = VeloraFinancialColors.incomeLightColor,
            incomeContainer = VeloraFinancialColors.incomeContainerLight,
            incomeCard = VeloraFinancialColors.incomeCardLight,
            incomeLabel = VeloraFinancialColors.incomeLabelLight,
            expense = VeloraFinancialColors.expenseLightColor,
            expenseContainer = VeloraFinancialColors.expenseContainerLight,
            expenseCard = VeloraFinancialColors.expenseCardLight,
            expenseLabel = VeloraFinancialColors.expenseLabelLight,
            isDark = false
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalVeloraSpacing provides VeloraSpacing(),
        LocalVeloraColors provides veloraColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = VeloraTypography,
            content = content
        )
    }
}
