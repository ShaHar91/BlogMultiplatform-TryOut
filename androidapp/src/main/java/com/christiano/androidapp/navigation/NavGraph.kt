package com.christiano.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.christiano.androidapp.models.Category
import com.christiano.androidapp.screens.category.CategoryScreen
import com.christiano.androidapp.screens.category.CategoryViewModel
import com.christiano.androidapp.screens.details.DetailsScreen
import com.christiano.androidapp.screens.home.HomeScreen
import com.christiano.androidapp.screens.home.HomeViewModel

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel()
            var query by remember { mutableStateOf("") }
            var searchbarOpened by remember { mutableStateOf(false) }
            var active by remember { mutableStateOf(false) }
            HomeScreen(
                posts = viewModel.allPosts.value,
                searchedPosts = viewModel.searchedPosts.value,
                query = query,
                searchbarOpened = searchbarOpened,
                onSearchBarChanged = {
                    searchbarOpened = it
                    if (!searchbarOpened) {
                        query = ""
                        active = false
                        viewModel.resetSearchPosts()
                    }
                },
                onQueryChange = { query = it },
                onSearch = viewModel::searchPostsByTitle,
                active = active,
                onActiveChange = {
                    active = it
                    if (!active) {
                        query = ""
                        searchbarOpened = false
                        viewModel.resetSearchPosts()
                    }
                },
                onCategorySelect = {
                    navController.navigate(Screen.Category.passCategory(it))
                },
                onPostClick = { navController.navigate(Screen.Details.passPostId(it)) }
            )
        }
        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
            })
        ) {
            val viewModel: CategoryViewModel = viewModel()
            val selectedCategory = it.arguments?.getString("category") ?: Category.Programming.name

            CategoryScreen(
                posts = viewModel.categoryPosts.value,
                category = Category.parseName(selectedCategory),
                onBackPress = { navController.popBackStack() },
                onPostClick = { navController.navigate(Screen.Details.passPostId(it)) }
            )
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.StringType
                }
            )
        ) {
            val postId = it.arguments?.getString("postId")
            DetailsScreen(
                url = "http://10.0.2.2:8080/posts/post?postId=$postId",
                onBackPress = { navController.popBackStack() }
            )
        }
    }
}