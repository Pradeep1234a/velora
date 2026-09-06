package com.velora.tracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.model.DashboardData
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.usecase.GetDashboardDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _selectedDateRange = MutableStateFlow<DateRange>(DateRange.ThisMonth)
    val selectedDateRange: StateFlow<DateRange> = _selectedDateRange.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "₹"
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardData: StateFlow<DashboardData> = _selectedDateRange
        .flatMapLatest { dateRange ->
            _isLoading.value = true
            getDashboardDataUseCase(dateRange)
        }
        .catch { e ->
            _error.value = e.message ?: "An error occurred"
            emit(DashboardData())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardData()
        )

    fun selectDateRange(dateRange: DateRange) {
        _selectedDateRange.value = dateRange
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DashboardViewModel(
                    app.getDashboardDataUseCase,
                    app.settingsRepository
                ) as T
            }
        }
    }
}
