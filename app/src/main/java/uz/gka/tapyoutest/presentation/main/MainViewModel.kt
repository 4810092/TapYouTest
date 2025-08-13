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
import uz.gka.tapyoutest.presentation.main.MainEffect.Loading
import uz.gka.tapyoutest.presentation.main.MainEffect.PointsLoaded
import uz.gka.tapyoutest.presentation.main.MainEffect.PointsLoadingError
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase,
    private val inputValidator: InputValidator,
    private val pointsCache: PointsCache
) : ViewModel() {

    private val _effect = MutableStateFlow<MainEffect>(MainEffect.Initial)
    val effect: Flow<MainEffect> by lazy { _effect }

    private var pointsLoadingJob: Job? = null

    private suspend fun emitEffect(effect: MainEffect) {
        _effect.emit(effect)
    }

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
            emitEffect(Loading(true))
            runCatching {
                getPointsUseCase(PointsCount(count))
            }.onSuccess {
                pointsCache.save(it)
                emitEffect(PointsLoaded)
            }.onFailure {
                emitEffect(PointsLoadingError(it.message))
            }
//            emitEffect(Loading(false))
//            emitEffect(MainEffect.Initial)
        }
    }
}