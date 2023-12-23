package org.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.example.blogmultiplatform.components.PostPreview
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.models.Theme
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(
    posts: ApiListResponse,
    breakpoint: Breakpoint,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(1920.px),
            contentAlignment = Alignment.Center
        ) {
            when (posts) {
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPosts(breakpoint, posts.data, onClick)
                }

                is ApiListResponse.Error -> {}
            }
        }
    }
}

@Composable
fun MainPosts(
    breakpoint: Breakpoint,
    posts: List<SimplePost>,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .margin(topBottom = 50.px),
        horizontalArrangement = Arrangement.Center
    ) {
        PostPreview(
            post = posts.first(),
            darkTheme = true,
            thumbnailHeight = if (breakpoint == Breakpoint.XL) 640.px else 320.px,
            onClick = onClick
        )

        if (breakpoint == Breakpoint.XL && posts.count() >= 2) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                posts.drop(1).forEach {
                    PostPreview(
                        modifier = Modifier.margin(bottom = 20.px),
                        post = it,
                        darkTheme = true,
                        vertical = false,
                        thumbnailHeight = 200.px,
                        titleMaxLength = 1,
                        onClick = onClick
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.LG && posts.count() >= 2) {
            Box(modifier = Modifier.width(24.px))

            PostPreview(
                post = posts[1],
                darkTheme = true,
                onClick = onClick
            )
        }
    }
}