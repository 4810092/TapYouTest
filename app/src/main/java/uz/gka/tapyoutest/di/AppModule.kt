package uz.gka.tapyoutest.di

import android.content.Context
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gka.tapyoutest.App
import uz.gka.tapyoutest.BuildConfig
import uz.gka.tapyoutest.data.mapper.PointMapper
import uz.gka.tapyoutest.data.remote.ApiService
import uz.gka.tapyoutest.data.repository.LegacyChartSaver
import uz.gka.tapyoutest.data.repository.PointsRepositoryImpl
import uz.gka.tapyoutest.data.repository.ScopedChartSaver
import uz.gka.tapyoutest.domain.repository.ChartSaver
import uz.gka.tapyoutest.domain.repository.PointsRepository
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
    @Reusable
    fun provideApiService(): ApiService {
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Provides
    @Reusable
    fun providePointsRepository(
        apiService: ApiService, pointMapper: PointMapper
    ): PointsRepository {
        return PointsRepositoryImpl(apiService, pointMapper)
    }


    @Provides
    @Singleton
    fun provideChartSaver(context: Context): ChartSaver {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ScopedChartSaver(context)
        } else {
            LegacyChartSaver(context)
        }
    }
}