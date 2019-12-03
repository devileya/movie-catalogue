package com.devileya.topmovie.database.model

data class MovieModel (
    val popularity: String?,
    val vote_count: String?,
    val video: Boolean?,
    val poster_path: String?,
    val id: String?,
    val adult: String?,
    val backdrop_path: String?,
    val original_language: String?,
    val orginal_title: String?,
    val genre_ids: List<String>?,
    val title: String?,
    val vote_average: String?,
    val overview: String?,
    val release_date: String?
)