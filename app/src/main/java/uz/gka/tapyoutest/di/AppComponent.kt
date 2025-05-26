package uz.gka.tapyoutest.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import uz.gka.tapyoutest.App
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
    ]
)

interface AppComponent {
    fun inject(application: App)

    fun getViewModelFactory(): ViewModelProvider.Factory
}