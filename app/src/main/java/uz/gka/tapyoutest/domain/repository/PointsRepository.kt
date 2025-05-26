package uz.gka.tapyoutest.domain.repository

import uz.gka.tapyoutest.domain.model.Point

interface PointsRepository {
    suspend fun getPoints(count: Int): List<Point>
}