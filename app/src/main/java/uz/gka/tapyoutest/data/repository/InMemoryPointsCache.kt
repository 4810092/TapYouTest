package uz.gka.tapyoutest.data.repository

import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.repository.PointsCache
import javax.inject.Inject

class InMemoryPointsCache @Inject constructor() : PointsCache {
    private var cache: List<Point> = emptyList()

    override fun save(points: List<Point>) {
        cache = points
    }

    override fun get(): List<Point> = cache

    override fun clear() {
        cache = emptyList()
    }
}