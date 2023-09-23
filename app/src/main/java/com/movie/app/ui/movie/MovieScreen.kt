package com.movie.app.ui.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.movie.app.MovieRoute
import com.movie.app.data.model.MovieResponse
import com.movie.app.ui.widget.LoadingAnimation
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MovieScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    genreId: String = "",
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movieUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val page = remember {
        mutableStateOf(1)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (movieUiState) {
            is MovieUiState.Loading,
            MovieUiState.Empty,
            is MovieUiState.Error -> {
                StateViewHandle(state = movieUiState)
            }

            is MovieUiState.Success -> {
                ListMovie(
                    state = movieUiState,
                    onLoadMore = {
                        if (page.value < viewModel.totalPages) {
                            page.value++
                            if (genreId.isNotBlank()) viewModel.getDataPagination(page.value, genreId)
                            else viewModel.getDataPagination(page.value)
                        }
                    },
                    onClick = {
                        navController.navigate(MovieRoute.MovieDetailScreen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun StateViewHandle(state: MovieUiState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is MovieUiState.Loading -> LoadingAnimation()
            MovieUiState.Empty -> Text(text = "No data available...")
            is MovieUiState.Error -> Text(text = (state).message)
            else -> Unit
        }
    }
}

@Composable
fun ListMovie(
    state: MovieUiState,
    onLoadMore: () -> Unit,
    onClick: (MovieResponse.Movie) -> Unit
) {
    val listState = rememberLazyGridState()
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 200.dp),
        state = listState
    ) {
        when (state) {
            is MovieUiState.Success -> {
                items(state.data) { data ->
                    CardMovie(
                        modifier = Modifier.padding(8.dp),
                        movie = data
                    ) {
                        onClick(data)
                    }
                }
                if (state.isPageLoading) {
                    items(
                        count = 1,
                        span = { GridItemSpan(2) }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(50.dp)
                                .height(50.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingAnimation()
                        }
                    }
                }
            }

            else -> Unit
        }
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}

@Composable
fun CardMovie(
    modifier: Modifier = Modifier,
    movie: MovieResponse.Movie,
    onClick: (MovieResponse.Movie) -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClick(movie)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Spacer(modifier = Modifier.height(8.dp))
            val painter = rememberAsyncImagePainter(
                model = movie.posterUrl
            )
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.originalTitle.orEmpty(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.overview.orEmpty(),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun InfiniteListHandler(
    listState: LazyGridState,
    buffer: Int = 0,
    onLoadMore: () -> Unit
) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 2
            lastVisibleIndex > (totalItems - buffer)
        }
    }

    LaunchedEffect(loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                onLoadMore()
            }
    }
}