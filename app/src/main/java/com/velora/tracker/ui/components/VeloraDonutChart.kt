package com.velora.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.CategoryBreakdown

@Composable
fun VeloraDonutChart(
    breakdowns: List<CategoryBreakdown>,
    totalSpentMinorUnits: Long,
    currencySymbol: String = "₹",
    subtext: String = "4 Oct Weeks",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(190.dp)) {
            val strokeWidth = 32.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val radius = diameter / 2f
            val centerOffset = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)

            if (breakdowns.isEmpty() || totalSpentMinorUnits == 0L) {
                drawCircle(
                    color = Color(0xFFE5EEFF),
                    radius = radius,
                    center = centerOffset,
                    style = Stroke(width = strokeWidth)
                )
            } else {
                var startAngle = -90f
                val defaultPalette = listOf(
                    Color(0xFF005C55), // Deep Teal
                    Color(0xFF007488), // Cyan/Blue
                    Color(0xFF4CD7F6), // Sky Cyan
                    Color(0xFF71F8E4), // Mint Green
                    Color(0xFFFFA726), // Amber
                    Color(0xFFAB47BC)  // Violet
                )

                breakdowns.forEachIndexed { index, item ->
                    val sweepAngle = (item.percentage / 100f) * 360f
                    val sliceColor = try {
                        Color(android.graphics.Color.parseColor(item.categoryColorHex))
                    } catch (e: Exception) {
                        defaultPalette[index % defaultPalette.size]
                    }

                    drawArc(
                        color = sliceColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                        topLeft = androidx.compose.ui.geometry.Offset(centerOffset.x - radius, centerOffset.y - radius),
                        size = androidx.compose.ui.geometry.Size(diameter, diameter)
                    )
                    startAngle += sweepAngle
                }
            }
        }

        // Center Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TOTAL SPENT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = MoneyFormatter.format(totalSpentMinorUnits, currencySymbol, showSign = false),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
