package uz.gka.tapyoutest.presentation.main

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface MainView : MvpView {
    @SingleState
    fun showLoading(show: Boolean)

    @OneExecution
    fun showInvalidNumberError()

    @OneExecution
    fun showPointsLoadingError(message: String?)
}
