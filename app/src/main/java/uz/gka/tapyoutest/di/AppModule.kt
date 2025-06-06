package uz.gka.tapyoutest.di

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import androidx.room.Room
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.BuildConfig
import uz.gka.tapyoutest.data.mapper.PointMapper
import uz.gka.tapyoutest.data.remote.ApiService
import uz.gka.tapyoutest.data.repository.LegacyChartSaver
import uz.gka.tapyoutest.data.repository.PointsRepositoryImpl
import uz.gka.tapyoutest.data.repository.ScopedChartSaver
import uz.gka.tapyoutest.data.repository.RoomPointsCache
import uz.gka.tapyoutest.data.local.database.AppDatabase
import uz.gka.tapyoutest.data.local.dao.PointDao
import uz.gka.tapyoutest.domain.repository.ChartSaver
import uz.gka.tapyoutest.domain.repository.PointsCache
import uz.gka.tapyoutest.domain.repository.PointsRepository
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Provides
    @Singleton
    fun provideApp(): App = application

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideCicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Provides
    @Singleton
    fun provideNavigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.getNavigatorHolder()

    @Provides
    @Reusable
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Reusable
    fun providePointsRepository(
        apiService: ApiService,
        pointMapper: PointMapper
    ): PointsRepository {
        return PointsRepositoryImpl(apiService, pointMapper)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePointDao(db: AppDatabase): PointDao = db.pointDao()


    @Provides
    @Singleton
    fun provideChartSaver(context: Context): ChartSaver {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ScopedChartSaver(context)
        } else {
            LegacyChartSaver(context)
        }
    }

    @Provides
    @Singleton
    fun providePointRepository(pointDao: PointDao): PointsCache = RoomPointsCache(pointDao)
}