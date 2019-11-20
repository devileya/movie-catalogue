package com.devileya.moviecatalogue.ui.main

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devileya.moviecatalogue.domain.module.appModules
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

/**
 * Created by Arif Fadly Siregar 2019-10-27.
 */
@RunWith(JUnit4::class)
class MainFragmentViewModelTest : AutoCloseKoinTest() {
    private val viewModel: MainFragmentViewModel by inject()
    private val dataRepository: DataRepository by inject()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var movies: List<MovieModel>? = null
    private var tvShows: List<TvShowModel>? = null
    private val verificationCollector = MockitoJUnit.collector()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var listMovieObserver: Observer<List<MovieModel>>
    @Mock
    lateinit var listTvShowObserver: Observer<List<TvShowModel>>


    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(appModules)
            androidContext(Mockito.mock(Context::class.java))
            module { viewModel { viewModel } }
        }
    }

    @Test
    fun getMovieList() {
        verificationCollector.assertLazily()
        viewModel.movies.observeForever(listMovieObserver)
        when (val value = runBlocking { dataRepository.getMovieList() }) {
            is UseCaseResult.Success<MovieResponse> -> movies = value.data.results
        }
        Mockito.verify(listMovieObserver).onChanged(movies)
    }

    @Test
    fun getTvShowList() {
        verificationCollector.assertLazily()
        viewModel.tvShows.observeForever(listTvShowObserver)
        when (val value = runBlocking { dataRepository.getTvShowList() }) {
            is UseCaseResult.Success<TvShowResponse> -> tvShows = value.data.results
        }
        Mockito.verify(listTvShowObserver).onChanged(tvShows)
    }
}