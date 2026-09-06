package com.velora.tracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val themeMode: StateFlow<Int> = settingsRepository.getThemeMode()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₹")

    val openingBalance: StateFlow<Long> = settingsRepository.getOpeningBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val isAiEnabled: StateFlow<Boolean> = settingsRepository.isAiCategorizationEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _showOpeningBalanceDialog = MutableStateFlow(false)
    val showOpeningBalanceDialog: StateFlow<Boolean> = _showOpeningBalanceDialog.asStateFlow()

    private val _openingBalanceInput = MutableStateFlow("")
    val openingBalanceInput: StateFlow<String> = _openingBalanceInput.asStateFlow()

    private val _showCurrencyDialog = MutableStateFlow(false)
    val showCurrencyDialog: StateFlow<Boolean> = _showCurrencyDialog.asStateFlow()

    fun setThemeMode(mode: Int) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setCurrencySymbol(symbol: String) {
        viewModelScope.launch {
            settingsRepository.setCurrencySymbol(symbol)
            _showCurrencyDialog.value = false
        }
    }

    fun showOpeningBalanceEditor() {
        // Pre-fill input (convert minor to major)
        val currentMajor = openingBalance.value / 100
        _openingBalanceInput.value = if (currentMajor == 0L) "" else currentMajor.toString()
        _showOpeningBalanceDialog.value = true
    }
    
    fun hideOpeningBalanceEditor() {
        _showOpeningBalanceDialog.value = false
    }

    fun setOpeningBalanceInput(input: String) {
        _openingBalanceInput.value = input
    }

    fun saveOpeningBalance() {
        val input = _openingBalanceInput.value
        val majorAmount = input.toLongOrNull() ?: 0L
        val minorAmount = majorAmount * 100 // Convert to paise
        viewModelScope.launch {
            settingsRepository.setOpeningBalance(minorAmount)
            _showOpeningBalanceDialog.value = false
        }
    }

    fun toggleAiCategorization() {
        viewModelScope.launch {
            settingsRepository.setAiCategorizationEnabled(!isAiEnabled.value)
        }
    }

    fun showCurrencySelector() {
        _showCurrencyDialog.value = true
    }
    
    fun hideCurrencySelector() {
        _showCurrencyDialog.value = false
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(app.settingsRepository) as T
            }
        }
    }
}
