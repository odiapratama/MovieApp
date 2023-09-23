package com.movie.app.ui.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.data.model.MovieResponse
import com.movie.app.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    private val genreId: String = checkNotNull(savedStateHandle["genreId"])

    var totalPages = 1
    private val movies = arrayListOf<MovieResponse.Movie>()

    private val _uiState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val uiState: StateFlow<MovieUiState>
        get() = _uiState

    init {
        if (genreId.isNotBlank()) getMoviesByGenre(genreId)
        else getNowPlayingMovie()
    }

    private fun getNowPlayingMovie() {
        movies.clear()
        viewModelScope.launch {
            try {
                val response = movieUseCase.getNowPlayingMovie(page = 1)
                if (response.results.isNotEmpty()) {
                    totalPages = response.totalPages
                    movies.addAll(response.results)
                    _uiState.emit(MovieUiState.Success(data = movies))
                } else _uiState.emit(MovieUiState.Empty)
            } catch (e: Exception) {
                _uiState.emit(MovieUiState.Error(e.localizedMessage ?: "Error Service"))
            }
        }
    }

    private fun getMoviesByGenre(genreId: String) {
        movies.clear()
        viewModelScope.launch {
            try {
                val response = movieUseCase.getMoviesByGenre(page = 1, genreId = genreId)
                if (response.results.isNotEmpty()) {
                    totalPages = response.totalPages
                    movies.addAll(response.results)
                    _uiState.emit(MovieUiState.Success(data = movies))
                } else _uiState.emit(MovieUiState.Empty)
            } catch (e: Exception) {
                _uiState.emit(MovieUiState.Error(e.localizedMessage ?: "Error Service"))
            }
        }
    }

    fun getDataPagination(page: Int, genreId: String = "") {
        viewModelScope.launch {
            _uiState.emit(MovieUiState.Success(data = movies, isPageLoading = true))
            try {
                val response = if (genreId.isNotBlank()) movieUseCase.getMoviesByGenre(page, genreId)
                else movieUseCase.getNowPlayingMovie(page)
                if (response.results.isNotEmpty()) {
                    movies.addAll(response.results)
                    _uiState.emit(MovieUiState.Success(data = movies + response.results))
                } else _uiState.emit(MovieUiState.Empty)
            } catch (e: Exception) {
                _uiState.emit(MovieUiState.Error(e.localizedMessage ?: "Error Service"))
            }
        }
    }
}

sealed interface MovieUiState {

    object Loading : MovieUiState

    object Empty : MovieUiState

    data class Success(
        val data: List<MovieResponse.Movie> = emptyList(),
        val isPageLoading: Boolean = false
    ) : MovieUiState

    data class Error(
        val message: String
    ) : MovieUiState
}