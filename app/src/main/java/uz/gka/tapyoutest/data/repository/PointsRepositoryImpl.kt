package uz.gka.tapyoutest.data.repository

import io.reactivex.Single
import uz.gka.tapyoutest.data.mapper.PointMapper
import uz.gka.tapyoutest.data.remote.ApiService
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.repository.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val pointMapper: PointMapper
) : PointsRepository {

    override fun getPoints(count: Int): Single<List<Point>> {
        return apiService.getPoints(count)
            .map { response ->
                pointMapper.mapPointDtoListToDomainList(response.points)
            }
    }
}