package uz.gka.tapyoutest.presentation.result

import uz.gka.tapyoutest.domain.model.Point

sealed class ResultState {
    data object Initial : ResultState()
    data class PointsData(val points: List<Point>) : ResultState()
}