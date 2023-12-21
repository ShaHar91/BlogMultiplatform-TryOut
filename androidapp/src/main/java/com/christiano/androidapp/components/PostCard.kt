package com.christiano.androidapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.christiano.androidapp.models.Category
import com.christiano.androidapp.models.Post
import com.christiano.androidapp.ui.theme.BlogMultiplatformTheme
import com.christiano.androidapp.util.RequestState
import com.christiano.androidapp.util.convertLongToDate

@Composable
fun PostCard(
    post: Post,
    onPostClick: (String) -> Unit
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onPostClick(post._id) },
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(260.dp),
                model = ImageRequest
                    .Builder(context)
                    .data(if (post.thumbnail.contains("http")) post.thumbnail else post.decodeThumbnailImage())
                    .build(),
                contentDescription = "Post Thumbnail",
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .alpha(0.5f),
                    text = post.date.convertLongToDate(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier,
                    text = post.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier,
                    text = post.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )

                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = {
                        Text(Category.parseName(post.category).name)
                    }
                )
            }
        }
    }
}

@Composable
fun PostCardsView(
    modifier: Modifier = Modifier,
    hideMessage: Boolean = false,
    posts: RequestState<List<Post>>,
    onPostClick: (String) -> Unit
) {
    when (posts) {
        is RequestState.Success -> {
            if (posts.data.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = posts.data,
                        key = { post -> post._id }
                    ) {
                        PostCard(post = it, onPostClick = onPostClick)
                    }
                }
            } else {
                EmptyUI()
            }
        }

        is RequestState.Error -> {
            EmptyUI(message = posts.error.message.toString())
        }

        is RequestState.Idle -> {
            EmptyUI(hideMessage)
        }

        is RequestState.Loading -> {
            EmptyUI(loading = true)
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    BlogMultiplatformTheme {
        PostCard(post = Post.previewData(), onPostClick = {})
    }
}