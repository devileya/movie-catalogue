package com.devileya.moviecatalogue.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
@Parcelize
data class DetailModel(
    val id: String?,
    val title: String?,
    val date: String?,
    val synopsis: String?,
    val rating: String?,
    val poster: String?
): Parcelable