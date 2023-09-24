package com.movie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.movie.app.data.model.MovieResponse
import com.movie.app.ui.genre.GenreDetailScreen
import com.movie.app.ui.genre.GenreScreen
import com.movie.app.ui.movie.MovieDetailScreen
import com.movie.app.ui.movie.MovieScreen
import com.movie.app.ui.review.ReviewScreen
import com.movie.app.ui.theme.MovieAppTheme
import com.movie.app.utils.fromJson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = MovieRoute.GenreScreen.route
                    ) {
                        composable(MovieRoute.GenreScreen.route) {
                            GenreScreen(navController = navController)
                        }
                        composable(
                            route = "${MovieRoute.MovieScreen.route}?genreId={genreId}",
                            arguments = listOf(navArgument("genreId") { defaultValue = "" })
                        ) {
                            MovieScreen(navController = navController, genreId = it.arguments?.getString("genreId").orEmpty())
                        }
                        composable(
                            route = "${MovieRoute.MovieDetailScreen.route}?movie={movie}",
                            arguments = listOf(navArgument("movie") { defaultValue = ""})
                        ) {
                            val dataJson = it.arguments?.getString("movie").orEmpty().fromJson<MovieResponse.Movie>()
                            MovieDetailScreen(navController = navController, movie = dataJson)
                        }
                        composable(MovieRoute.GenreDetailScreen.route) {
                            GenreDetailScreen()
                        }
                        composable(MovieRoute.ReviewScreen.route) {
                            ReviewScreen()
                        }
                    }
                }
            }
        }
    }
}

enum class MovieRoute(val route: String) {
    MovieScreen("movie"),
    MovieDetailScreen("movie-detail"),
    GenreScreen("genre"),
    GenreDetailScreen("genre-detail"),
    ReviewScreen("review")
}