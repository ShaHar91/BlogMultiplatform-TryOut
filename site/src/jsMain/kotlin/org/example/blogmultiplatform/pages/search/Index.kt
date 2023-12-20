package org.example.blogmultiplatform.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.LoadingIndicator
import org.example.blogmultiplatform.components.OverflowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.sections.FooterSection
import org.example.blogmultiplatform.sections.HeaderSection
import org.example.blogmultiplatform.sections.PostsSection
import org.example.blogmultiplatform.utils.CommonConstants.CATEGORY_PARAM
import org.example.blogmultiplatform.utils.CommonConstants.POSTS_PER_PAGE
import org.example.blogmultiplatform.utils.CommonConstants.QUERY_PARAM
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.searchPostsByCategory
import org.example.blogmultiplatform.utils.searchPostsByTitle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement

@Page("query")
@Composable
fun SearchPage() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()

    var apiResponse by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    var overflowOpened by remember { mutableStateOf(false) }

    var postsToSkip by remember(context.route) { mutableStateOf(0) }
    val searchedPosts = remember { mutableStateListOf<SimplePost>() }
    var showMorePosts by remember(context.route) { mutableStateOf(false) }

    val hasCategoryParam = remember(context.route) {
        context.route.params.containsKey(CATEGORY_PARAM)
    }
    val hasQueryParam = remember(context.route) {
        context.route.params.containsKey(QUERY_PARAM)
    }

    val value = remember(context.route) {
        if (hasCategoryParam) {
            context.route.params.getValue(CATEGORY_PARAM)
        } else if (hasQueryParam) {
            context.route.params.getValue(QUERY_PARAM)
        } else {
            ""
        }
    }

    LaunchedEffect(context.route) {
        if (hasCategoryParam) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = ""

            searchPostsByCategory(
                category = Category.parseName(value),
                skip = postsToSkip,
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += response.data.count()
                        showMorePosts = response.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = { println(it) }
            )
        }

        if (hasQueryParam) {
            (document.getElementById(Id.adminSearchBar) as HTMLInputElement).value = value

            searchPostsByTitle(
                query = value,
                skip = postsToSkip,
                onSuccess = { response ->
                    apiResponse = response
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += response.data.count()
                        showMorePosts = response.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = { println(it) }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowOpened = false },
                content = {
                    CategoryNavigationItems(
                        selectedCategory = if (hasCategoryParam) Category.parseName(value) else null,
                        vertical = true
                    )
                }
            )
        }

        HeaderSection(
            breakpoint = breakpoint,
            selectedCategory = if (hasCategoryParam) Category.parseName(value) else null,
            onMenuOpen = { overflowOpened = true }
        )

        if (apiResponse is ApiListResponse.Success) {

            if (hasCategoryParam) {
                SpanText(
                    modifier = Modifier
                        .margin(top = 100.px, bottom = 20.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(36.px)
                        .fillMaxWidth()
                        .textAlign(TextAlign.Center),
                    text = Category.parseName(value).name
                )
            }

            PostsSection(
                breakpoint = breakpoint,
                posts = searchedPosts,
                showMoreVisibility = showMorePosts,
                onShowMore = {
                    scope.launch {
                        if (hasCategoryParam) {
                            searchPostsByCategory(
                                category = Category.parseName(value),
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += response.data.count()
                                            showMorePosts = response.data.size >= POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = { println(it) }
                            )
                        }

                        if (hasQueryParam) {
                            searchPostsByTitle(
                                query = value,
                                skip = postsToSkip,
                                onSuccess = { response ->
                                    if (response is ApiListResponse.Success) {
                                        if (response.data.isNotEmpty()) {
                                            searchedPosts.addAll(response.data)
                                            postsToSkip += response.data.count()
                                            showMorePosts = response.data.size >= POSTS_PER_PAGE
                                        } else {
                                            showMorePosts = false
                                        }
                                    }
                                },
                                onError = { println(it) }
                            )
                        }
                    }
                },
                onClick = {
                    context.router.navigateTo(Screen.PostPage.getPost(it))
                }
            )
        } else {
            LoadingIndicator()
        }

        FooterSection()
    }
}