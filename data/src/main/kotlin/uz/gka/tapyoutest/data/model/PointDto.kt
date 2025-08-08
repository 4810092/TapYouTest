package uz.gka.tapyoutest.data.model

import com.google.gson.annotations.SerializedName

data class PointDto(
    @SerializedName("x") val x: Double?,
    @SerializedName("y") val y: Double?
)