package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.maxLines
import org.example.blogmultiplatform.utils.parseDateString
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    post: SimplePost,
    selectableMode: Boolean,
    onCheckedChanged: (Boolean, String) -> Unit
) {
    val context = rememberPageContext()
    var checked by remember(selectableMode) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(95.percent)
            .margin(bottom = 24.px)
            .padding(if (selectableMode) 10.px else 0.px)
            .borderRadius(4.px)
            .border(
                width = if (selectableMode) 4.px else 0.px,
                style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                color = if (selectableMode) Theme.Primary.rgb else Theme.Grey.rgb
            )
            .onClick {
                if (selectableMode) {
                    checked = !checked
                    onCheckedChanged(checked, post.id)
                } else {
                   context.router.navigateTo(Screen.AdminCreate.passPostId(post.id))
                }
            }
            .transition(CSSTransition(TransitionProperty.All, 300.ms))
            .cursor(Cursor.Pointer)
    ) {
        Image(
            modifier = Modifier
                .margin(bottom = 16.px)
                .height(320.px)
                .fillMaxWidth()
                .objectFit(ObjectFit.Cover),
            src = post.thumbnail,
            description = "Post Thumbnail Image"
        )

        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 12.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .color(Colors.Black)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .maxLines(2),
            text = post.title
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .color(Colors.Black)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .maxLines(3),
            text = post.subtitle
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryChip(post.category)

            if (selectableMode) {
                CheckboxInput(
                    checked = checked,
                    attrs = Modifier
                        .size(20.px)
                        .toAttrs()
                )
            }
        }
    }
}

@Composable
fun Posts(
    breakpoint: Breakpoint,
    posts: List<SimplePost>,
    selectableMode: Boolean = false,
    onCheckedChanged: (Boolean, String) -> Unit,
    showMoreVisibility: Boolean,
    onShowMore: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(1, 2, 3, 4)
        ) {
            posts.forEach {
                PostPreview(
                    post = it,
                    selectableMode = selectableMode,
                    onCheckedChanged = onCheckedChanged
                )
            }
        }

        SpanText(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .fontWeight(FontWeight.Medium)
                .cursor(Cursor.Pointer)
                .visibility(if (showMoreVisibility) Visibility.Visible else Visibility.Hidden)
                .onClick { onShowMore() },
            text = "Show more"
        )
    }
}