package com.velora.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun VeloraChartCard(
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row { actions() }
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun VeloraBarChart(
    data: List<Pair<String, Float>>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (data.isEmpty()) return@Canvas
        
        val width = size.width
        val height = size.height
        val barSpacing = 16.dp.toPx()
        val textHeight = 24.dp.toPx()
        val graphHeight = height - textHeight
        
        val totalSpacing = barSpacing * (data.size - 1)
        val barWidth = (width - totalSpacing) / data.size
        
        data.forEachIndexed { index, (label, value) ->
            val x = index * (barWidth + barSpacing)
            val barHeight = value * graphHeight
            val y = graphHeight - barHeight
            
            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            
            val textLayoutResult = textMeasurer.measure(label, textStyle)
            val textX = x + (barWidth - textLayoutResult.size.width) / 2
            val textY = graphHeight + 4.dp.toPx()
            
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(textX, textY)
            )
        }
    }
}

@Composable
fun VeloraCategoryProgressBar(
    categoryName: String,
    categoryIconName: String,
    categoryColorHex: String,
    amountMinorUnits: Long,
    percentage: Float,
    currencySymbol: String = "₹",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VeloraCategoryIcon(
            iconName = categoryIconName,
            colorHex = categoryColorHex,
            size = 32.dp
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            val color = try { Color(android.graphics.Color.parseColor(categoryColorHex)) } catch (e: Exception) { MaterialTheme.colorScheme.primary }
            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = MoneyFormatter.format(amountMinorUnits, currencySymbol, showSign = false),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${percentage.toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
