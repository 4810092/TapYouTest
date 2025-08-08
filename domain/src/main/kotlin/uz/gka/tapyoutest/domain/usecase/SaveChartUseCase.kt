package uz.gka.tapyoutest.domain.usecase

import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.repository.ChartSaver
import javax.inject.Inject

class SaveChartUseCase @Inject constructor(
    private val chartSaver: ChartSaver
) {
    operator fun invoke(data: ByteArray): ChartSaveResult {
        return chartSaver.save(data)
    }
}