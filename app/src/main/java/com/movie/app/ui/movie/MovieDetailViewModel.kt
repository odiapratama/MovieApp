package com.movie.app.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.data.model.MovieResponse
import com.movie.app.data.model.ReviewResponse
import com.movie.app.domain.MovieUseCase
import com.movie.app.utils.fromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    val movie = (savedStateHandle["movie"] ?: "").fromJson<MovieResponse.Movie>()

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState>
        get() = _uiState

    init {
        getMovieReview((movie?.id ?: 0).toString())
    }

    private fun getMovieReview(movieId: String) {
        viewModelScope.launch {
            try {
                val response = movieUseCase.getMovieReviews(1, movieId)
                if (response.results.isNotEmpty()) _uiState.emit(MovieDetailUiState.Success(response))
                else _uiState.emit(MovieDetailUiState.Empty)
            } catch (e: Exception) {
                _uiState.emit(MovieDetailUiState.Error(e.localizedMessage ?: ""))
            }
        }
    }
}

sealed interface MovieDetailUiState {

    object Loading : MovieDetailUiState

    object Empty : MovieDetailUiState

    data class Success(
        val data: ReviewResponse
    ) : MovieDetailUiState

    data class Error(
        val message: String
    ) : MovieDetailUiState
}