package uz.gka.tapyoutest.presentation.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import uz.gka.tapyoutest.domain.model.PointsCount
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.usecase.GetPointsUseCase
import uz.gka.tapyoutest.domain.validator.InputValidator
import uz.gka.tapyoutest.presentation.navigation.Screens
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase,
    private val inputValidator: InputValidator,
    private val pointsCache: PointsCache,
    private val router: Router
) : MvpPresenter<MainView>() {

    private val disposables = CompositeDisposable()

    fun onLoadPointsClicked(input: String) {
        when (val result = inputValidator.validateCountInput(input)) {
            is InputValidator.ValidationResult.Valid -> loadPoints(result.value)
            is InputValidator.ValidationResult.Invalid -> viewState.showInvalidNumberError()
        }
    }

    private fun loadPoints(count: Int) {
        disposables.clear()
        viewState.showLoading(true)
        val disposable = getPointsUseCase(PointsCount(count))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ points ->
                pointsCache.save(points)
                viewState.showLoading(false)
                router.navigateTo(Screens.result())
            }, { error ->
                viewState.showPointsLoadingError(error.message)
                viewState.showLoading(false)
            })
        disposables.add(disposable)
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
