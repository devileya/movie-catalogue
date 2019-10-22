package com.devileya.moviecatalogue.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devileya.moviecatalogue.network.ApiInteractor
import com.devileya.moviecatalogue.network.ApiService
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Arif Fadly Siregar 2019-10-22.
 */
class DetailViewModel: ViewModel(), CoroutineScope {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    val showLoading = MutableLiveData<Boolean>()
    val videoResponse = MutableLiveData<VideoResponse>()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main

    internal fun getVideoTrailer(movieId: String?, category: String?) {
        showLoading.value = true
        launch {
            try {
                val data = withContext(Dispatchers.Default) { apiServices.getVideoAsync(category, movieId) }
                Log.d("datass",data.toString())
                videoResponse.value = data
                showLoading.value = false
            } catch (e: Exception) {
                showLoading.value = false
                Log.e("error", e.message.toString())
            }
        }
    }
}