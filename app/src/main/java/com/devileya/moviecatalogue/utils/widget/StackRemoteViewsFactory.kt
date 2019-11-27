package com.devileya.moviecatalogue.utils.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.CATEGORY
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.DATE
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.ID
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.POPULARITY
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.POSTER
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.RATE
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.SYNOPSIS
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.TITLE
import com.devileya.moviecatalogue.network.model.DetailModel
import timber.log.Timber
import java.util.concurrent.ExecutionException

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {


    private var listFavorites: MutableList<DetailModel> = mutableListOf()
    private lateinit var bmp: Bitmap

    override fun onCreate() {

    }

    override fun onDataSetChanged() {

        val identityToken = Binder.clearCallingIdentity()
        val cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor
        mapCursorToList(cursor)
        Binder.restoreCallingIdentity(identityToken)
    }



    override fun onDestroy() {
    }

    override fun getCount(): Int{
        return listFavorites.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        try {
            bmp = Glide.with(mContext)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w185${listFavorites[position].poster}")
                .placeholder(R.drawable.img_placeholder)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()

        } catch (e: InterruptedException) {
            Timber.e("Widget Load Error ${e.message}")
        } catch (e: ExecutionException) {
            Timber.e("Widget Load Error ${e.message}")
        }

        rv.setImageViewBitmap(R.id.imageView, bmp)
        rv.setTextViewText(R.id.textView, listFavorites[position].title)



        val extras = Bundle()
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position)
        extras.putString(ID, listFavorites[position].id)
        extras.putString(TITLE, listFavorites[position].title)
        extras.putString(RATE, listFavorites[position].rating)
        extras.putString(DATE, listFavorites[position].date)
        extras.putString(SYNOPSIS, listFavorites[position].synopsis)
        extras.putString(POSTER, listFavorites[position].poster)
        extras.putString(CATEGORY, listFavorites[position].category)
        extras.putString(POPULARITY, listFavorites[position].popularity)

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun mapCursorToList(moviesCursor: Cursor?) {

        if(moviesCursor!=null) {
            listFavorites.clear()
            while (moviesCursor.moveToNext()) {
                val id = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(ID))
                val title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TITLE))
                val synopsis = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(SYNOPSIS))
                val date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DATE))
                val poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER))
                val rating = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(RATE))
                val category = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(CATEGORY))
                val popularity = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POPULARITY))
                listFavorites.add(
                    DetailModel(
                        id = id,
                        title = title,
                        synopsis = synopsis,
                        date = date,
                        poster = poster,
                        rating = rating,
                        category = category,
                        popularity = popularity
                    )
                )
            }
        }

    }

}