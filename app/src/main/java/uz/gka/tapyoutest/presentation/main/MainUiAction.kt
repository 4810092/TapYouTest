package uz.gka.tapyoutest.presentation.main

sealed class MainUiAction {
    data class LoadPoints(val count: String) : MainUiAction()

}