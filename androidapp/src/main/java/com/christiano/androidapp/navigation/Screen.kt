package com.christiano.androidapp.navigation

import com.christiano.androidapp.util.Constants.CATEGORY_ARGUMENT
import com.christiano.androidapp.util.Constants.POST_ID_ARGUMENT
import org.example.blogmultiplatform.Category as PostCategory

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Category : Screen("category_screen/{$CATEGORY_ARGUMENT}") {
        fun passCategory(category: PostCategory) = "category_screen/${category}"
    }

    data object Details : Screen("details_screen/{$POST_ID_ARGUMENT}") {
        fun passPostId(id: String) = "details_screen/$id"
    }
}