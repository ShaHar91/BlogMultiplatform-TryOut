package org.example.blogmultiplatform.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.OverflowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.sections.HeaderSection
import org.example.blogmultiplatform.sections.MainSection
import org.example.blogmultiplatform.sections.PostsSection
import org.example.blogmultiplatform.utils.CommonConstants.POSTS_PER_PAGE
import org.example.blogmultiplatform.utils.fetchLatestPosts
import org.example.blogmultiplatform.utils.fetchMainPosts

@Page
@Composable
fun HomePage() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var overflowMenuOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val latestPosts = remember { mutableStateListOf<SimplePost>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        fetchMainPosts(onSuccess = {
            mainPosts = it
        }, onError = {})

        fetchLatestPosts(
            skip = latestPostsToSkip,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    latestPosts.clear()
                    latestPosts.addAll(it.data)
                    latestPostsToSkip += it.data.count()
                    showMoreLatest = it.data.size >= POSTS_PER_PAGE
                }
            },
            onError = { println(it) }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowMenuOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowMenuOpened = false }
            ) {
                CategoryNavigationItems(true)
            }
        }

        HeaderSection(breakpoint) {
            overflowMenuOpened = true
        }

        MainSection(
            breakpoint = breakpoint,
            posts = mainPosts,
            onClick = {
                context.router.navigateTo(Screen.AdminHome.route)
            }
        )

        PostsSection(
            breakpoint = breakpoint,
            posts = latestPosts,
            title = "Latest Posts",
            showMoreVisibility = showMoreLatest,
            onShowMore = {
                scope.launch {
                    fetchLatestPosts(
                        skip = latestPostsToSkip,
                        onSuccess = {
                            if (it is ApiListResponse.Success) {
                                if (it.data.isNotEmpty()) {
                                    latestPosts.addAll(it.data)
                                    latestPostsToSkip += it.data.count()
                                    if (it.data.size < POSTS_PER_PAGE) showMoreLatest = false
                                } else {
                                    showMoreLatest = false
                                }
                            }
                        },
                        onError = { println(it) })

                }
            },
            onClick = {
                context.router.navigateTo(Screen.AdminHome.route)
            }
        )
    }
}
