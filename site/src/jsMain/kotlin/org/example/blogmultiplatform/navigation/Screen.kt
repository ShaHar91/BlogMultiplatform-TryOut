package org.example.blogmultiplatform.navigation

import org.example.blogmultiplatform.util.Constants.QUERY_PARAM

sealed class Screen(val route: String) {
    data object AdminHome: Screen(route = "/admin/")
    data object AdminLogin: Screen(route = "/admin/login")
    data object AdminCreate: Screen(route = "/admin/create")
    data object AdminMyPosts: Screen(route = "/admin/myposts"){
        fun searchByTitle(query: String) = "/admin/myposts?$QUERY_PARAM=$query"
    }
    data object AdminSuccess: Screen(route = "/admin/success")
}