package com.devileya.topmovie.ui

import android.database.Cursor
import android.os.Binder
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devileya.topmovie.database.DatabaseContract
import com.devileya.topmovie.database.model.DetailModel
import com.devileya.topmovie.utils.DataEnum

class MainViewModel : ViewModel() {

    val tvShows = MutableLiveData<List<DetailModel>>()
    val movies = MutableLiveData<List<DetailModel>>()

    fun getMovieList(cursor: Cursor?) {
        val identityToken = Binder.clearCallingIdentity()
        mapCursorToList(cursor, DataEnum.MOVIE.value)
        Binder.restoreCallingIdentity(identityToken)
    }

    fun getTvShowList(cursor: Cursor?) {
        val identityToken = Binder.clearCallingIdentity()
        mapCursorToList(cursor, DataEnum.TV.value)
        Binder.restoreCallingIdentity(identityToken)
    }

    private fun mapCursorToList(moviesCursor: Cursor?, cat: String) {

        if(moviesCursor!=null) {
            val listMovie = mutableListOf<DetailModel>()
            while (moviesCursor.moveToNext()) {
                val id = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow("id"))
                val title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.TITLE
                ))
                val synopsis = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.SYNOPSIS
                ))
                val date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.DATE
                ))
                val poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.POSTER
                ))
                val rate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.RATE
                ))
                val category = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.CATEGORY
                ))
                val popularity = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(
                    DatabaseContract.FavColumns.POPULARITY
                ))
                if(category == cat)
                    listMovie.add(
                        DetailModel(
                            id = id,
                            title = title,
                            date = date,
                            synopsis = synopsis,
                            rating = rate,
                            poster = poster,
                            category = category,
                            popularity = popularity
                        )
                    )
            }
            Log.d("MovieFragment", "Data $cat $listMovie")
            when (cat) {
                DataEnum.MOVIE.value -> movies.value = listMovie
                DataEnum.TV.value -> tvShows.value = listMovie
            }
        }
    }
}
