package com.velora.tracker.presentation.addtransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velora.tracker.domain.model.PaymentMethod
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.presentation.dashboard.getCategoryEmoji
import com.velora.tracker.ui.components.VeloraFormField
import com.velora.tracker.ui.components.VeloraNumericKeypad
import com.velora.tracker.ui.components.VeloraTopBar
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onNavigateBack: () -> Unit,
    transactionId: Long? = null
) {
    val transactionType by viewModel.transactionType.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val title by viewModel.title.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val categorySuggestion by viewModel.categorySuggestion.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(transactionId) {
        if (transactionId != null) {
            viewModel.loadTransaction(transactionId)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.saveResult.collect { result ->
            when (result) {
                is AddTransactionViewModel.SaveResult.Success -> {
                    onNavigateBack()
                }
                is AddTransactionViewModel.SaveResult.Error -> {
                    snackbarHostState.showSnackbar(result.message)
                }
            }
        }
    }

    val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

    var showCategorySheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            VeloraTopBar(
                title = if (transactionId == null) "Add Transaction" else "Edit Transaction",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Segmented Expense vs Income Toggle from Stitch
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val isExpense = transactionType == TransactionType.EXPENSE
                    // Expense Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isExpense) Color(0xFFBA1A1A) else Color.Transparent)
                            .clickable { viewModel.setTransactionType(TransactionType.EXPENSE) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDownward,
                                contentDescription = null,
                                tint = if (isExpense) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Expense",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpense) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Income Button
                    val isIncome = transactionType == TransactionType.INCOME
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isIncome) Color(0xFF005C55) else Color.Transparent)
                            .clickable { viewModel.setTransactionType(TransactionType.INCOME) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                imageVector = Icons.Filled.ArrowUpward,
                                contentDescription = null,
                                tint = if (isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Income",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isIncome) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Amount Card with Quick Add Chips
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (transactionType == TransactionType.EXPENSE) "TOTAL AMOUNT" else "TOTAL RECEIVED AMOUNT",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currencySymbol,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (transactionType == TransactionType.EXPENSE) Color(0xFFBA1A1A) else Color(0xFF005C55)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (amount.isEmpty()) "0" else amount,
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (transactionType == TransactionType.EXPENSE) Color(0xFFBA1A1A) else Color(0xFF005C55)
                        )
                    }

                    // Quick add pill chips
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val quickAmounts = if (transactionType == TransactionType.EXPENSE) {
                            listOf(50, 100, 500)
                        } else {
                            listOf(500, 1000, 5000)
                        }
                        quickAmounts.forEach { q ->
                            Surface(
                                shape = RoundedCornerShape(100.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                modifier = Modifier.clickable {
                                    val cur = amount.toDoubleOrNull() ?: 0.0
                                    viewModel.setAmount((cur + q).toInt().toString())
                                }
                            ) {
                                Text(
                                    text = "+$currencySymbol$q",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Merchant / Title Input Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 0.5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Storefront,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (transactionType == TransactionType.EXPENSE) "Merchant / Item" else "Source Description",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        androidx.compose.foundation.text.BasicTextField(
                            value = title,
                            onValueChange = viewModel::setTitle,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            decorationBox = { inner ->
                                if (title.isEmpty()) {
                                    Text(
                                        text = if (transactionType == TransactionType.EXPENSE) "e.g. Lunch at Bistro" else "e.g. Monthly Consulting",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                inner()
                            }
                        )
                    }
                    if (title.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.setTitle("") },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear", tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // AI Suggestion Banner
            if (categorySuggestion != null) {
                val isAlreadySelected = selectedCategoryId == categorySuggestion?.categoryId
                val emoji = getCategoryEmoji(categorySuggestion!!.categoryName)
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEFF6FF)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "✨ Suggested: $emoji ${categorySuggestion!!.categoryName}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1D4ED8)
                        )
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = if (isAlreadySelected) Color(0xFF16803C) else Color(0xFF2563EB),
                            modifier = Modifier.clickable { viewModel.acceptAiSuggestion() }
                        ) {
                            Text(
                                text = if (isAlreadySelected) "✓ Applied" else "Apply",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Selector Card
            val selectedCat = categories.find { it.id == selectedCategoryId }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategorySheet = true },
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 0.5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = selectedCat?.let { getCategoryEmoji(it.name) } ?: "🏷️", fontSize = 20.sp)
                        }
                        Column {
                            Text(text = "Category", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = selectedCat?.name ?: "Select Category",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Change >",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Date & Time Twin Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 0.5.dp
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Column {
                            Text("Date", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(selectedDate.format(dateFormatter), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 0.5.dp
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Column {
                            Text("Time", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(selectedTime.format(timeFormatter), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Payment Method Chips
            Text("Payment Method", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val methods = listOf(
                    PaymentMethod.CASH to "💵 Cash",
                    PaymentMethod.UPI to "🔲 UPI",
                    PaymentMethod.DEBIT_CARD to "💳 Debit Card",
                    PaymentMethod.CREDIT_CARD to "💳 Credit Card",
                    PaymentMethod.BANK_TRANSFER to "🏛️ Bank Transfer"
                )
                items(methods) { (method, label) ->
                    val isSelected = paymentMethod == method
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setPaymentMethod(method) },
                        label = { Text(label) },
                        shape = RoundedCornerShape(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Custom On-Screen Numeric Keypad
            VeloraNumericKeypad(
                onDigitClick = { digit ->
                    val cur = if (amount == "0") "" else amount
                    if (cur.length < 9) {
                        viewModel.setAmount(cur + digit)
                    }
                },
                onBackspaceClick = {
                    if (amount.isNotEmpty()) {
                        viewModel.setAmount(amount.dropLast(1))
                    }
                },
                onDecimalClick = {
                    if (!amount.contains(".")) {
                        viewModel.setAmount(if (amount.isEmpty()) "0." else "$amount.")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Save Button
            val buttonText = if (transactionType == TransactionType.EXPENSE) {
                "✓ Save Expense ($currencySymbol${if (amount.isEmpty()) "0" else amount})"
            } else {
                "✓ Save Income (+$currencySymbol${if (amount.isEmpty()) "0" else amount})"
            }
            val buttonBg = if (transactionType == TransactionType.EXPENSE) Color(0xFF005C55) else Color(0xFF16803C)

            Button(
                onClick = viewModel::saveTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonBg),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(buttonText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    if (showCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showCategorySheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Category", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                categories.forEach { category ->
                    Text(
                        text = category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectCategory(category.id)
                                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) showCategorySheet = false
                                }
                            }
                            .padding(vertical = 12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
