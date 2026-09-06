package com.velora.tracker.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velora.tracker.ui.components.VeloraCategoryIcon

val AVAILABLE_ICONS = listOf(
    "restaurant", "shopping_cart", "shopping_bag", "directions_car", "local_gas_station",
    "receipt_long", "home", "movie", "favorite", "school", "flight", "subscriptions",
    "account_balance", "computer", "business_center", "trending_up", "redeem", "replay",
    "more_horiz", "attach_money", "star", "sports_esports", "pets", "child_care", "build",
    "brush", "fitness_center", "local_hospital", "wifi", "phone_android"
)

val AVAILABLE_COLORS = listOf(
    "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5",
    "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50",
    "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800",
    "#FF5722", "#795548", "#9E9E9E", "#607D8B", "#000000"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditDialog(
    state: CategoryEditState,
    onStateChange: (CategoryEditState) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (state.isNew) "Add Category" else "Edit Category") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onStateChange(state.copy(name = it)) },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Select Icon", style = MaterialTheme.typography.titleSmall)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.height(150.dp).padding(vertical = 8.dp)
                ) {
                    items(AVAILABLE_ICONS) { iconName ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(if (state.selectedIconName == iconName) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .clickable { onStateChange(state.copy(selectedIconName = iconName)) },
                            contentAlignment = Alignment.Center
                        ) {
                            VeloraCategoryIcon(iconName = iconName, contentDescription = iconName)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Select Color", style = MaterialTheme.typography.titleSmall)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.height(150.dp).padding(vertical = 8.dp)
                ) {
                    items(AVAILABLE_COLORS) { colorHex ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(colorHex)))
                                .clickable { onStateChange(state.copy(selectedColorHex = colorHex)) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.selectedColorHex == colorHex) {
                                // Simple indicator for selection
                                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color.White))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = state.name.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
