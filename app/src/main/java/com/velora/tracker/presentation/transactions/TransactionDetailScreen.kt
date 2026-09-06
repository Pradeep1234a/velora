package com.velora.tracker.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.CategorySource
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.*
import com.velora.tracker.ui.theme.Spacing
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel,
    transactionId: Long,
    onNavigateBack: () -> Unit,
    onEditTransaction: (Long) -> Unit
) {
    val transaction by viewModel.transaction.collectAsState()
    val category by viewModel.category.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val deleteResult by viewModel.deleteResult.collectAsState(initial = false)

    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMMM d, yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a") }

    LaunchedEffect(transactionId) {
        viewModel.loadTransaction(transactionId)
    }

    LaunchedEffect(deleteResult) {
        if (deleteResult) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            VeloraTopBar(
                title = "Transaction Details",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { onEditTransaction(transactionId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = viewModel::showDeleteConfirmation) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                VeloraLoadingState()
            }
        } else if (transaction != null) {
            val t = transaction!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VeloraAmountText(
                    amountMinorUnits = t.amountMinorUnits,
                    isExpense = t.type == TransactionType.EXPENSE,
                    currencySymbol = currencySymbol,
                    style = MaterialTheme.typography.displaySmall
                )
                
                Spacer(modifier = Modifier.height(Spacing.small))
                
                AssistChip(
                    onClick = { },
                    label = { Text(if (t.type == TransactionType.INCOME) "Income" else "Expense") }
                )
                
                Spacer(modifier = Modifier.height(Spacing.medium))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(Spacing.medium))
                
                DetailRow("Title / Merchant", t.title)
                
                val catName = category?.name ?: t.categoryName
                val catIcon = category?.iconName ?: t.categoryIconName
                val catColor = category?.colorHex ?: t.categoryColorHex
                if (catName.isNotEmpty()) {
                    DetailRow("Category") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            VeloraCategoryIcon(iconName = catIcon, colorHex = catColor, size = 32.dp)
                            Spacer(modifier = Modifier.width(Spacing.small))
                            Text(catName, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                
                DetailRow("Date", t.dateTime.format(dateFormatter))
                DetailRow("Time", t.dateTime.format(timeFormatter))
                DetailRow("Payment Method", t.paymentMethod.displayName())
                
                if (t.notes.isNotEmpty()) {
                    DetailRow("Notes", t.notes)
                }
                
                val sourceText = when(t.categorySource) {
                    CategorySource.AI -> "AI suggested (${((t.aiConfidence ?: 0.9f) * 100).toInt()}% confidence)"
                    CategorySource.RULE -> "Auto-categorized (Rule-based)"
                    else -> "Manual"
                }
                DetailRow("Category Source", sourceText)
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = viewModel::dismissDeleteConfirmation,
                title = { Text("Delete Transaction?") },
                text = { Text("This action cannot be undone. This will permanently delete this transaction.") },
                confirmButton = {
                    TextButton(onClick = viewModel::deleteTransaction) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::dismissDeleteConfirmation) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    DetailRow(label) {
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun DetailRow(label: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.small)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}
