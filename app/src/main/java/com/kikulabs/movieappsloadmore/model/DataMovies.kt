package com.kikulabs.movieappsloadmore.model

/**
 * Created by rizki kurniawan.
 **/

data class DataMovies(
    var id: Int = 0,
    var title: String? = null,
    var poster: String? = null,
    var backdrop: String? = null,
    var releaseDate: String? = null,
    var voteAverage: String? = null,
    var language: String? = null,
    var overview: String? = null
)