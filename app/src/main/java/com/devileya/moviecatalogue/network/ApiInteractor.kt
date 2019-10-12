package com.devileya.moviecatalogue.network

import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInteractor {
    @GET("movie")
    suspend fun getMovieAsync(@Query("api_key") api_key: String): MovieResponse

    @GET("tv")
    suspend fun getTvAsync(@Query("api_key") api_key: String): TvShowResponse
}