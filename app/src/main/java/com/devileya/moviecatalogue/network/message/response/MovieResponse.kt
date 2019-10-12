package com.devileya.moviecatalogue.network.message.response

import com.devileya.moviecatalogue.network.model.MovieModel

data class MovieResponse (
    val page: String,
    val total_results: String,
    val total_pages: String,
    val results: List<MovieModel>
)