package com.devileya.moviecatalogue.network.message.response

import com.devileya.moviecatalogue.network.model.TvShowModel

data class TvShowResponse (
    val page: String,
    val total_results: String,
    val total_pages: String,
    val results: List<TvShowModel>
)