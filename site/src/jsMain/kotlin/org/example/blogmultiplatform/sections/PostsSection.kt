package org.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.example.blogmultiplatform.components.PostsView
import org.example.blogmultiplatform.models.SimplePost
import org.jetbrains.compose.web.css.px

@Composable
fun PostsSection(
    breakpoint: Breakpoint,
    posts: List<SimplePost>,
    title: String? = null,
    showMoreVisibility: Boolean,
    onShowMore: () -> Unit,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .margin(topBottom = 50.px)
            .maxWidth(1920.px),
        contentAlignment = Alignment.TopCenter
    ) {
        PostsView(
            breakpoint = breakpoint,
            posts = posts,
            title = title,
            onShowMore = onShowMore,
            showMoreVisibility = showMoreVisibility,
            onClick = onClick
        )
    }
}