package com.devileya.moviecatalogue.ui.main.search

import androidx.lifecycle.MutableLiveData
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.utils.SingleLiveEvent
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
class SearchViewModel(private val dataRepository: DataRepository, private val favoriteRepository: FavoriteRepository): BaseViewModel() {

    val showError = SingleLiveEvent<String>()
    val showLoading = MutableLiveData<Boolean>()
    val tvShows = MutableLiveData<List<TvShowModel>>()
    val movies = MutableLiveData<List<MovieModel>>()
    val favorites = MutableLiveData<List<DetailModel>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    fun searchMovie(keyword: String) {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { dataRepository.getMovieList() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<MovieResponse> -> movies.value = response.data.results
                is UseCaseResult.Error -> showError.value = response.exception.message
            }
        }
    }

    fun searchTVShows(keyword: String) {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { dataRepository.getTvShowList() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<TvShowResponse> -> tvShows.value = response.data.results
                is UseCaseResult.Error -> showError.value = response.exception.message
            }
        }
    }
}