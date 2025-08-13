package uz.gka.tapyoutest.presentation.main

sealed class MainEffect {
    data object InvalidNumber : MainEffect()
    data object PointsLoaded : MainEffect()
    data class PointsLoadingError(val message: String?) : MainEffect()
}