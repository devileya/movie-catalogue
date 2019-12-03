package com.devileya.topmovie.database.model

data class TvShowModel (
    val original_name: String,
    val genre_ids: List<String>,
    val name: String,
    val popularity: String,
    val origin_country: List<String>,
    val vote_count: String,
    val first_air_date: String,
    val backdrop_path: String,
    val original_language: String,
    val id: String,
    val vote_average: String,
    val overview: String,
    val poster_path: String
)