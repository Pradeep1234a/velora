package com.velora.tracker.presentation.addtransaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.velora.tracker.domain.model.PaymentMethod
import com.velora.tracker.domain.model.TransactionType
import com.velora.tracker.ui.components.VeloraFormField
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
            // Transaction Type Segmented Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.setTransactionType(TransactionType.EXPENSE) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Expense", color = if (transactionType == TransactionType.EXPENSE) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                }
                OutlinedButton(
                    onClick = { viewModel.setTransactionType(TransactionType.INCOME) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (transactionType == TransactionType.INCOME) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text("Income", color = if (transactionType == TransactionType.INCOME) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Amount Field
            OutlinedTextField(
                value = amount,
                onValueChange = viewModel::setAmount,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.displaySmall,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                leadingIcon = { Text(currencySymbol, style = MaterialTheme.typography.headlineMedium) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            VeloraFormField(
                label = "Title",
                value = title,
                onValueChange = viewModel::setTitle,
                placeholder = "What was this for?"
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (categorySuggestion != null && selectedCategoryId == null) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "AI suggested: ${categorySuggestion!!.categoryName}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        TextButton(onClick = viewModel::acceptAiSuggestion) {
                            Text("Accept")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Box(modifier = Modifier.clickable { showCategorySheet = true }) {
                VeloraFormField(
                    label = "Category",
                    value = categories.find { it.id == selectedCategoryId }?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                VeloraFormField(
                    label = "Date",
                    value = selectedDate.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                VeloraFormField(
                    label = "Time",
                    value = selectedTime.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            VeloraFormField(
                label = "Notes (Optional)",
                value = notes,
                onValueChange = viewModel::setNotes,
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::saveTransaction,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Save Transaction")
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
