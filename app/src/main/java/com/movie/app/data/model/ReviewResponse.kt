package com.movie.app.data.model

import com.squareup.moshi.Json

data class ReviewResponse(
    val id: String,
    val page: Int,
    val results: List<Review>,
    @field:Json(name = "total_pages")
    val totalPages: Int,
    @field:Json(name = "total_results")
    val totalResults: Int
) {
    data class Review(
        val author: String,
        @field:Json(name = "author_details")
        val authorDetails: AuthorDetail,
        val content: String,
        @field:Json(name = "created_at")
        val createdAt: String,
        val id: String,
        @field:Json(name = "updated_at")
        val updatedAt: String,
        val url: String
    ) {
        data class AuthorDetail(
            val name: String,
            val username: String,
            @field:Json(name = "avatar_path")
            val avatarPath: String?,
            val rating: Int?
        )
    }
}
