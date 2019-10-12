package com.devileya.moviecatalogue.ui.main

import androidx.lifecycle.ViewModel
import com.devileya.moviecatalogue.network.ApiInteractor
import com.devileya.moviecatalogue.network.ApiService
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import kotlinx.coroutines.*

class MainFragmentViewModel: ViewModel() {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private val api_key = "e7a41162cc1c3c684ee7bb055f184e9c"

    fun getMovieList(): List<MovieModel> = runBlocking { apiServices.getMovieAsync(api_key).results }

    fun getTvShowList(): List<TvShowModel> = runBlocking { apiServices.getTvAsync(api_key).results }
}
