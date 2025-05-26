package uz.gka.tapyoutest.domain.usecase

import android.graphics.Bitmap
import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.repository.ChartSaver
import javax.inject.Inject

class SaveChartUseCase @Inject constructor(
    private val chartSaver: ChartSaver
) {
    operator fun invoke(bitmap: Bitmap): ChartSaveResult {
        return chartSaver.save(bitmap)
    }
}