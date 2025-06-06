package uz.gka.tapyoutest.di

import dagger.Component
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.usecase.GetPointsUseCase
import uz.gka.tapyoutest.domain.usecase.SaveChartUseCase
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.NavigatorHolder
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
    ]
)

interface AppComponent {
    fun inject(application: App)

    fun router(): Router
    fun navigatorHolder(): NavigatorHolder
    fun getPointsUseCase(): GetPointsUseCase
    fun saveChartUseCase(): SaveChartUseCase
    fun pointsCache(): PointsCache
}