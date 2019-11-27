package com.devileya.moviecatalogue.database.dao

import android.database.Cursor
import androidx.room.*
import com.devileya.moviecatalogue.network.model.DetailModel

/**
 * Created by Arif Fadly Siregar 2019-11-07.
 */
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detail: DetailModel): Long

    @Delete
    fun delete(detail: DetailModel)

    @Update
    fun update(detail: DetailModel): Int

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteById(id: String) : Int

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getById(id: String): DetailModel

    @Query("SELECT * FROM favorite WHERE id = :id and category = :category")
    fun getByIdCategory(id: String, category: String): DetailModel

    @Query("SELECT * FROM favorite WHERE category = :category")
    fun getByCategory(category: String): List<DetailModel>

    @Query("SELECT * FROM favorite")
    fun getCursor(): Cursor

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getCursorById(id: String): Cursor

    @Query("SELECT * FROM favorite")
    fun get(): List<DetailModel>
}