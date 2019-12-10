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

    @GET("https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.API_KEY}")
    fun getReleaseMovieAsync(@Query("primary_release_date.gte") currentDateGte: String?, @Query("primary_release_date.lte") currentDateLte: String?): Deferred<MovieResponse>

    @GET("https://api.themoviedb.org/3/search/movie?api_key=${BuildConfig.API_KEY}&language=en-US")
    fun searchMovieAsync(@Query("query") keyword: String?): Deferred<MovieResponse>

    @GET("https://api.themoviedb.org/3/search/tv?api_key=${BuildConfig.API_KEY}&language=en-US")
    fun searchTVAsync(@Query("query") keyword: String?): Deferred<TvShowResponse>
}