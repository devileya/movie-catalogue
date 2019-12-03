package com.devileya.topmovie.database

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by Arif Fadly Siregar 2019-12-03.
 */
object DatabaseContract {
    const val AUTHORITY = "com.devileya.moviecatalogue"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite"
            const val ID = "id"
            const val TITLE = "title"
            const val SYNOPSIS = "synopsis"
            const val RATE = "rating"
            const val POSTER = "poster"
            const val DATE = "date"
            const val CATEGORY = "category"
            const val POPULARITY = "popularity"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}