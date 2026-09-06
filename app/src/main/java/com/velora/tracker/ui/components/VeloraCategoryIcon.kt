package com.velora.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VeloraCategoryIcon(
    iconName: String,
    colorHex: String = "#90A4AE",
    size: Dp = 40.dp,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val color = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        Color.Gray
    }

    Box(
        modifier = modifier
            .size(size)
            .background(color.copy(alpha = 0.15f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getCategoryIcon(iconName),
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}

fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "restaurant" -> Icons.Outlined.Restaurant
        "shopping_cart" -> Icons.Outlined.ShoppingCart
        "directions_car" -> Icons.Outlined.DirectionsCar
        "receipt" -> Icons.Outlined.Receipt
        "local_mall" -> Icons.Outlined.LocalMall
        "movie" -> Icons.Outlined.Movie
        "favorite" -> Icons.Outlined.Favorite
        "school" -> Icons.Outlined.School
        "flight" -> Icons.Outlined.Flight
        "subscriptions" -> Icons.Outlined.Subscriptions
        "local_gas_station" -> Icons.Outlined.LocalGasStation
        "home" -> Icons.Outlined.Home
        "attach_money" -> Icons.Outlined.AttachMoney
        "work" -> Icons.Outlined.Work
        "business_center" -> Icons.Outlined.BusinessCenter
        "trending_up" -> Icons.Outlined.TrendingUp
        "card_giftcard" -> Icons.Outlined.CardGiftcard
        "replay" -> Icons.Outlined.Replay
        else -> Icons.Outlined.MoreHoriz
    }
}
