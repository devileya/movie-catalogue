package com.devileya.topmovie.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
@Entity(tableName = "favorite")
data class DetailModel(
    @PrimaryKey
    val id: String,
    val title: String?,
    val date: String?,
    val synopsis: String?,
    val rating: String?,
    val poster: String?,
    val category: String?,
    val popularity: String?
)