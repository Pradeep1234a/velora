package com.velora.tracker.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.velora.tracker.R
import com.velora.tracker.ui.components.MoneyFormatter
import com.velora.tracker.ui.components.VeloraSectionHeader
import com.velora.tracker.ui.components.VeloraTopBar
import com.velora.tracker.ui.theme.Spacing
import kotlinx.coroutines.launch

val COMMON_CURRENCIES = listOf(
    "₹" to "Indian Rupee (INR)",
    "$" to "US Dollar (USD)",
    "€" to "Euro (EUR)",
    "£" to "British Pound (GBP)",
    "¥" to "Japanese Yen / Chinese Yuan (JPY/CNY)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val currencySymbol by viewModel.currencySymbol.collectAsState()
    val openingBalance by viewModel.openingBalance.collectAsState()
    val isAiEnabled by viewModel.isAiEnabled.collectAsState()
    val showOpeningBalanceDialog by viewModel.showOpeningBalanceDialog.collectAsState()
    val openingBalanceInput by viewModel.openingBalanceInput.collectAsState()
    val showCurrencyDialog by viewModel.showCurrencyDialog.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            VeloraTopBar(
                title = "Settings",
                onNavigateBack = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "APPEARANCE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 0.5.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Theme",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(modifier = Modifier.padding(top = 10.dp)) {
                                FilterChip(
                                    selected = themeMode == 0,
                                    onClick = { viewModel.setThemeMode(0) },
                                    label = { Text("System") },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                FilterChip(
                                    selected = themeMode == 1,
                                    onClick = { viewModel.setThemeMode(1) },
                                    label = { Text("Light") },
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                FilterChip(
                                    selected = themeMode == 2,
                                    onClick = { viewModel.setThemeMode(2) },
                                    label = { Text("Dark") }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "FINANCE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 0.5.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            ListItem(
                                headlineContent = { Text("Currency", fontWeight = FontWeight.Medium) },
                                supportingContent = { Text("Current: $currencySymbol") },
                                modifier = Modifier.clickable { viewModel.showCurrencySelector() }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                            ListItem(
                                headlineContent = { Text("Opening Balance", fontWeight = FontWeight.Medium) },
                                supportingContent = { 
                                    Text("Current: " + MoneyFormatter.format(openingBalance, currencySymbol, showSign = false))
                                },
                                modifier = Modifier.clickable { viewModel.showOpeningBalanceEditor() }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                            ListItem(
                                headlineContent = { Text("Categories", fontWeight = FontWeight.Medium) },
                                supportingContent = { Text("Manage income and expense categories") },
                                modifier = Modifier.clickable { onNavigateToCategories() }
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "INTELLIGENCE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 0.5.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = { Text("AI Categorization", fontWeight = FontWeight.Medium) },
                            supportingContent = { Text("Automatically suggest categories based on transaction details") },
                            trailingContent = {
                                Switch(
                                    checked = isAiEnabled,
                                    onCheckedChange = { viewModel.toggleAiCategorization() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        )
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "DATA",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shadowElevation = 0.5.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            ListItem(
                                headlineContent = { Text("Export Transactions", fontWeight = FontWeight.Medium) },
                                supportingContent = { Text("Export to CSV") },
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Coming soon")
                                    }
                                }
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                            ListItem(
                                headlineContent = { Text("Import Transactions", fontWeight = FontWeight.Medium) },
                                supportingContent = { Text("Import from CSV") },
                                modifier = Modifier.clickable {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Coming soon")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    shadowElevation = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_velora_logo),
                            contentDescription = "Velora Logo",
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Velora Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Intelligent Personal Finance & Expense Tracker",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Version 1.0.0",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    if (showOpeningBalanceDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideOpeningBalanceEditor() },
            title = { Text("Opening Balance") },
            text = {
                OutlinedTextField(
                    value = openingBalanceInput,
                    onValueChange = viewModel::setOpeningBalanceInput,
                    label = { Text("Amount ($currencySymbol)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = viewModel::saveOpeningBalance) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideOpeningBalanceEditor() }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showCurrencyDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideCurrencySelector() },
            title = { Text("Select Currency") },
            text = {
                Column {
                    COMMON_CURRENCIES.forEach { (symbol, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setCurrencySymbol(symbol) }
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                        ) {
                            Text(text = "$symbol - $name", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.hideCurrencySelector() }) {
                    Text("Cancel")
                }
            }
        )
    }
}
