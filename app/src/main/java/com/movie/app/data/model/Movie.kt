package com.movie.app.data.model

import com.squareup.moshi.Json

data class MovieResponse(
    val dates: Dates,
    val page: Int,
    val results: List<Movie>,
    @field:Json(name = "total_pages")
    val totalPages: Int
) {

    data class Dates(
        val maximum: String,
        val minimum: String
    )

    data class Movie(
        @field:Json(name = "adult")
        val adult: Boolean?,
        @field:Json(name = "backdrop_path")
        val backDropPath: String?,
        @field:Json(name = "genre_ids")
        val genreIds: List<Int>?,
        @field:Json(name = "id")
        val id: Int?,
        @field:Json(name = "original_language")
        val originalLanguage: String?,
        @field:Json(name = "original_title")
        val originalTitle: String?,
        @field:Json(name = "overview")
        val overview: String?,
        @field:Json(name = "popularity")
        val popularity: Double?,
        @field:Json(name = "poster_path")
        val posterPath: String?,
        @field:Json(name = "release_date")
        val releaseDate: String?,
        @field:Json(name = "title")
        val title: String?,
        @field:Json(name = "video")
        val video: Boolean?,
        @field:Json(name = "vote_average")
        val voteAverage: Double?,
        @field:Json(name = "vote_count")
        val voteCount: Int?
    ) {
        val posterUrl: String
            get() = "https://image.tmdb.org/t/p/original${posterPath}"
    }
}
