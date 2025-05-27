package uz.gka.tapyoutest.presentation.result

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.usecase.SaveChartUseCase
import uz.gka.tapyoutest.presentation.result.ResultEffect.MemoryAccessError
import uz.gka.tapyoutest.presentation.result.ResultEffect.SaveError
import uz.gka.tapyoutest.presentation.result.ResultState.Initial
import uz.gka.tapyoutest.presentation.result.ResultState.PointsData
import javax.inject.Inject

class ResultViewModel @Inject constructor(
    private val saveChartUseCase: SaveChartUseCase, private val pointsCache: PointsCache
) : ViewModel() {

    private val _state = MutableStateFlow<ResultState>(Initial)
    val state: Flow<ResultState> by lazy { _state }

    private val _effect = MutableSharedFlow<ResultEffect>(replay = 0)
    val effect: Flow<ResultEffect> by lazy { _effect }

    init {
        launchState(PointsData(pointsCache.get()))
    }

    private suspend fun emitEffect(effect: ResultEffect) {
        _effect.emit(effect)
    }

    private fun launchEffect(effect: ResultEffect) = viewModelScope.launch {
        emitEffect(effect)
    }

    private suspend fun emitState(state: ResultState) {
        _state.emit(state)
    }

    private fun launchState(state: ResultState) = viewModelScope.launch {
        emitState(state)
    }

    fun handleUiAction(action: ResultUiAction) {
        when (action) {
            is ResultUiAction.SaveChart -> saveChart(action.chartBitmap)
        }
    }

    private fun saveChart(chartBitmap: Bitmap) {
        runCatching {
            saveChartUseCase(chartBitmap)
        }.onSuccess { result ->
            val effect = when (result) {
                is ChartSaveResult.Legacy -> ResultEffect.SavedIn(result.filePath)
                ChartSaveResult.Scoped -> ResultEffect.Saved
            }
            launchEffect(effect)
        }.onFailure {
            when (it) {
                is IllegalStateException -> launchEffect(MemoryAccessError)
                else -> launchEffect(SaveError)
            }
        }
    }
}
