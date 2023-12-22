package com.christiano.androidapp.navigation

import com.christiano.androidapp.models.Category as PostCategory

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Category : Screen("category_screen/{category}") {
        fun passCategory(category: PostCategory) = "category_screen/${category}"
    }

    object Details : Screen("details_screen/{postId}") {
        fun passPostId(id: String) = "details_screen/$id"
    }
}