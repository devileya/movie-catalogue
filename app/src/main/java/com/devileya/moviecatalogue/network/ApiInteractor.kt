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
    @GET("movie")
    suspend fun getMovieAsync(@Query("api_key") api_key: String): MovieResponse

    @GET("tv")
    suspend fun getTvAsync(@Query("api_key") api_key: String): TvShowResponse

    @GET("https://api.themoviedb.org/3/{category}/{movieId}/videos?api_key=${BuildConfig.API_KEY}&language=en-US")
    suspend fun getVideoAsync(@Path("category") category: String?,
                              @Path("movieId") movieId: String?): VideoResponse
}