package uz.gka.tapyoutest.presentation.main

import uz.gka.tapyoutest.domain.model.Point

sealed class MainEffect {
    data object InvalidNumber : MainEffect()
    data class Loading(val show: Boolean) : MainEffect()
    data class PointsLoaded(val points: List<Point>) : MainEffect()
    data class PointsLoadingError(val message: String?) : MainEffect()
}