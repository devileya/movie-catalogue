package com.devileya.moviecatalogue.network.repository

import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.ApiInteractor
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.message.response.TvShowResponse
import com.devileya.moviecatalogue.network.message.response.VideoResponse
import com.devileya.moviecatalogue.utils.UseCaseResult

/**
 * Created by Arif Fadly Siregar 2019-11-01.
 */
class DataRepositoryImpl(private val apiInteractor: ApiInteractor): DataRepository {
    override suspend fun getMovieList(): UseCaseResult<MovieResponse> {
        return try {
            val data = apiInteractor.getMovieAsync().await()
            UseCaseResult.Success(data)
        } catch (e: Exception){
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvShowList(): UseCaseResult<TvShowResponse> {
        return try {
            val data = apiInteractor.getTvAsync().await()
            UseCaseResult.Success(data)
        } catch (e: java.lang.Exception){
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getVideo(movieId: String?, category: String?): UseCaseResult<VideoResponse> {
        return try {
            val data = apiInteractor.getVideoAsync(category, movieId).await()
            UseCaseResult.Success(data)
        } catch (e: java.lang.Exception){
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getReleaseMovie(currentDateGte: String?, currentDateLte: String?): UseCaseResult<MovieResponse> {
        return try {
            val data = apiInteractor.getReleaseMovieAsync(currentDateGte, currentDateLte).await()
            UseCaseResult.Success(data)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun searchMovie(keyWord: String?): UseCaseResult<MovieResponse> {
        return try {
            val data = apiInteractor.searchMovieAsync(keyWord).await()
            UseCaseResult.Success(data)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun searchTvShow(keyWord: String?): UseCaseResult<TvShowResponse> {
        return try {
            val data = apiInteractor.searchTVAsync(keyWord).await()
            UseCaseResult.Success(data)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}