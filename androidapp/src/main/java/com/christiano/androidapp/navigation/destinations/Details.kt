package com.christiano.androidapp.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.christiano.androidapp.navigation.Screen
import com.christiano.androidapp.screens.details.DetailsScreen
import com.christiano.androidapp.util.Constants.POST_ID_ARGUMENT

fun NavGraphBuilder.detailsRoute(
    onBackPress: () -> Unit
) {
    composable(
        route = Screen.Details.route,
        arguments = listOf(
            navArgument(POST_ID_ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) {
        val postId = it.arguments?.getString(POST_ID_ARGUMENT)
        DetailsScreen(
            url = "http://10.0.2.2:8081/posts/post?$POST_ID_ARGUMENT=$postId&showSections=false",
            onBackPress = onBackPress
        )
    }
}