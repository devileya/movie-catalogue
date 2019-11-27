package com.devileya.moviecatalogue.ui.main.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.SingleLiveEvent
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : BaseViewModel() {

    val showError = SingleLiveEvent<String>()
    val showLoading = MutableLiveData<Boolean>()
    val tvShows = MutableLiveData<List<DetailModel>>()
    val movies = MutableLiveData<List<DetailModel>>()
    var tvShowsPagination: LiveData<PagedList<DetailModel>>? = null
    var moviesPagination: LiveData<PagedList<DetailModel>>? = null
    val favorites = MutableLiveData<List<DetailModel>>()

    init {
//        getMovieList()
//        getTvShowList()
        getMoviePagination()
        getTvShowPagination()
        getFavorite()
    }


    private fun getMovieList() {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { favoriteRepository.getMovies() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<List<DetailModel>> -> movies.value = response.data
                is UseCaseResult.Error -> showError.value = response.exception.message
            }
        }
    }

    private fun getTvShowList() {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { favoriteRepository.getTvShows() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<List<DetailModel>> -> tvShows.value = response.data
                is UseCaseResult.Error -> showError.value = response.exception.message
            }
        }
    }

    private fun getMoviePagination() {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { favoriteRepository.getMoviesResources() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success -> {
                    Timber.d("movie data ${response.data.value}")
                    moviesPagination = response.data
                }
                is UseCaseResult.Error -> {
                    Timber.d("movie error ${response.exception.message}")
                    showError.value = response.exception.message
                }
            }
        }
    }

    private fun getTvShowPagination() {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { favoriteRepository.getTvResources() }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<LiveData<PagedList<DetailModel>>> -> {
                    Timber.d("tv data ${response.data}")
                    tvShowsPagination = response.data
                }
                is UseCaseResult.Error -> {
                    Timber.d("tv error ${response.exception.message}")
                    showError.value = response.exception.message
                }
            }
        }
    }

    private fun getFavorite() {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.Default) { favoriteRepository.get() }
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success<List<DetailModel>> -> favorites.value = result.data
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
    }
}
