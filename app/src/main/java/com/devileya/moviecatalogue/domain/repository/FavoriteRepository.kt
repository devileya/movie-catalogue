package com.devileya.moviecatalogue.domain.repository

import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.UseCaseResult

/**
 * Created by Arif Fadly Siregar 2019-11-08.
 */
interface FavoriteRepository {
    suspend fun getMovies(): UseCaseResult<List<DetailModel>>
    suspend fun getMovieById(id: String): UseCaseResult<DetailModel>
    suspend fun getTvShows(): UseCaseResult<List<DetailModel>>
    suspend fun getTvShowById(id: String): UseCaseResult<DetailModel>
    suspend fun insert(detailModel: DetailModel)
    suspend fun update(detailModel: DetailModel)
    suspend fun delete(detailModel: DetailModel)
    suspend fun getFavoriteById(id: String): UseCaseResult<DetailModel>
}