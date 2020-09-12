package com.ddapps.reservoirreviews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ddapps.reservoirreviews.data.local.dao.ReviewDao
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity


@Database(
    entities = [
        ReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)

public abstract class AppDataBase: RoomDatabase() {
    public abstract fun reviewDao() : ReviewDao
}