package com.velora.tracker.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            val isIncome = t.type == TransactionType.INCOME
            val isExpense = t.type == TransactionType.EXPENSE
            val accentColor = if (isIncome) Color(0xFF16803C) else Color(0xFFC2410C)
            val badgeBg = if (isIncome) Color(0xFFDCFCE7) else Color(0xFFFFEDD5)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Amount Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = badgeBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (isIncome) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                                    contentDescription = null,
                                    tint = accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isIncome) "Income" else "Expense",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor
                                )
                            }
                        }

                        VeloraAmountText(
                            amountMinorUnits = t.amountMinorUnits,
                            isExpense = isExpense,
                            currencySymbol = currencySymbol,
                            style = MaterialTheme.typography.displayMedium
                        )
                        
                        Text(
                            text = t.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Details Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "TRANSACTION DETAILS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        val catName = category?.name ?: t.categoryName
                        val catIcon = category?.iconName ?: t.categoryIconName
                        val catColor = category?.colorHex ?: t.categoryColorHex
                        if (catName.isNotEmpty()) {
                            DetailItem(label = "Category") {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    VeloraCategoryIcon(iconName = catIcon, colorHex = catColor, size = 28.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = catName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                        }

                        DetailItem(label = "Date & Time", value = "${t.dateTime.format(dateFormatter)} at ${t.dateTime.format(timeFormatter)}")
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)

                        DetailItem(label = "Payment Method", value = t.paymentMethod.displayName())
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)

                        if (t.notes.isNotEmpty()) {
                            DetailItem(label = "Notes", value = t.notes)
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                        }

                        val sourceText = when (t.categorySource) {
                            CategorySource.AI -> "AI suggested (${((t.aiConfidence ?: 0.9f) * 100).toInt()}% confidence)"
                            CategorySource.RULE -> "Auto-categorized (Rule-based)"
                            else -> "Manual"
                        }
                        DetailItem(label = "Categorization", value = sourceText)
                    }
                }
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
fun DetailItem(label: String, value: String) {
    DetailItem(label) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DetailItem(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        content()
    }
}
