package com.devileya.moviecatalogue.ui.main.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.devileya.moviecatalogue.utils.SingleLiveEvent
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : BaseViewModel() {

    val showError = SingleLiveEvent<String>()
    val showLoading = MutableLiveData<Boolean>()
    var tvShowsPagination: LiveData<PagedList<DetailModel>>? = null
    var moviesPagination: LiveData<PagedList<DetailModel>>? = null
    val favorites = MutableLiveData<List<DetailModel>>()

    init {
        getMoviePagination()
        getTvShowPagination()
        getFavorite()
    }

    private fun getMoviePagination() {
        EspressoIdlingResource.increment()
        showLoading.value = true
        val config = (PagedList.Config.Builder()).setPageSize(20).setEnablePlaceholders(false).build()
        val result = favoriteRepository.getMoviesResources()
        moviesPagination = LivePagedListBuilder(result, config).build()
        showLoading.value = false
        EspressoIdlingResource.decrement()
    }

    private fun getTvShowPagination() {
        EspressoIdlingResource.increment()
        showLoading.value = true
        val config = (PagedList.Config.Builder()).setPageSize(20).setEnablePlaceholders(false).build()
        val result = favoriteRepository.getTvResources()
        tvShowsPagination = LivePagedListBuilder(result, config).build()
        showLoading.value = false
        EspressoIdlingResource.decrement()
    }

    private fun getFavorite() {
        EspressoIdlingResource.increment()
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.Default) { favoriteRepository.get() }
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success<List<DetailModel>> -> favorites.value = result.data
                is UseCaseResult.Error -> showError.value = result.exception.message
            }
        }
        EspressoIdlingResource.decrement()
    }

    internal fun deleteFavorite(detailModel: DetailModel) {
        launch { withContext(Dispatchers.Default) { favoriteRepository.delete(detailModel) } }
    }

    internal fun insertFavorite(detailModel: DetailModel) {
        launch { withContext(Dispatchers.Default) { favoriteRepository.insert(detailModel) } }
    }
}
