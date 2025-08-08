package uz.gka.tapyoutest.data.repository

import uz.gka.tapyoutest.data.mapper.PointMapper
import uz.gka.tapyoutest.data.remote.ApiService
import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.repository.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val apiService: ApiService, private val pointMapper: PointMapper
) : PointsRepository {

    override suspend fun getPoints(count: Int): List<Point> {
        val response = apiService.getPoints(count)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) pointMapper.mapPointDtoListToDomainList(body.points)
            else throw NullPointerException("Empty body")
        } else {
            throw Exception("API error ${response.code()}")
        }
    }
}