package com.devileya.moviecatalogue.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devileya.moviecatalogue.domain.module.appModules
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.devileya.moviecatalogue.network.model.VideoModel
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Arif Fadly Siregar 2019-11-07.
 */
class DetailViewModelTest : AutoCloseKoinTest() {
    private val viewModel: DetailViewModel by inject()
    private val dataRepository: DataRepository by inject()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var videos: List<VideoModel>? = null
    private val category = "movie"
    private val movieId = "475557"

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var listVideoObserver: Observer<List<VideoModel>>

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(appModules)
            module { viewModel { viewModel } }
        }
    }

    @Test
    fun getVideos() {
        viewModel.videos.observeForever(listVideoObserver)
        viewModel.getVideoTrailer(movieId, category)
        when (val value = runBlocking { dataRepository.getVideo(movieId, category) }) {
            is UseCaseResult.Success<VideoResponse> -> videos = value.data.results
        }
        Mockito.verify(listVideoObserver).onChanged(videos)
    }
}