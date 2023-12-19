package org.example.blogmultiplatform.navigation

import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.utils.CommonConstants.CATEGORY_PARAM
import org.example.blogmultiplatform.utils.CommonConstants.POST_ID_PARAM
import org.example.blogmultiplatform.utils.CommonConstants.QUERY_PARAM

sealed class Screen(val route: String) {
    data object AdminHome : Screen(route = "/admin/")
    data object AdminLogin : Screen(route = "/admin/login")
    data object AdminCreate : Screen(route = "/admin/create") {
        fun passPostId(id: String) = "/admin/create?$POST_ID_PARAM=$id"
    }

    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String) = "/admin/myposts?$QUERY_PARAM=$query"
    }

    data object AdminSuccess : Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?updated=true"
    }

    data object HomePage: Screen(route = "/")

    data object SearchPage: Screen(route = "/search/query") {
        fun searchByTitle(query: String) = "/search/query?$QUERY_PARAM=$query"
        fun searchByCategory(category: Category)= "/search/query?$CATEGORY_PARAM=${category.name}"
    }
}