package com.devileya.moviecatalogue.ui.main.search

import androidx.lifecycle.MutableLiveData
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.utils.SingleLiveEvent
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
class SearchViewModel(private val dataRepository: DataRepository): BaseViewModel() {

    val showError = SingleLiveEvent<String>()
    val showLoading = MutableLiveData<Boolean>()
    val tvShows = MutableLiveData<List<TvShowModel>>()
    val movies = MutableLiveData<List<MovieModel>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    fun searchMovie(keyWord: String) {
        Timber.d("keyword movie $keyWord")
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { dataRepository.searchMovie(keyWord) }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<MovieResponse> -> {
                    Timber.d("data movie ${response.data.results}")
                    movies.value = response.data.results
                }
                is UseCaseResult.Error -> {
                    Timber.d("data movie error ${response.exception.message}")
                    showError.value = response.exception.message
                }
            }
        }
    }

    fun searchTVShows(keyWord: String) {
        Timber.d("keyword tv $keyWord")
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { dataRepository.searchTvShow(keyWord) }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<TvShowResponse> -> {
                    Timber.d("data tv ${response.data.results}")
                    tvShows.value = response.data.results
                }
                is UseCaseResult.Error -> {
                    Timber.d("data tv error ${response.exception.message}")
                    showError.value = response.exception.message
                }
            }
        }
    }
}