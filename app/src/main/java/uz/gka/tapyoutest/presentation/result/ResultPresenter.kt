package uz.gka.tapyoutest.presentation.result

import android.graphics.Bitmap
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import uz.gka.tapyoutest.domain.model.ChartSaveResult
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.usecase.SaveChartUseCase
import javax.inject.Inject

class ResultPresenter @Inject constructor(
    private val saveChartUseCase: SaveChartUseCase,
    pointsCache: PointsCache
) : MvpPresenter<ResultView>() {

    private val disposables = CompositeDisposable()
    private val points = pointsCache.get()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showPoints(points)
    }

    fun onSaveChartClicked(bitmap: Bitmap) {
        val disposable = Single.fromCallable { saveChartUseCase(bitmap) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                when (result) {
                    is ChartSaveResult.Legacy -> viewState.showChartSavedIn(result.filePath)
                    ChartSaveResult.Scoped -> viewState.showChartSaved()
                }
            }, { error ->
                when (error) {
                    is IllegalStateException -> viewState.showMemoryAccessError()
                    else -> viewState.showSaveError()
                }
            })
        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
