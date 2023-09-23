package com.movie.app.ui.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.movie.app.MovieRoute
import com.movie.app.data.model.GenreResponse
import com.movie.app.ui.widget.LoadingAnimation

@Composable
fun GenreScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GenreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StateViewHandle(
            navController = navController,
            state = uiState
        )
    }
}

@Composable
fun StateViewHandle(
    navController: NavController,
    state: GenreUiState,
) {
    when (state) {
        GenreUiState.Loading -> LoadingAnimation()
        GenreUiState.Empty -> Text(text = "No data available!")
        is GenreUiState.Error -> Text(text = state.message)
        is GenreUiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    GenreCard(data = GenreResponse.Genre(0, "POPULAR MOVIES"), onClick = {
                        navController.navigate(MovieRoute.MovieScreen.route)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(state.data) {
                    Spacer(modifier = Modifier.height(8.dp))
                    GenreCard(data = it, onClick = { genre ->
                        navController.navigate("${MovieRoute.MovieScreen.route}?genreId=${genre.id}")
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun GenreCard(
    data: GenreResponse.Genre,
    modifier: Modifier = Modifier,
    onClick: (GenreResponse.Genre) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onClick(data)
            },
        shape = RoundedCornerShape(30.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = data.name
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}