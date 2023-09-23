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
import com.movie.app.ui.genre.GenreDetailScreen
import com.movie.app.ui.genre.GenreScreen
import com.movie.app.ui.movie.MovieDetailScreen
import com.movie.app.ui.movie.MovieScreen
import com.movie.app.ui.theme.MovieAppTheme
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
                            MovieScreen(navController = navController, genreId = it.arguments?.getString("genreId") ?: "")
                        }
                        composable(MovieRoute.MovieDetailScreen.route) {
                            MovieDetailScreen()
                        }
                        composable(MovieRoute.GenreDetailScreen.route) {
                            GenreDetailScreen()
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
    GenreDetailScreen("genre-detail")
}