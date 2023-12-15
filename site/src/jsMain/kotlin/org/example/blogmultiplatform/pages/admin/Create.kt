package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.isUserLoggedIn
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val breakpoint = rememberBreakpoint()
    var popularChecked by remember { mutableStateOf(false) }
    var mainChecked by remember { mutableStateOf(false) }
    var sponsoredChecked by remember { mutableStateOf(false) }
    var thumbnailInputDisabled by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(Category.Programming) }
    var fileName by remember { mutableStateOf("") }

    AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleGrid(numColumns(base = 1, sm = 3)) {
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = popularChecked,
                            onCheckedChange = { popularChecked = it },
                            size = SwitchSize.LG
                        )

                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Popular"
                        )
                    }

                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = mainChecked,
                            onCheckedChange = { mainChecked = it },
                            size = SwitchSize.LG
                        )

                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Main"
                        )
                    }

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier.margin(right = 8.px),
                            checked = sponsoredChecked,
                            onCheckedChange = { sponsoredChecked = it },
                            size = SwitchSize.LG
                        )

                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Sponsored"
                        )
                    }
                }

                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGrey.rgb)
                        .borderRadius(4.px)
                        .border(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                        .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                        .fontSize(16.px)
                        .fontFamily(FONT_FAMILY)
                        .attrsModifier {
                            attr("placeholder", "Title")
                        }.toAttrs()
                )

                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGrey.rgb)
                        .borderRadius(4.px)
                        .border(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                        .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
                        .fontSize(16.px)
                        .fontFamily(FONT_FAMILY)
                        .attrsModifier {
                            attr("placeholder", "Subtitle")
                        }.toAttrs()
                )

                CategoryDropdown(selectedCategory = selectedCategory) {
                    selectedCategory = it
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(topBottom = 12.px),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        checked = thumbnailInputDisabled,
                        onCheckedChange = { thumbnailInputDisabled = it },
                        size = SwitchSize.MD
                    )

                    SpanText(
                        modifier = Modifier
                            .fontSize(14.px)
                            .fontFamily(FONT_FAMILY)
                            .color(Theme.HalfBlack.rgb),
                        text = "Paste an Image URL insead"
                    )
                }

                ThumbnailUploader(fileName, thumbnailInputDisabled) { filename, file ->
                    fileName = filename
                    println(filename)
                    println(file)
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    selectedCategory: Category,
    onCategorySelect: (Category) -> Unit
) {
    Box(
        modifier = Modifier
            .margin(bottom = 12.px)
            .classNames("dropdown")
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGrey.rgb)
            .cursor(Cursor.Pointer)
            .attrsModifier {
                attr("data-bs-toggle", "dropdown")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .fontSize(16.px)
                    .fontFamily(FONT_FAMILY),
                text = selectedCategory.name
            )

            Box(
                modifier = Modifier.classNames("dropdown-toggle")
            ) {

            }

        }

        Ul(
            attrs = Modifier
                .fillMaxWidth()
                .classNames("dropdown-menu")
                .toAttrs()
        ) {
            Category.entries.forEach { category ->
                A(
                    attrs = Modifier
                        .classNames("dropdown-item")
                        .color(Colors.Black)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .onClick { onCategorySelect(category) }
                        .toAttrs()
                ) {
                    Text(category.name)
                }
            }
        }
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputDisabled: Boolean,
    onThumbnailSelect: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px)
    ) {
        Input(InputType.Text, attrs = Modifier
            .fillMaxSize()
            .padding(leftRight = 20.px)
            .margin(right = 12.px)
            .backgroundColor(Theme.LightGrey.rgb)
            .border(0.px, LineStyle.None, Colors.Transparent)
            .outline(0.px, LineStyle.None, Colors.Transparent)
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(4.px)
            .thenIf(!thumbnailInputDisabled) {
                Modifier.disabled()
            }
            .toAttrs {
                attr("placeholder", "Thumbnail")
                attr("value", thumbnail)
            }
        )

        Button(
            attrs = Modifier
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if (thumbnailInputDisabled) Theme.Grey.rgb else Theme.Primary.rgb)
                .color(if (thumbnailInputDisabled) Theme.DarkGrey.rgb else Theme.White.rgb)
                .border(0.px, LineStyle.None, Colors.Transparent)
                .outline(0.px, LineStyle.None, Colors.Transparent)
                .borderRadius(4.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .fontWeight(FontWeight.Medium)
                .thenIf(thumbnailInputDisabled) {
                    Modifier.disabled()
                }
                .onClick {
                    document.loadDataUrlFromDisk("image/png, image/jpeg") {
                        onThumbnailSelect(filename, it)
                    }
                }
                .toAttrs()
        ) {
            SpanText("Upload")
        }
    }
}

























