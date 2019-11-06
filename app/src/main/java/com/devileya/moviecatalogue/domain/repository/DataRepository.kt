package com.devileya.moviecatalogue.domain.repository

import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.devileya.moviecatalogue.utils.UseCaseResult

/**
 * Created by Arif Fadly Siregar 2019-11-01.
 */
interface DataRepository {
    suspend fun getMovieList(): UseCaseResult<MovieResponse>
    suspend fun getTvShowList(): UseCaseResult<TvShowResponse>
    suspend fun getVideo(movieId: String?, category: String?): UseCaseResult<VideoResponse>
}