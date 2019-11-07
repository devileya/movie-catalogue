package com.devileya.moviecatalogue.network.repository

import com.devileya.moviecatalogue.database.AppDatabase
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.UseCaseResult
import timber.log.Timber

/**
 * Created by Arif Fadly Siregar 2019-11-08.
 */
class FavoriteRepositoryImpl(private val appDatabase: AppDatabase): FavoriteRepository {

    override suspend fun getMovies(): UseCaseResult<List<DetailModel>> {
        return try {
            val result = appDatabase.favoriteDao().getByCategory(DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getMovieById(id: String): UseCaseResult<DetailModel> {
        return try {
            val result = appDatabase.favoriteDao().getByIdCategory(id, DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvShows(): UseCaseResult<List<DetailModel>> {
        return try {
            val result = appDatabase.favoriteDao().getByCategory(DataEnum.TV.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvShowById(id: String): UseCaseResult<DetailModel> {
        return try {
            val result = appDatabase.favoriteDao().getByIdCategory(id, DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun insert(detailModel: DetailModel) {
        Timber.d("masuk insert favorite")
        appDatabase.favoriteDao().insert(detailModel)
    }

    override suspend fun update(detailModel: DetailModel) {
        appDatabase.favoriteDao().update(detailModel)
    }

    override suspend fun delete(detailModel: DetailModel) {
        Timber.d("masuk delete favorite")
        appDatabase.favoriteDao().delete(detailModel)
    }

    override suspend fun getFavoriteById(id: String): UseCaseResult<DetailModel> {
        return try {
            Timber.d("masuk get favorite")
            val result = appDatabase.favoriteDao().getById(id)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}