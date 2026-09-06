package com.velora.tracker.presentation.categories

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCategoryDialog(
    state: DeleteCategoryState,
    onSelectMoveToCategory: (Long) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete ${state.categoryName}?") },
        text = {
            Column {
                if (state.transactionCount > 0) {
                    Text("This category has ${state.transactionCount} transactions. Choose another category to move them to.")
                    Spacer(modifier = Modifier.height(16.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = state.availableCategories.find { it.id == state.selectedMoveToCategoryId }?.name ?: "Select category",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.availableCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        onSelectMoveToCategory(category.id)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text("Are you sure you want to delete this category?")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = state.transactionCount == 0 || state.selectedMoveToCategoryId != null,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
