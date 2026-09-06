package com.velora.tracker.presentation.splash

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isReducedMotion = remember {
        try {
            val durationScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            durationScale == 0f
        } catch (e: Exception) {
            false
        }
    }

    // Animation elapsed time tracker in milliseconds (0f -> 2600f)
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        if (isReducedMotion) {
            // Accessible reduced-motion timing: short fade-in and hold
            animProgress.animateTo(
                targetValue = 2600f,
                animationSpec = tween(durationMillis = 1000)
            )
            delay(400)
            onSplashFinished()
        } else {
            // Refined deceleration easing matching cubic-bezier(0.16, 1, 0.3, 1)
            animProgress.animateTo(
                targetValue = 2600f,
                animationSpec = tween(
                    durationMillis = 2600,
                    easing = androidx.compose.animation.core.LinearEasing
                )
            )
            delay(200)
            onSplashFinished()
        }
    }

    val currentMs = animProgress.value
    val cubicEasing = remember { CubicBezierEasing(0.16f, 1.0f, 0.3f, 1.0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF003840),
                        Color(0xFF001F2A),
                        Color(0xFF001219)
                    ),
                    center = Offset(700f, 300f),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 240x240dp Centered Logo Assembly Canvas
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasScale = size.width / VeloraSplashLogoData.CANVAS_WIDTH

                    // Gradients matching official Velora artwork
                    val gradVStem = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF00F5D4),
                            Color(0xFF10B981),
                            Color(0xFF00B4D8),
                            Color(0xFF1D4ED8)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height)
                    )

                    val gradLeafUpper = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0077B6),
                            Color(0xFF00B4D8),
                            Color(0xFF10B981),
                            Color(0xFF86EFAC)
                        ),
                        start = Offset(0f, size.height),
                        end = Offset(size.width, 0f)
                    )

                    val gradLeafLower = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0077B6),
                            Color(0xFF00B4D8),
                            Color(0xFF2563EB)
                        ),
                        start = Offset(0f, size.height * 0.5f),
                        end = Offset(size.width, size.height * 0.5f)
                    )

                    scale(canvasScale, Offset.Zero) {
                        if (isReducedMotion) {
                            // Reduced Motion: Direct serene fade of full logo
                            val alpha = (currentMs / 600f).coerceIn(0f, 1f)
                            drawUnifiedLogo(gradVStem, gradLeafUpper, gradLeafLower, alpha)
                        } else {
                            // Full Intelligent Fragment Assembly
                            val fusionStartMs = 1550f
                            val fusionEndMs = 1900f
                            val fusionProgress = if (currentMs >= fusionStartMs) {
                                cubicEasing.transform(
                                    ((currentMs - fusionStartMs) / (fusionEndMs - fusionStartMs)).coerceIn(0f, 1f)
                                )
                            } else 0f

                            val fragmentAlpha = (1f - fusionProgress).coerceIn(0f, 1f)

                            // 1. Draw individual constructing fragments
                            if (fragmentAlpha > 0f) {
                                VeloraSplashLogoData.elements.forEach { elem ->
                                    if (currentMs >= elem.delayMs) {
                                        val elapsed = currentMs - elem.delayMs
                                        val rawProgress = (elapsed / elem.durationMs.toFloat()).coerceIn(0f, 1f)
                                        val progress = cubicEasing.transform(rawProgress)

                                        val curX = elem.startX + (elem.endX - elem.startX) * progress
                                        val curY = elem.startY + (elem.endY - elem.startY) * progress
                                        val curScale = elem.startScale + (elem.endScale - elem.startScale) * progress
                                        val curOpacity = (elem.startOpacity + (elem.endOpacity - elem.startOpacity) * progress) * fragmentAlpha
                                        val curRot = elem.rotation * (1f - progress)

                                        if (curOpacity > 0f) {
                                            withTransform({
                                                translate(left = curX, top = curY)
                                                rotate(degrees = curRot, pivot = Offset.Zero)
                                                scale(scaleX = curScale, scaleY = curScale, pivot = Offset.Zero)
                                            }) {
                                                val path = Path()
                                                elem.pathDrawer(path)
                                                drawPath(
                                                    path = path,
                                                    color = elem.color.copy(alpha = curOpacity)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 2. Continuous Unified Logo geometry fusion (1550ms -> 1900ms)
                            if (fusionProgress > 0f) {
                                drawUnifiedLogo(gradVStem, gradLeafUpper, gradLeafLower, fusionProgress)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Wordmark & Tagline: Subtly revealed after logo assembly (2000ms -> 2400ms)
            val wordmarkProgress = if (isReducedMotion) {
                ((currentMs - 600f) / 400f).coerceIn(0f, 1f)
            } else if (currentMs >= 2000f) {
                cubicEasing.transform(((currentMs - 2000f) / 400f).coerceIn(0f, 1f))
            } else {
                0f
            }

            val wordmarkOffsetY = (1f - wordmarkProgress) * 12.dp.value

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = wordmarkOffsetY.dp)
                    .alpha(wordmarkProgress)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Velora",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .offset(y = (-4).dp)
                            .background(Color(0xFF10B981), androidx.compose.foundation.shape.CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Intelligent Personal Finance",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

/**
 * Draws the consolidated complete silhouette of the Velora logo
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawUnifiedLogo(
    stemGradient: Brush,
    leafUpperGradient: Brush,
    leafLowerGradient: Brush,
    alpha: Float
) {
    // 1. Left V-Arm & Upper Stem
    val vStemPath = Path().apply {
        moveTo(44f, 58f)
        cubicTo(44f, 52f, 49f, 47f, 55f, 47f)
        lineTo(78f, 47f)
        cubicTo(84f, 47f, 88f, 52f, 90f, 57f)
        lineTo(132f, 156f)
        cubicTo(134f, 162f, 130f, 169f, 123f, 171f)
        lineTo(115f, 173f)
        cubicTo(102f, 176f, 89f, 166f, 84f, 154f)
        lineTo(46f, 65f)
        cubicTo(44f, 63f, 44f, 60f, 44f, 58f)
        close()
    }
    drawPath(path = vStemPath, brush = stemGradient, alpha = alpha)

    // 2. Curved Bottom Base / Apex
    val vBasePath = Path().apply {
        moveTo(84f, 154f)
        cubicTo(89f, 166f, 102f, 176f, 115f, 173f)
        lineTo(123f, 171f)
        cubicTo(131f, 168f, 138f, 172f, 141f, 180f)
        cubicTo(144f, 188f, 139f, 197f, 131f, 199f)
        lineTo(121f, 201f)
        cubicTo(98f, 206f, 75f, 191f, 68f, 168f)
        lineTo(44f, 98f)
        lineTo(68f, 154f)
        close()
    }
    drawPath(path = vBasePath, color = Color(0xFF1D4ED8), alpha = alpha)

    // 3. Main Upper Botanical Leaf
    val leafUpperPath = Path().apply {
        moveTo(132f, 156f)
        cubicTo(122f, 125f, 128f, 88f, 152f, 60f)
        cubicTo(172f, 36f, 198f, 24f, 212f, 22f)
        cubicTo(214f, 22f, 215f, 24f, 215f, 26f)
        cubicTo(212f, 48f, 198f, 82f, 174f, 108f)
        cubicTo(158f, 126f, 144f, 144f, 132f, 156f)
        close()
    }
    drawPath(path = leafUpperPath, brush = leafUpperGradient, alpha = alpha)

    // 4. Secondary Lower Botanical Leaf
    val leafLowerPath = Path().apply {
        moveTo(136f, 156f)
        cubicTo(146f, 142f, 165f, 134f, 186f, 134f)
        cubicTo(193f, 134f, 196f, 138f, 194f, 144f)
        cubicTo(188f, 162f, 172f, 178f, 150f, 188f)
        cubicTo(142f, 192f, 135f, 188f, 134f, 181f)
        cubicTo(133f, 172f, 134f, 163f, 136f, 156f)
        close()
    }
    drawPath(path = leafLowerPath, brush = leafLowerGradient, alpha = alpha)
}
