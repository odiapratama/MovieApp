package com.movie.app.ui.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.movie.app.MovieRoute
import com.movie.app.data.model.MovieResponse
import com.movie.app.data.model.ReviewResponse
import com.movie.app.ui.widget.LoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    movie: MovieResponse.Movie?,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults
        .exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = movie?.title.orEmpty())
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.ArrowBack),
                            contentDescription = "nav-back"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Image(
                    modifier = Modifier
                        .height(600.dp)
                        .fillMaxWidth(),
                    painter = rememberAsyncImagePainter(model = movie?.posterUrl),
                    contentScale = ContentScale.Crop,
                    contentDescription = "movie-image"
                )
            }
            item {
                Box {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = movie?.overview.orEmpty()
                    )
                }
            }

            when (uiState) {
                is MovieDetailUiState.Loading,
                is MovieDetailUiState.Empty,
                is MovieDetailUiState.Error -> {
                    item {
                        StateViewHandle(state = uiState)
                    }
                }

                is MovieDetailUiState.Success -> {
                    val reviews = uiState as MovieDetailUiState.Success
                    item {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            text = "Reviews"
                        )
                    }
                    items(reviews.data.results) {
                        CardReview(review = it)
                    }
                    if (reviews.data.totalPages > 1) {
                        item {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    navController.navigate(MovieRoute.ReviewScreen.route)
                                }) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    text = "SHOW MORE REVIEW",
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StateViewHandle(state: MovieDetailUiState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        when (state) {
            is MovieDetailUiState.Loading -> LoadingAnimation()
            is MovieDetailUiState.Empty -> Text(text = "No review available!")
            is MovieDetailUiState.Error -> Text(text = state.message)
            else -> Unit
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CardReview(
    modifier: Modifier = Modifier,
    review: ReviewResponse.Review
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = review.author
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(8.dp),
            text = review.content
        )
    }
}