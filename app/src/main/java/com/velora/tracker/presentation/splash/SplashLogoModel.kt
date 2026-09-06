package com.velora.tracker.presentation.splash

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

enum class LogoElementType {
    V_STEM,
    LEAF_MAIN,
    LEAF_SUB
}

/**
 * Stitch Implementation Intent:
 * Every logo fragment has:
 *  - startPosition & endPosition
 *  - startOpacity & endOpacity
 *  - startScale & endScale
 *  - rotation
 *  - delayMs & durationMs
 *  - individual color
 *  - path drawing logic
 */
data class SplashLogoElement(
    val id: String,
    val type: LogoElementType,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val startScale: Float,
    val endScale: Float = 1.0f,
    val startOpacity: Float = 0.0f,
    val endOpacity: Float = 1.0f,
    val rotation: Float,
    val delayMs: Int,
    val durationMs: Int,
    val color: Color,
    val pathDrawer: (Path) -> Unit
)

object VeloraSplashLogoData {
    // Canvas standard dimensions: 240 x 240
    const val CANVAS_WIDTH = 240f
    const val CANVAS_HEIGHT = 240f

    val elements = listOf(
        // ==========================================
        // 1. V-STEM STRUCTURE (Forms 0ms -> 1200ms)
        // ==========================================
        SplashLogoElement(
            id = "v_cap_top",
            type = LogoElementType.V_STEM,
            startX = 40f, startY = 20f,
            endX = 62f, endY = 52f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -18f,
            delayMs = 50, durationMs = 800,
            color = Color(0xFF00F5D4),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-14f, -6f)
                path.cubicTo(-14f, -10f, -9f, -14f, -3f, -14f)
                path.lineTo(14f, -14f)
                path.cubicTo(18f, -14f, 20f, -10f, 20f, -6f)
                path.lineTo(14f, 8f)
                path.lineTo(-12f, 8f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_upper_outer",
            type = LogoElementType.V_STEM,
            startX = 15f, startY = 70f,
            endX = 52f, endY = 75f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 12f,
            delayMs = 120, durationMs = 850,
            color = Color(0xFF10B981),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-10f, -15f)
                path.lineTo(12f, -10f)
                path.lineTo(4f, 16f)
                path.lineTo(-10f, 12f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_upper_inner",
            type = LogoElementType.V_STEM,
            startX = 85f, startY = 35f,
            endX = 78f, endY = 72f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -8f,
            delayMs = 160, durationMs = 800,
            color = Color(0xFF06D6A0),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-10f, -12f)
                path.lineTo(12f, -12f)
                path.lineTo(10f, 14f)
                path.lineTo(-8f, 10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_mid_outer",
            type = LogoElementType.V_STEM,
            startX = 25f, startY = 130f,
            endX = 68f, endY = 108f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 10f,
            delayMs = 220, durationMs = 850,
            color = Color(0xFF00B4D8),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-12f, -16f)
                path.lineTo(14f, -10f)
                path.lineTo(6f, 18f)
                path.lineTo(-12f, 12f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_mid_inner",
            type = LogoElementType.V_STEM,
            startX = 95f, startY = 85f,
            endX = 92f, endY = 105f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -6f,
            delayMs = 260, durationMs = 820,
            color = Color(0xFF0096C7),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-10f, -14f)
                path.lineTo(14f, -10f)
                path.lineTo(8f, 16f)
                path.lineTo(-8f, 12f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_lower_outer",
            type = LogoElementType.V_STEM,
            startX = 45f, startY = 180f,
            endX = 86f, endY = 142f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 8f,
            delayMs = 320, durationMs = 850,
            color = Color(0xFF0284C7),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-12f, -15f)
                path.lineTo(14f, -8f)
                path.lineTo(6f, 18f)
                path.lineTo(-10f, 14f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_stem_lower_inner",
            type = LogoElementType.V_STEM,
            startX = 110f, startY = 130f,
            endX = 108f, endY = 138f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -5f,
            delayMs = 360, durationMs = 820,
            color = Color(0xFF0284C7),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-8f, -14f)
                path.lineTo(12f, -8f)
                path.lineTo(8f, 16f)
                path.lineTo(-8f, 12f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_vertex_base",
            type = LogoElementType.V_STEM,
            startX = 90f, startY = 220f,
            endX = 105f, endY = 184f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -10f,
            delayMs = 420, durationMs = 860,
            color = Color(0xFF1D4ED8),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-25f, -12f)
                path.cubicTo(-10f, 14f, 15f, 16f, 28f, -2f)
                path.lineTo(20f, -12f)
                path.cubicTo(10f, -2f, -6f, 2f, -18f, -10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "v_vertex_junction",
            type = LogoElementType.V_STEM,
            startX = 140f, startY = 195f,
            endX = 130f, endY = 160f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 15f,
            delayMs = 480, durationMs = 820,
            color = Color(0xFF1E3A8A),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-8f, -10f)
                path.cubicTo(6f, -4f, 14f, 8f, 10f, 18f)
                path.lineTo(-4f, 14f)
                path.cubicTo(-2f, 6f, -4f, 0f, -10f, -4f)
                path.close()
            }
        ),

        // ==========================================
        // 2. MAIN BOTANICAL LEAF (Forms 600ms -> 1600ms)
        // ==========================================
        SplashLogoElement(
            id = "leaf_root_seed",
            type = LogoElementType.LEAF_MAIN,
            startX = 125f, startY = 175f,
            endX = 134f, endY = 146f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -12f,
            delayMs = 620, durationMs = 750,
            color = Color(0xFF0077B6),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-6f, 8f)
                path.cubicTo(-4f, 0f, 2f, -6f, 8f, -8f)
                path.lineTo(10f, -2f)
                path.cubicTo(4f, 2f, 0f, 6f, -2f, 10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_lower_inner_fold",
            type = LogoElementType.LEAF_MAIN,
            startX = 110f, startY = 120f,
            endX = 133f, endY = 122f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -8f,
            delayMs = 680, durationMs = 780,
            color = Color(0xFF0096C7),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-5f, 14f)
                path.cubicTo(-6f, 2f, 0f, -10f, 8f, -16f)
                path.lineTo(14f, -10f)
                path.cubicTo(8f, -2f, 4f, 6f, 4f, 16f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_mid_inner_spine",
            type = LogoElementType.LEAF_MAIN,
            startX = 120f, startY = 80f,
            endX = 142f, endY = 94f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -6f,
            delayMs = 740, durationMs = 800,
            color = Color(0xFF00B4D8),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-8f, 16f)
                path.cubicTo(-6f, 2f, 4f, -12f, 14f, -18f)
                path.lineTo(18f, -10f)
                path.cubicTo(10f, -2f, 4f, 8f, 0f, 20f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_upper_spine",
            type = LogoElementType.LEAF_MAIN,
            startX = 145f, startY = 50f,
            endX = 164f, endY = 68f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -4f,
            delayMs = 800, durationMs = 820,
            color = Color(0xFF10B981),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-10f, 16f)
                path.cubicTo(-6f, 0f, 8f, -12f, 18f, -18f)
                path.lineTo(22f, -10f)
                path.cubicTo(12f, -2f, 4f, 8f, 0f, 18f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_apex_tip",
            type = LogoElementType.LEAF_MAIN,
            startX = 235f, startY = 10f,
            endX = 204f, endY = 30f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 20f,
            delayMs = 880, durationMs = 850,
            color = Color(0xFF86EFAC),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-18f, 14f)
                path.cubicTo(-8f, 6f, 6f, -4f, 14f, -12f)
                path.cubicTo(14f, -10f, 10f, 2f, 2f, 12f)
                path.cubicTo(-4f, 16f, -12f, 16f, -18f, 14f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_upper_outer_blade",
            type = LogoElementType.LEAF_MAIN,
            startX = 230f, startY = 60f,
            endX = 196f, endY = 62f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 15f,
            delayMs = 920, durationMs = 850,
            color = Color(0xFF4ADE80),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-12f, -16f)
                path.cubicTo(6f, -6f, 18f, 6f, 18f, 18f)
                path.lineTo(10f, 16f)
                path.cubicTo(8f, 6f, 0f, -4f, -14f, -10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_mid_outer_blade",
            type = LogoElementType.LEAF_MAIN,
            startX = 215f, startY = 105f,
            endX = 182f, endY = 96f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 12f,
            delayMs = 960, durationMs = 840,
            color = Color(0xFF22C55E),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-10f, -16f)
                path.cubicTo(8f, -4f, 16f, 8f, 14f, 20f)
                path.lineTo(6f, 18f)
                path.cubicTo(6f, 6f, 0f, -4f, -12f, -10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "leaf_lower_outer_blade",
            type = LogoElementType.LEAF_MAIN,
            startX = 190f, startY = 140f,
            endX = 158f, endY = 128f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 10f,
            delayMs = 1020, durationMs = 820,
            color = Color(0xFF10B981),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-8f, -16f)
                path.cubicTo(6f, -4f, 14f, 8f, 10f, 20f)
                path.lineTo(2f, 16f)
                path.cubicTo(4f, 6f, 0f, -4f, -10f, -10f)
                path.close()
            }
        ),

        // ==========================================
        // 3. SECONDARY LOWER LEAF (Forms 980ms -> 1750ms)
        // ==========================================
        SplashLogoElement(
            id = "subleaf_root",
            type = LogoElementType.LEAF_SUB,
            startX = 130f, startY = 145f,
            endX = 144f, endY = 152f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -10f,
            delayMs = 980, durationMs = 750,
            color = Color(0xFF0077B6),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-8f, -2f)
                path.cubicTo(0f, -6f, 8f, -6f, 14f, -2f)
                path.lineTo(12f, 6f)
                path.cubicTo(8f, 2f, 2f, 2f, -4f, 4f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "subleaf_upper_curve",
            type = LogoElementType.LEAF_SUB,
            startX = 170f, startY = 120f,
            endX = 168f, endY = 140f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -6f,
            delayMs = 1040, durationMs = 780,
            color = Color(0xFF00B4D8),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-14f, -4f)
                path.cubicTo(-4f, -8f, 8f, -6f, 18f, 2f)
                path.lineTo(14f, 8f)
                path.cubicTo(6f, 2f, -2f, 0f, -12f, 4f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "subleaf_petal_tip",
            type = LogoElementType.LEAF_SUB,
            startX = 215f, startY = 135f,
            endX = 188f, endY = 144f,
            startScale = 0.2f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 15f,
            delayMs = 1100, durationMs = 800,
            color = Color(0xFF38BDF8),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(-12f, -8f)
                path.cubicTo(-2f, -10f, 8f, -4f, 8f, 4f)
                path.cubicTo(6f, 10f, -2f, 12f, -10f, 10f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "subleaf_underbelly",
            type = LogoElementType.LEAF_SUB,
            startX = 185f, startY = 185f,
            endX = 168f, endY = 168f,
            startScale = 0.3f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = 12f,
            delayMs = 1150, durationMs = 820,
            color = Color(0xFF0284C7),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(14f, -10f)
                path.cubicTo(6f, 4f, -4f, 14f, -16f, 18f)
                path.lineTo(-14f, 10f)
                path.cubicTo(-4f, 6f, 4f, 0f, 10f, -8f)
                path.close()
            }
        ),
        SplashLogoElement(
            id = "subleaf_base_closure",
            type = LogoElementType.LEAF_SUB,
            startX = 140f, startY = 188f,
            endX = 142f, endY = 176f,
            startScale = 0.25f, endScale = 1.0f,
            startOpacity = 0.0f, endOpacity = 1.0f,
            rotation = -8f,
            delayMs = 1200, durationMs = 800,
            color = Color(0xFF2563EB),
            pathDrawer = { path ->
                path.reset()
                path.moveTo(10f, -12f)
                path.cubicTo(4f, -2f, -4f, 6f, -12f, 8f)
                path.lineTo(-10f, 0f)
                path.cubicTo(-4f, -2f, 2f, -6f, 6f, -10f)
                path.close()
            }
        )
    )
}
