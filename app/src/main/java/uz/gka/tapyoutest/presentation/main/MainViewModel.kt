package uz.gka.tapyoutest.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import uz.gka.tapyoutest.domain.usecase.GetPointsUseCase
import uz.gka.tapyoutest.presentation.main.MainEffect.Loading
import uz.gka.tapyoutest.presentation.main.MainEffect.PointsLoaded
import uz.gka.tapyoutest.presentation.main.MainEffect.PointsLoadingError
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<MainEffect>(replay = 0)
    val effect: Flow<MainEffect> by lazy { _effect }

    private var pointsLoadingJob: Job? = null

    private suspend fun emitEffect(effect: MainEffect) {
        _effect.emit(effect)
    }

    fun handleUiAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.LoadPoints -> loadPoints(action.count)
        }
    }

    fun loadPoints(count: String) {
        if (pointsLoadingJob?.isActive == true) pointsLoadingJob?.cancel()

        pointsLoadingJob = viewModelScope.launch {
            val countNum = count.toIntOrNull()
            if (countNum == null || countNum <= 0) {
                emitEffect(MainEffect.InvalidNumber)
                return@launch
            }
            emitEffect(Loading(true))
            runCatching {
                getPointsUseCase(countNum)
            }.onSuccess {
                emitEffect(Loading(false))
                emitEffect(PointsLoaded(it))
            }.onFailure {
                emitEffect(Loading(false))
                emitEffect(PointsLoadingError(it.message))
            }
        }
    }
}