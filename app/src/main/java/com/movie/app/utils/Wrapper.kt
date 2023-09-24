package com.movie.app.utils

import com.squareup.moshi.Moshi

inline fun <reified T> T.toJson(): String? {
    return try {
        Moshi.Builder().build()
            .adapter(T::class.java)
            .toJson(this)
    } catch (_ : Exception) {
        null
    }
}

inline fun <reified T> String.fromJson() : T? {
    return try {
        Moshi.Builder().build()
            .adapter(T::class.java)
            .lenient()
            .fromJson(this)
    } catch (_: Exception) {
        null
    }
}