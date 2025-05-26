package uz.gka.tapyoutest.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Point(
    val x: Double,
    val y: Double
) : Parcelable