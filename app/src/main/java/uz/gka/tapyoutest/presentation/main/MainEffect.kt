package uz.gka.tapyoutest.presentation.main

sealed class MainEffect {
    data object Initial: MainEffect()
    data object InvalidNumber : MainEffect()
    data class Loading(val show: Boolean) : MainEffect()
    data object PointsLoaded : MainEffect()
    data class PointsLoadingError(val message: String?) : MainEffect()
}