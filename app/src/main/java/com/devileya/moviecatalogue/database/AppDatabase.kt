package com.devileya.moviecatalogue.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devileya.moviecatalogue.database.dao.FavoriteDao
import com.devileya.moviecatalogue.network.model.DetailModel

/**
 * Created by Arif Fadly Siregar 2019-11-07.
 */
@Database(entities = [DetailModel::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "movieDB")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}