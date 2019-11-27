package com.devileya.moviecatalogue.network.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devileya.moviecatalogue.database.dao.FavoriteDao
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.UseCaseResult
import timber.log.Timber

/**
 * Created by Arif Fadly Siregar 2019-11-08.
 */
class FavoriteRepositoryImpl(private val favoriteDao: FavoriteDao): FavoriteRepository {

    override suspend fun getMovies(): UseCaseResult<List<DetailModel>> {
        return try {
            val result = favoriteDao.getByCategory(DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getMovieById(id: String): UseCaseResult<DetailModel> {
        return try {
            val result = favoriteDao.getByIdCategory(id, DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvShows(): UseCaseResult<List<DetailModel>> {
        return try {
            val result = favoriteDao.getByCategory(DataEnum.TV.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvShowById(id: String): UseCaseResult<DetailModel> {
        return try {
            val result = favoriteDao.getByIdCategory(id, DataEnum.MOVIE.value)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun insert(detailModel: DetailModel) {
        Timber.d("masuk insert favorite")
        favoriteDao.insert(detailModel)
    }

    override suspend fun update(detailModel: DetailModel) {
        favoriteDao.update(detailModel)
    }

    override suspend fun delete(detailModel: DetailModel) {
        Timber.d("masuk delete favorite")
        favoriteDao.deleteById(detailModel.id)
    }

    override suspend fun getFavoriteById(id: String): UseCaseResult<DetailModel> {
        return try {
            Timber.d("masuk get favorite")
            val result = favoriteDao.getById(id)
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun get(): UseCaseResult<List<DetailModel>> {
        return try {
            val result = favoriteDao.get()
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getMoviesResources(): UseCaseResult<LiveData<PagedList<DetailModel>>> {
        return try {
            val result = favoriteDao.getByCategoryResource(DataEnum.MOVIE.value)
            val config = PagedList.Config.Builder()
                .setPageSize(20)
                .setInitialLoadSizeHint(20 * 2)
                .setEnablePlaceholders(false)
                .build()
            val data = LivePagedListBuilder<Int, DetailModel>(result, config).build()
            Timber.d("movie data ${data.value} ${result}")
            UseCaseResult.Success(data)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getTvResources(): UseCaseResult<LiveData<PagedList<DetailModel>>> {
        return try {
            val result = favoriteDao.getByCategoryResource(DataEnum.TV.value)
            val data = LivePagedListBuilder(result, 20).build()
            UseCaseResult.Success(data)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}