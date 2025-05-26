package uz.gka.tapyoutest.domain.repository

import android.graphics.Bitmap
import uz.gka.tapyoutest.domain.model.ChartSaveResult

interface ChartSaver {
    fun save(bitmap: Bitmap): ChartSaveResult

}