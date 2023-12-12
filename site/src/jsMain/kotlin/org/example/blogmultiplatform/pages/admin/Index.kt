package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import org.example.blogmultiplatform.util.isUserLoggedIn

@Page
@Composable
fun HomePage() {
    isUserLoggedIn {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    println("Admin Home Page")
}