package com.christiano.androidapp.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.christiano.androidapp.components.NavigationDrawer
import com.christiano.androidapp.components.PostCardsView
import com.christiano.androidapp.models.Category
import com.christiano.androidapp.models.Post
import com.christiano.androidapp.ui.theme.BlogMultiplatformTheme
import com.christiano.androidapp.util.RequestState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: RequestState<List<Post>>,
    searchedPosts: RequestState<List<Post>>,
    query: String,
    searchbarOpened: Boolean,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onSearchBarChanged: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onPostClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    NavigationDrawer(
        drawerState = drawerState,
        onCategorySelect = {
            scope.launch {
                drawerState.close()
            }
            onCategorySelect(it)
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Blog")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer Icon")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            onSearchBarChanged(true)
                            onActiveChange(true)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )

                if (searchbarOpened) {
                    SearchBar(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = onSearch,
                        active = active,
                        onActiveChange = onActiveChange,
                        leadingIcon = {
                            IconButton(onClick = {
                                onSearchBarChanged(false)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back Arrow Icon",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                onQueryChange("")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Icon",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Search here"
                            )
                        }
                    ) {
                        PostCardsView(
                            modifier = Modifier
                                .fillMaxSize(),
                            posts = searchedPosts,
                            onPostClick = onPostClick
                        )
                    }
                }
            }
        ) { paddingValues ->
            PostCardsView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                hideMessage = true,
                posts = posts,
                onPostClick = onPostClick
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    BlogMultiplatformTheme {
        HomeScreen(
            posts = RequestState.Idle,
            searchedPosts = RequestState.Idle,
            query = "",
            searchbarOpened = false,
            onSearchBarChanged = { },
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = {},
            onCategorySelect = {},
            onPostClick = {}
        )
    }
}