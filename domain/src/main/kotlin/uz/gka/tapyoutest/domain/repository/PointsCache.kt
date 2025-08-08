package uz.gka.tapyoutest.domain.repository

import uz.gka.tapyoutest.domain.model.Point

interface PointsCache {
    fun save(points: List<Point>)
    fun get(): List<Point>
    fun clear()
}