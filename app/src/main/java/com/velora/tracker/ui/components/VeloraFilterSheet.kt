package com.velora.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeloraFilterSheet(
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    selectedType: String? = null,
    onTypeSelected: (String?) -> Unit = {},
    selectedCategoryIds: Set<Long> = emptySet(),
    onCategorySelected: (Set<Long>) -> Unit = {},
    categories: List<Category> = emptyList(),
    sortOrder: String = "NEWEST",
    onSortOrderChanged: (String) -> Unit = {}
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
            ) {
                Text("Type", style = MaterialTheme.typography.titleMedium)
                Row {
                    FilterChip(selected = selectedType == null, onClick = { onTypeSelected(null) }, label = { Text("All") })
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(selected = selectedType == "INCOME", onClick = { onTypeSelected("INCOME") }, label = { Text("Income") })
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(selected = selectedType == "EXPENSE", onClick = { onTypeSelected("EXPENSE") }, label = { Text("Expense") })
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Categories", style = MaterialTheme.typography.titleMedium)
                // Filter chips for categories...
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Sort By", style = MaterialTheme.typography.titleMedium)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sortOrder == "NEWEST", onClick = { onSortOrderChanged("NEWEST") })
                        Text("Newest first")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sortOrder == "OLDEST", onClick = { onSortOrderChanged("OLDEST") })
                        Text("Oldest first")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sortOrder == "HIGHEST", onClick = { onSortOrderChanged("HIGHEST") })
                        Text("Highest amount")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = sortOrder == "LOWEST", onClick = { onSortOrderChanged("LOWEST") })
                        Text("Lowest amount")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) {
                        Text("Reset")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onApply, modifier = Modifier.weight(1f)) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
