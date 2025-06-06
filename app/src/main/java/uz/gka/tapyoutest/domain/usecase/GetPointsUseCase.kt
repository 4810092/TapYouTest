package uz.gka.tapyoutest.domain.usecase

import uz.gka.tapyoutest.domain.model.Point
import uz.gka.tapyoutest.domain.model.PointsCount
import uz.gka.tapyoutest.domain.repository.PointsRepository
import io.reactivex.Single
import javax.inject.Inject

class GetPointsUseCase @Inject constructor(
    private val repository: PointsRepository
) {
    operator fun invoke(count: PointsCount): Single<List<Point>> {
        return repository.getPoints(count.value)
    }
}
