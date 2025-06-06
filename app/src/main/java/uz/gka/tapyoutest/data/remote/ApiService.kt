package uz.gka.tapyoutest.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import uz.gka.tapyoutest.data.model.PointResponseDto
import io.reactivex.Single

interface ApiService {
    @GET("/api/test/points")
    fun getPoints(@Query("count") count: Int): Single<PointResponseDto>
}