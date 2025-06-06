package uz.gka.tapyoutest.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import uz.gka.tapyoutest.data.local.dao.PointDao
import uz.gka.tapyoutest.data.local.model.PointEntity
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.repository.PointsCache
import javax.inject.Inject

class RoomPointsCache @Inject constructor(
    private val pointDao: PointDao
) : PointsCache {

    override fun save(points: List<Point>) {
        val entities = points.map { PointEntity(x = it.x, y = it.y) }
        Single.fromCallable {
            pointDao.clear()
            pointDao.insertPoints(entities)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun get(): List<Point> {
        return pointDao.getPoints()
            .subscribeOn(Schedulers.io())
            .blockingGet()
            .map { Point(it.x, it.y) }
    }

    override fun clear() {
        Single.fromCallable {
            pointDao.clear()
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}
