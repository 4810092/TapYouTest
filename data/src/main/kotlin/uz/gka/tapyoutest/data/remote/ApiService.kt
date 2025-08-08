package uz.gka.tapyoutest.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.gka.tapyoutest.data.model.PointResponseDto

interface ApiService {
    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") count: Int): Response<PointResponseDto>
}