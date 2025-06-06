package uz.gka.tapyoutest.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gka.tapyoutest.data.local.dao.PointDao
import uz.gka.tapyoutest.data.local.model.PointEntity

@Database(entities = [PointEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pointDao(): PointDao
}
