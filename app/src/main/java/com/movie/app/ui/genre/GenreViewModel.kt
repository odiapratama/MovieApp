package com.movie.app.ui.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.app.data.model.GenreResponse
import com.movie.app.domain.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenreUiState>(GenreUiState.Loading)
    val uiState: StateFlow<GenreUiState>
        get() = _uiState

    init {
        getGenre()
    }

    private fun getGenre() {
        viewModelScope.launch {
            try {
                val response = movieUseCase.getGenre()
                if (response.genres.isNotEmpty()) _uiState.emit(GenreUiState.Success(response.genres))
                else _uiState.emit(GenreUiState.Empty)
            } catch (e: Exception) {
                _uiState.emit(GenreUiState.Error(e.localizedMessage ?: ""))
            }
        }
    }
}

sealed interface GenreUiState {
    object Loading : GenreUiState

    object Empty : GenreUiState

    data class Success(
        val data: List<GenreResponse.Genre>
    ) : GenreUiState

    data class Error(
        val message: String
    ) : GenreUiState
}