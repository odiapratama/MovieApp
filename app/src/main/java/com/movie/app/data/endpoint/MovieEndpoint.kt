package com.movie.app.data.endpoint

import com.movie.app.data.model.GenreResponse
import com.movie.app.data.model.MovieResponse
import com.movie.app.data.model.ReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieEndpoint {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovie(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenreMovie(
        @Query("language") language: String = "en-US",
    ): GenreResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int = 1,
        @Query("with_genres") genreId: String
    ): MovieResponse

    @GET("movie/{movieId}/reviews")
    suspend fun getReview(
        @Path("movieId") movieId: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): ReviewResponse
}