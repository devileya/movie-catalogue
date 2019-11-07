package com.devileya.moviecatalogue.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
@Parcelize
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
): Parcelable