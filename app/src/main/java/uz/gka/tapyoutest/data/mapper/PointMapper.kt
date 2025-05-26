package uz.gka.tapyoutest.data.mapper

import uz.gka.tapyoutest.data.model.PointDto
import uz.gka.tapyoutest.domain.model.Point
import javax.inject.Inject

class PointMapper @Inject constructor() {

    fun mapPointDtoListToDomainList(pointDtoList: List<PointDto?>?): List<Point> {
        if (pointDtoList == null) return emptyList()
        return pointDtoList.mapNotNull { mapPointDtoToDomain(it) }.sortedBy { it.x }
    }

    fun mapPointDtoToDomain(pointDto: PointDto?): Point? {
        if (pointDto == null) return null
        if (pointDto.x == null) return null
        if (pointDto.y == null) return null
        return Point(pointDto.x, pointDto.y)
    }
}