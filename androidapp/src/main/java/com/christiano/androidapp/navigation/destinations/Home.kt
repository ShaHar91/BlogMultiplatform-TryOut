package com.christiano.androidapp.navigation.destinations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.example.blogmultiplatform.Category
import com.christiano.androidapp.navigation.Screen
import com.christiano.androidapp.screens.home.HomeScreen
import com.christiano.androidapp.screens.home.HomeViewModel

fun NavGraphBuilder.homeRoute(
    onCategorySelect: (Category) -> Unit,
    onPostClick: (String) -> Unit
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
            onCategorySelect = onCategorySelect,
            onPostClick = onPostClick
        )
    }
}