package uz.gka.tapyoutest.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points")
data class PointEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val x: Double,
    val y: Double
)
