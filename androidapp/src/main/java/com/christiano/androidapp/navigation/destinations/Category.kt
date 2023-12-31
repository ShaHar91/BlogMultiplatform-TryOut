package com.christiano.androidapp.navigation.destinations

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.example.blogmultiplatform.Category
import com.christiano.androidapp.navigation.Screen
import com.christiano.androidapp.screens.category.CategoryScreen
import com.christiano.androidapp.screens.category.CategoryViewModel
import com.christiano.androidapp.util.Constants.CATEGORY_ARGUMENT
import org.example.blogmultiplatform.parseCategoryName

fun NavGraphBuilder.categoryRoute(
    onBackPress: () -> Unit,
    onPostClick: (String) -> Unit
) {
    composable(
        route = Screen.Category.route,
        arguments = listOf(navArgument(CATEGORY_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val viewModel: CategoryViewModel = viewModel()
        val selectedCategory = it.arguments?.getString(CATEGORY_ARGUMENT) ?: Category.Programming.name

        CategoryScreen(
            posts = viewModel.categoryPosts.value,
            category = selectedCategory.parseCategoryName(),
            onBackPress = onBackPress,
            onPostClick = onPostClick
        )
    }
}