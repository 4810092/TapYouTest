package uz.gka.tapyoutest

import android.app.Application
import uz.gka.tapyoutest.di.AppComponent
import uz.gka.tapyoutest.di.AppModule
import uz.gka.tapyoutest.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        component.inject(this)
    }

    companion object {
        lateinit var component: AppComponent
    }
}