package com.devileya.moviecatalogue.domain.module

import com.devileya.moviecatalogue.database.AppDatabase
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.domain.repository.FavoriteRepository
import com.devileya.moviecatalogue.network.ApiInteractor
import com.devileya.moviecatalogue.network.repository.DataRepositoryImpl
import com.devileya.moviecatalogue.network.repository.FavoriteRepositoryImpl
import com.devileya.moviecatalogue.ui.detail.DetailViewModel
import com.devileya.moviecatalogue.ui.main.MainFragmentViewModel
import com.devileya.moviecatalogue.ui.main.favorite.FavoriteViewModel
import com.devileya.moviecatalogue.ui.main.search.SearchViewModel
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Arif Fadly Siregar 2019-10-31.
 */
const val URL = "https://api.themoviedb.org/3/discover/"

val appModules =  module {
    single {
        createWebService<ApiInteractor>(
            okHttpClient = createHttpClient(),
            baseUrl = URL)
    }

    factory { AppDatabase.getAppDataBase(androidContext())?.favoriteDao() }

    // Main Fragment
    factory<DataRepository> { DataRepositoryImpl(get()) }
    viewModel { MainFragmentViewModel(get(), get()) }

    // Detail Activity
    factory<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    viewModel { DetailViewModel(get(), get()) }

    // Favorite
    viewModel { FavoriteViewModel(get()) }

    // Search
    viewModel { SearchViewModel(get(), get()) }
}

fun createHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
    client.readTimeout(5 * 60, TimeUnit.SECONDS)
    return client.addInterceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder.header("Content-Type", "application/json")
        val request = requestBuilder.method(original.method(), original.body()).build()
        return@addInterceptor it.proceed(request)
    }.build()
}

inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient, baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()
    return retrofit.create(T::class.java)
}