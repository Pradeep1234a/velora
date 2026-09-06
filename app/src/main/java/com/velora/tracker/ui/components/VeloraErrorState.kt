package com.velora.tracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VeloraErrorState(
    message: String = "",
    error: String = message,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val displayMessage = error.ifEmpty { message.ifEmpty { "An unexpected error occurred." } }
    VeloraEmptyState(
        icon = Icons.Outlined.ErrorOutline,
        title = "Something went wrong",
        message = message,
        actionLabel = if (onRetry != null) "Retry" else null,
        onAction = onRetry,
        modifier = modifier
    )
}
