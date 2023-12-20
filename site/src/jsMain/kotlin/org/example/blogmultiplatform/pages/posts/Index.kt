package org.example.blogmultiplatform.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.ErrorView
import org.example.blogmultiplatform.components.LoadingIndicator
import org.example.blogmultiplatform.components.OverflowSidePanel
import org.example.blogmultiplatform.models.ApiResponse
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.sections.FooterSection
import org.example.blogmultiplatform.sections.HeaderSection
import org.example.blogmultiplatform.utils.CommonConstants.POST_ID_PARAM
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.fetchSelectedPost
import org.example.blogmultiplatform.utils.maxLines
import org.example.blogmultiplatform.utils.parseDateString
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page("post")
@Composable
fun PostPage() {
    val context = rememberPageContext()
    var overflowOpened by remember { mutableStateOf(false) }
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()

    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    val hasPostIdParam = remember(context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(context.route) {
        if (hasPostIdParam) {
            val postId = context.route.params.getValue(POST_ID_PARAM)
            apiResponse = fetchSelectedPost(postId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowOpened = false },
                content = {
                    CategoryNavigationItems(
                        vertical = true
                    )
                }
            )
        }

        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = { overflowOpened = true }
        )

        when (apiResponse) {
            is ApiResponse.Error -> {
                ErrorView((apiResponse as ApiResponse.Error).message)
            }

            ApiResponse.Idle -> {
                LoadingIndicator()
            }

            is ApiResponse.Success -> {
                PostContent(post = (apiResponse as ApiResponse.Success).data)
                scope.launch {
                    delay(50)
                    try {
                        js("hljs.highlightAll()") as Unit
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            }
        }

        FooterSection()
    }
}

@Composable
fun PostContent(
    post: Post
) {
    LaunchedEffect(post) {
        (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML = post.content
    }

    Column(
        modifier = Modifier
            .margin(top = 50.px, bottom = 100.px, leftRight = 24.px)
            .fillMaxWidth()
            .maxWidth(800.px),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )

        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(FONT_FAMILY)
                .fontSize(40.px)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 20.px)
                .textOverflow(TextOverflow.Ellipsis)
                .color(Colors.Black)
                .maxLines(2),
            text = post.title
        )

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.px),
            src = post.thumbnail
        )

        Div(
            attrs = Modifier
                .id(Id.postContent)
                .fontFamily(FONT_FAMILY)
                .fillMaxWidth()
                .toAttrs()
        ) { }
    }
}