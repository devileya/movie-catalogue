package com.devileya.moviecatalogue.ui.detail

import androidx.lifecycle.MutableLiveData
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.VideoModel
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
class DetailViewModel(private val dataRepository: DataRepository, private val favoriteRepository: FavoriteRepository): BaseViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    val videos = MutableLiveData<List<VideoModel>>()
    val favorite = MutableLiveData<DetailModel>()

    internal fun getFavoriteById(id: String) {
        showLoading.value = true
        launch {
            val result = withContext(Dispatchers.Default) { favoriteRepository.getFavoriteById(id) }
            showLoading.value = false
            when (result) {
                is UseCaseResult.Success<DetailModel> -> favorite.value = result.data
                is UseCaseResult.Error -> favorite.value = null
            }
        }
    }

    internal fun getVideoTrailer(movieId: String?, category: String?) {
        showLoading.value = true
        launch {
            val response = withContext(Dispatchers.Default) { dataRepository.getVideo(movieId, category) }
            showLoading.value = false
            when (response) {
                is UseCaseResult.Success<VideoResponse> -> videos.value = response.data.results
                is UseCaseResult.Error -> Timber.e(response.exception)
            }
        }
    }

    internal fun insertFavorite(detailModel: DetailModel) {
        launch { withContext(Dispatchers.Default) { favoriteRepository.insert(detailModel) } }
        favorite.value = detailModel
    }

    internal fun deleteFavorite(detailModel: DetailModel) {
        launch { withContext(Dispatchers.Default) { favoriteRepository.delete(detailModel) } }
        favorite.value = null
    }
}