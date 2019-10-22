package com.devileya.moviecatalogue.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devileya.moviecatalogue.BuildConfig
import com.devileya.moviecatalogue.network.ApiInteractor
import com.devileya.moviecatalogue.network.ApiService
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainFragmentViewModel: ViewModel(), CoroutineScope {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    val showLoading = MutableLiveData<Boolean>()
    val tvShows = MutableLiveData<List<TvShowModel>>()
    val movies = MutableLiveData<List<MovieModel>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    init {
        getMovieList()
        getTvShowList()
    }


    fun getMovieList() {
        showLoading.value = true
        launch {
            try {
                val data = withContext(Dispatchers.Default) { apiServices.getMovieAsync(BuildConfig.API_KEY) }
                Log.d("datass",data.toString())
                movies.value = data.results
                showLoading.value = false
            } catch (e: java.lang.Exception) {
                showLoading.value = false
                Log.e("errorrr", e.message.toString())
            }
        }
    }

//    fun getTvShowList(): List<TvShowModel> = runBlocking { apiServices.getTvAsync(api_key).results }
    fun getTvShowList() {
        showLoading.value = true
        launch {
            try {
                val data =
                    withContext(Dispatchers.Default) { apiServices.getTvAsync(BuildConfig.API_KEY) }
                tvShows.value = data.results
                showLoading.value = false
            } catch (e: Exception) {
                showLoading.value = false
                Log.e("errorrr", e.message.toString())
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
