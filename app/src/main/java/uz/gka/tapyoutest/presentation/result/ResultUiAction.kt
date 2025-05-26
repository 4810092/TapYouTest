package uz.gka.tapyoutest.presentation.result

import android.graphics.Bitmap

sealed class ResultUiAction {
    data class SaveChart(val chartBitmap: Bitmap) : ResultUiAction()
}