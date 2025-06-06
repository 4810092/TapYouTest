package uz.gka.tapyoutest.domain.repository

import uz.gka.tapyoutest.domain.model.Point
import io.reactivex.Single

interface PointsRepository {
    fun getPoints(count: Int): Single<List<Point>>
}