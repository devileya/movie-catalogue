package com.devileya.moviecatalogue.network

import com.devileya.moviecatalogue.BuildConfig
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInteractor {
    @GET("movie?api_key=${BuildConfig.API_KEY}")
    fun getMovieAsync(): Deferred<MovieResponse>

    @GET("tv?api_key=${BuildConfig.API_KEY}")
    fun getTvAsync(): Deferred<TvShowResponse>

    @GET("https://api.themoviedb.org/3/{category}/{movieId}/videos?api_key=${BuildConfig.API_KEY}&language=en-US")
    fun getVideoAsync(@Path("category") category: String?,
                              @Path("movieId") movieId: String?): Deferred<VideoResponse>
}