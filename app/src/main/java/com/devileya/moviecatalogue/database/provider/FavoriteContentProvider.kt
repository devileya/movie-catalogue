package com.devileya.moviecatalogue.database.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.devileya.moviecatalogue.database.AppDatabase
import com.devileya.moviecatalogue.database.DatabaseContract.AUTHORITY
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.DataEnum

/**
 * Created by Arif Fadly Siregar 2019-11-26.
 */
class FavoriteContentProvider: ContentProvider() {

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private var db: AppDatabase? = null

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)
            sUriMatcher.addURI(AUTHORITY, DataEnum.FAVORITE.value + "/*", FAVORITE_ID)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE) {
            sUriMatcher.match(uri) -> db?.favoriteDao()!!.insert(fromContentValues(values!!))
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE -> db?.favoriteDao()?.getCursor()
            FAVORITE_ID -> db?.favoriteDao()?.getCursorById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        db = AppDatabase.getAppDataBase(context as Context)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val updated: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> db?.favoriteDao()?.update(fromContentValues(values!!)) as Int
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> db?.favoriteDao()!!.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    private fun fromContentValues(values: ContentValues): DetailModel {
        return DetailModel(
            id = if (values.containsKey("id"))values.getAsString("id") else "",
            poster = if (values.containsKey("poster"))values.getAsString("poster") else "",
            synopsis = if (values.containsKey("synopsis"))values.getAsString("synopsis") else "",
            rating = if (values.containsKey("rate"))values.getAsString("rate") else "",
            title = if (values.containsKey("title"))values.getAsString("title") else "",
            category = if (values.containsKey("category"))values.getAsString("category") else "",
            date = if (values.containsKey("date"))values.getAsString("date") else "",
            popularity = if (values.containsKey("date"))values.getAsString("popularity") else "")
    }
}