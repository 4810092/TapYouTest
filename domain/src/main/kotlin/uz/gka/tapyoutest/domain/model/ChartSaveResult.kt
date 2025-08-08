package uz.gka.tapyoutest.domain.model

sealed class ChartSaveResult {
    data object Scoped : ChartSaveResult()
    data class Legacy(val filePath: String) : ChartSaveResult()
}