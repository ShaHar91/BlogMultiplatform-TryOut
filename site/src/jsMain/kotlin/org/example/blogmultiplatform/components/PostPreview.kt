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
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.maxLines
import org.example.blogmultiplatform.utils.parseDateString
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    post: SimplePost,
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    selectableMode: Boolean = false,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLength: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    onCheckedChanged: (Boolean, String) -> Unit = { _, _ -> },
    onClick: (String) -> Unit
) {
    var checked by remember(selectableMode) { mutableStateOf(false) }

    if (vertical) {
        Column(
            modifier = modifier
                .fillMaxWidth(if (darkTheme || titleColor == Theme.Sponsored.rgb) 100.percent else 95.percent)
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
                        onClick(post.id)
                    }
                }
                .transition(CSSTransition(TransitionProperty.All, 300.ms))
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                vertical = vertical,
                selectableMode = selectableMode,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                checked = checked,
                titleColor = titleColor
            )
        }
    } else {
        Row(
            modifier = modifier
                .cursor(Cursor.Pointer)
                .onClick {
                    onClick(post.id)
                }
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                vertical = vertical,
                selectableMode = selectableMode,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                checked = checked,
                titleColor = titleColor
            )
        }
    }
}

@Composable
fun PostContent(
    post: SimplePost,
    darkTheme: Boolean = false,
    vertical: Boolean = true,
    selectableMode: Boolean = false,
    thumbnailHeight: CSSSizeValue<CSSUnit.px> = 320.px,
    titleMaxLength: Int,
    titleColor: CSSColorValue,
    checked: Boolean
) {
    Image(
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .height(thumbnailHeight)
            .fillMaxWidth(if (vertical) 100.percent else 40.percent)
            .objectFit(ObjectFit.Cover),
        src = post.thumbnail,
        description = "Post Thumbnail Image"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .thenIf(!vertical) {
                Modifier.margin(left = 20.px)
            }
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(12.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 12.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .color(if (darkTheme) Colors.White else titleColor)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .maxLines(titleMaxLength),
            text = post.title
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .color(if (darkTheme) Colors.White else Colors.Black)
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
            CategoryChip(category = post.category, darkTheme = darkTheme)

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
