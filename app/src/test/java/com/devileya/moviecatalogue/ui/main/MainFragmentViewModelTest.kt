package com.devileya.moviecatalogue.ui.main

import androidx.lifecycle.MutableLiveData
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by Arif Fadly Siregar 2019-10-27.
 */
class MainFragmentViewModelTest {

    private lateinit var viewModel: MainFragmentViewModel
    private val dummyTvShows =  MutableLiveData<List<TvShowModel>>()
    private val dummyMovies =  MutableLiveData<List<MovieModel>>()
    private val showLoading = MutableLiveData<Boolean>()

    @Before
    fun setUp() { viewModel = MainFragmentViewModel() }

    @Test
    fun getTvShows() {
//        val data = mutableListOf<TvShowModel>(
//            TvShowModel(
//                original_name = "Arrow",
//                overview = "Spoiled billionaire playboy Oliver Queen is missing and presumed dead when his yacht is lost at sea. He returns five years later a changed man, determined to clean up the city as a hooded vigilante armed with a bow.",
//                genre_ids = listOf("80", "18", "9648", "10759"),
//                original_language = "en",
//                backdrop_path = "/dKxkwAJfGuznW8Hu0mhaDJtna0n.jpg",
//                id = "1412",
//                poster_path = "/mo0FP1GxOFZT4UDde7RFDz5APXF.jpg",
//                vote_average = "5.8",
//                vote_count = "2729",
//                popularity = "365.616",
//                first_air_date = "2012-10-10",
//                name = "Arrow",
//                origin_country = listOf("US")
//        ))
//        dummyTvShows.postValue(data)
//        `when`(viewModel.tvShows).thenReturn(dummyTvShows)
//        assertEquals(dummyTvShows, viewModel.tvShows)
    }

    @Test
    fun getMovies() {
//        val data = mutableListOf<MovieModel>(
//            MovieModel(
//                popularity = "513.78",
//                vote_count = "4120",
//                vote_average = "8.6",
//                poster_path = "/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
//                id = "475557",
//                backdrop_path = "/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
//                original_language = "en",
//                genre_ids = listOf("80", "18", "53"),
//                overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
//                title = "Joker",
//                release_date = "2019-10-04",
//                adult = "false",
//                video = false,
//                orginal_title = "Joker"
//            )
//        )
//        dummyMovies.postValue(data)
//        `when`(viewModel.movies).thenReturn(dummyMovies)
//        assertEquals(dummyMovies, viewModel.movies)
    }
}