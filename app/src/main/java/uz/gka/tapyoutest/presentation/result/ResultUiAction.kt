package uz.gka.tapyoutest.presentation.result

sealed class ResultUiAction {
    data class SaveChart(val chartData: ByteArray) : ResultUiAction()
}