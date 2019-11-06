package com.devileya.moviecatalogue.ui.detail

import androidx.lifecycle.MutableLiveData
import com.devileya.moviecatalogue.base.BaseViewModel
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.devileya.moviecatalogue.network.model.VideoModel
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
class DetailViewModel(private val dataRepository: DataRepository): BaseViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    val videos = MutableLiveData<List<VideoModel>>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

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
}