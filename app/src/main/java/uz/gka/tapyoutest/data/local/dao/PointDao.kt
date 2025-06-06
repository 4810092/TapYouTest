package uz.gka.tapyoutest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import uz.gka.tapyoutest.data.local.model.PointEntity

@Dao
interface PointDao {
    @Query("SELECT * FROM points")
    fun getPoints(): Single<List<PointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoints(points: List<PointEntity>)

    @Query("DELETE FROM points")
    fun clear()
}
