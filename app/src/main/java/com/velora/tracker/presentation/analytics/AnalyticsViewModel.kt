package com.velora.tracker.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.velora.tracker.VeloraApplication
import com.velora.tracker.domain.model.AnalyticsData
import com.velora.tracker.domain.model.DateRange
import com.velora.tracker.domain.repository.SettingsRepository
import com.velora.tracker.domain.usecase.GetAnalyticsDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModel(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val selectedDateRange = MutableStateFlow<DateRange>(DateRange.ThisMonth)
    val trendGranularity = MutableStateFlow("daily")

    val currencySymbol: StateFlow<String> = settingsRepository.getCurrencySymbol()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "₹")

    val analyticsData: StateFlow<AnalyticsData> = combine(
        selectedDateRange,
        trendGranularity
    ) { range, granularity ->
        range to granularity
    }.flatMapLatest { (range, granularity) ->
        _isLoading.value = true
        getAnalyticsDataUseCase(range, granularity)
            .onEach { _isLoading.value = false }
            .catch { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AnalyticsData()
    )

    fun selectDateRange(range: DateRange) {
        selectedDateRange.value = range
    }

    fun setTrendGranularity(granularity: String) {
        trendGranularity.value = granularity
    }

    companion object {
        fun provideFactory(app: VeloraApplication): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AnalyticsViewModel(
                    app.getAnalyticsDataUseCase,
                    app.settingsRepository
                ) as T
            }
        }
    }
}
