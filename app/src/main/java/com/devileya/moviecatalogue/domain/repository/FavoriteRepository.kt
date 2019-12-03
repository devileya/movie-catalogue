package com.devileya.moviecatalogue.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
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
    suspend fun get(): UseCaseResult<List<DetailModel>>
    fun getMoviesResources(): DataSource.Factory<Int, DetailModel>
    fun getTvResources(): UseCaseResult<LiveData<PagedList<DetailModel>>>
}