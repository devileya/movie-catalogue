package com.devileya.moviecatalogue.database.dao

import androidx.room.*
import com.devileya.moviecatalogue.network.model.DetailModel

/**
 * Created by Arif Fadly Siregar 2019-11-07.
 */
@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detail: DetailModel)

    @Delete
    fun delete(detail: DetailModel)

    @Update
    suspend fun update(detail: DetailModel)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getById(id: String): DetailModel

    @Query("SELECT * FROM favorite WHERE id = :id and category = :category")
    suspend fun getByIdCategory(id: String, category: String): DetailModel

    @Query("SELECT * FROM favorite WHERE category = :category")
    suspend fun getByCategory(category: String): List<DetailModel>
}