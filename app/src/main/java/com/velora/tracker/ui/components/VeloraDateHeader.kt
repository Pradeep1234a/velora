package com.velora.tracker.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VeloraDateHeader(
    dateFormatted: String = "",
    text: String = dateFormatted,
    isFirst: Boolean = false,
    modifier: Modifier = Modifier
) {
    val display = text.ifEmpty { dateFormatted }
    Text(
        text = display,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = if (isFirst) 16.dp else 24.dp,
            bottom = 4.dp
        )
    )
}
