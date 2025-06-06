package uz.gka.tapyoutest.presentation.result

import uz.gka.tapyoutest.domain.model.Point
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.SingleState

interface ResultView : MvpView {
    @SingleState
    fun showPoints(points: List<Point>)

    @OneExecution
    fun showChartSaved()

    @OneExecution
    fun showChartSavedIn(path: String)

    @OneExecution
    fun showSaveError()

    @OneExecution
    fun showMemoryAccessError()
}
