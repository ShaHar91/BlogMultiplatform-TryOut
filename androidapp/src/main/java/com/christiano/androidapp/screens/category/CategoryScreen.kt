package com.christiano.androidapp.screens.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.christiano.androidapp.components.PostCardsView
import org.example.blogmultiplatform.Category
import com.christiano.androidapp.models.Post
import com.christiano.androidapp.ui.theme.BlogMultiplatformTheme
import com.christiano.androidapp.util.RequestState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: Category,
    posts: RequestState<List<Post>>,
    onBackPress: () -> Unit,
    onPostClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackPress() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Arrow Icon")
                    }
                }
            )
        }
    ) { paddingValues ->
        PostCardsView(
            modifier = Modifier.padding(paddingValues),
            posts = posts,
            onPostClick = onPostClick
        )
    }
}

@Preview
@Composable
fun CategoryScreenPreview() {
    BlogMultiplatformTheme {
//        CategoryScreen()
    }
}