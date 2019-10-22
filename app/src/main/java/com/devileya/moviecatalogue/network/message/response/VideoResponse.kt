package com.devileya.moviecatalogue.network.message.response

import com.devileya.moviecatalogue.network.model.VideoModel

/**
 * Created by Arif Fadly Siregar 2019-10-23.
 */
data class VideoResponse (
    val id: Int? = 0,
    val results: List<VideoModel>? = null
)