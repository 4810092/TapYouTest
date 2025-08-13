package uz.gka.tapyoutest.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.gka.tapyoutest.domain.model.PointsCount
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.usecase.GetPointsUseCase
import uz.gka.tapyoutest.domain.validator.InputValidator
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase,
    private val inputValidator: InputValidator,
    private val pointsCache: PointsCache
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: Flow<MainState> by lazy { _state }

    private val _effect = MutableSharedFlow<MainEffect>(replay = 0)
    val effect: Flow<MainEffect> by lazy { _effect }

    private var pointsLoadingJob: Job? = null

    private suspend fun emitState(state: MainState) = _state.emit(state)

    private suspend fun emitEffect(effect: MainEffect) = _effect.emit(effect)

    private fun launchEffect(effect: MainEffect) = viewModelScope.launch {
        emitEffect(effect)
    }

    fun handleUiAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.LoadPoints -> onSubmitCount(action.count)
        }
    }

    private fun onSubmitCount(input: String) {
        when (val result = inputValidator.validateCountInput(input)) {
            is InputValidator.ValidationResult.Valid -> loadPoints(result.value)
            is InputValidator.ValidationResult.Invalid -> launchEffect(MainEffect.InvalidNumber)
        }
    }

    private fun loadPoints(count: Int) {
        if (pointsLoadingJob?.isActive == true) pointsLoadingJob?.cancel()
        pointsLoadingJob = viewModelScope.launch {
            emitState(MainState(isLoading = true))
            runCatching {
                getPointsUseCase(PointsCount(count))
            }.onSuccess {
                pointsCache.save(it)
                emitEffect(MainEffect.PointsLoaded)
            }.onFailure {
                emitEffect(MainEffect.PointsLoadingError(it.message))
            }
            emitState(MainState(isLoading = false))
        }
    }
}