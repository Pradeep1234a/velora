package com.velora.tracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VeloraNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val items = listOf(
            Triple("dashboard", "Dashboard", Icons.Filled.Dashboard to Icons.Outlined.Dashboard),
            Triple("transactions", "Transactions", Icons.Filled.ReceiptLong to Icons.Outlined.ReceiptLong),
            Triple("analytics", "Analytics", Icons.Filled.Analytics to Icons.Outlined.Analytics),
            Triple("categories", "Categories", Icons.Filled.Category to Icons.Outlined.Category)
        )

        items.forEach { (route, label, icons) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label
                    )
                },
                label = { Text(label) }
            )
        }
    }
}
