package com.movie.app.domain

import com.movie.app.data.model.GenreResponse
import com.movie.app.data.model.MovieResponse
import com.movie.app.data.model.ReviewResponse

interface MovieUseCase {
    suspend fun getNowPlayingMovie(page: Int): MovieResponse
    suspend fun getGenre(): GenreResponse
    suspend fun getMoviesByGenre(page: Int, genreId: String): MovieResponse
    suspend fun getMovieReviews(page: Int, movieId: String): ReviewResponse
}