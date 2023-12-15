package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
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
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.components.ControlPopup
import org.example.blogmultiplatform.components.MessagePopup
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.ControlStyle
import org.example.blogmultiplatform.models.EditorControl
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.EditorKeyStyle
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Id
import org.example.blogmultiplatform.util.addPost
import org.example.blogmultiplatform.util.applyStyle
import org.example.blogmultiplatform.util.getEditor
import org.example.blogmultiplatform.util.getSelectedText
import org.example.blogmultiplatform.util.isUserLoggedIn
import org.example.blogmultiplatform.util.noBorder
import org.example.blogmultiplatform.util.placeholder
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea
import org.jetbrains.compose.web.dom.Ul
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

data class CreatePageUiState(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var thumbnail: String = "",
    var thumbnailInputDisabled: Boolean = true,
    var content: String = "",
    var category: Category = Category.Programming,
    var popular: Boolean = false,
    var main: Boolean = false,
    var sponsored: Boolean = false,
    var editorVisibility: Boolean = true,
    var messagePopup: Boolean = false,
    var linkPopup: Boolean = false,
    var imagePopup: Boolean = false,
)

@Page
@Composable
fun CreatePage() {
    isUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    var uiState by remember { mutableStateOf(CreatePageUiState()) }
    val scope = rememberCoroutineScope()

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
                            checked = uiState.popular,
                            onCheckedChange = { uiState = uiState.copy(popular = it) },
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
                            checked = uiState.main,
                            onCheckedChange = { uiState = uiState.copy(main = it) },
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
                            checked = uiState.sponsored,
                            onCheckedChange = { uiState = uiState.copy(sponsored = it) },
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
                        .id(Id.titleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGrey.rgb)
                        .borderRadius(4.px)
                        .noBorder()
                        .fontSize(16.px)
                        .fontFamily(FONT_FAMILY)
                        .placeholder("Title")
                        .toAttrs()
                )

                Input(
                    type = InputType.Text,
                    attrs = Modifier
                        .id(Id.subtitleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGrey.rgb)
                        .borderRadius(4.px)
                        .noBorder()
                        .fontSize(16.px)
                        .fontFamily(FONT_FAMILY)
                        .placeholder("Subtitle")
                        .toAttrs()
                )

                CategoryDropdown(selectedCategory = uiState.category) {
                    uiState = uiState.copy(category = it)
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
                        checked = !uiState.thumbnailInputDisabled,
                        onCheckedChange = { uiState = uiState.copy(thumbnailInputDisabled = !it) },
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

                ThumbnailUploader(uiState.thumbnail, uiState.thumbnailInputDisabled) { filename, file ->
                    (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value = filename
                    uiState = uiState.copy(thumbnail = file)
                }

                EditorControls(
                    breakpoint = breakpoint,
                    editorVisibility = uiState.editorVisibility,
                    onEditorVisibilityChanged = {
                        uiState = uiState.copy(editorVisibility = !uiState.editorVisibility)
                    },
                    onLinkClick = {
                        uiState = uiState.copy(linkPopup = true)
                    },
                    onImageClick = {
                        uiState = uiState.copy(imagePopup = true)
                    }
                )

                Editor(uiState.editorVisibility)

                CreateButton {
                    uiState = uiState.copy(
                        title = (document.getElementById(Id.titleInput) as HTMLInputElement).value,
                        subtitle = (document.getElementById(Id.subtitleInput) as HTMLInputElement).value,
                        content = (document.getElementById(Id.contentInput) as HTMLTextAreaElement).value,
                    )

                    if (uiState.thumbnailInputDisabled.not()) {
                        uiState = uiState.copy(thumbnail = (document.getElementById(Id.thumbnailInput) as HTMLInputElement).value)
                    }

                    if (
                        uiState.title.isNotEmpty() &&
                        uiState.subtitle.isNotEmpty() &&
                        uiState.thumbnail.isNotEmpty() &&
                        uiState.content.isNotEmpty()
                    ) {
                        scope.launch {
                            val result = addPost(
                                Post(
                                    author = localStorage.getItem("username").toString(),
                                    title = uiState.title,
                                    subtitle = uiState.subtitle,
                                    date = Date.now().toLong(),
                                    thumbnail = uiState.thumbnail,
                                    content = uiState.content,
                                    category = uiState.category,
                                    popular = uiState.popular,
                                    main = uiState.main,
                                    sponsored = uiState.sponsored
                                )
                            )

                            if (result) {
                                context.router.navigateTo(Screen.AdminSuccess.route)
                            }
                        }
                    } else {
                        scope.launch {
                            uiState = uiState.copy(messagePopup = true)
                            delay(2000)
                            uiState = uiState.copy(messagePopup = false)
                        }
                    }
                }
            }
        }
    }

    if (uiState.messagePopup) {
        MessagePopup("Please fill out all fields") {
            uiState = uiState.copy(messagePopup = false)
        }
    }

    if (uiState.linkPopup) {
        ControlPopup(
            editControl = EditorControl.Link,
            onDialogDismiss = {
                uiState = uiState.copy(linkPopup = false)
            }
        ) { href, title ->
            applyStyle(
                ControlStyle.Link(
                    selectedText = getSelectedText(),
                    href = href,
                    title = title
                )
            )
        }
    }

    if (uiState.imagePopup) {
        ControlPopup(
            editControl = EditorControl.Image,
            onDialogDismiss = {
                uiState = uiState.copy(imagePopup = false)
            }
        ) { imageUrl, description ->
            applyStyle(
                ControlStyle.Image(
                    selectedText = getSelectedText(),
                    imageUrl = imageUrl,
                    desc = description
                )
            )
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
            .id(Id.thumbnailInput)
            .fillMaxSize()
            .padding(leftRight = 20.px)
            .margin(right = 12.px)
            .backgroundColor(Theme.LightGrey.rgb)
            .noBorder()
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .borderRadius(4.px)
            .thenIf(thumbnailInputDisabled) {
                Modifier.disabled()
            }
            .placeholder("Thumbnail")
            .toAttrs {
                attr("value", thumbnail)
            }
        )

        Button(
            attrs = Modifier
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if (!thumbnailInputDisabled) Theme.Grey.rgb else Theme.Primary.rgb)
                .color(if (!thumbnailInputDisabled) Theme.DarkGrey.rgb else Theme.White.rgb)
                .noBorder()
                .borderRadius(4.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .fontWeight(FontWeight.Medium)
                .thenIf(!thumbnailInputDisabled) {
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

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
    onEditorVisibilityChanged: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(1, 2)
        ) {
            Row(
                modifier = Modifier
                    .height(54.px)
                    .borderRadius(4.px)
                    .backgroundColor(Theme.LightGrey.rgb)
            ) {
                EditorControl.entries.forEach {
                    EditorControlView(it) { applyControlStyle(it, onLinkClick, onImageClick) }
                }
            }

            Box(
                modifier = Modifier,
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .thenIf(breakpoint < Breakpoint.SM) {
                            Modifier.fillMaxWidth()
                        }
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .padding(leftRight = 24.px)
                        .borderRadius(4.px)
                        .backgroundColor(if (editorVisibility) Theme.LightGrey.rgb else Theme.Primary.rgb)
                        .color(if (editorVisibility) Theme.DarkGrey.rgb else Theme.White.rgb)
                        .noBorder()
                        .onClick {
                            onEditorVisibilityChanged()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .fontSize(14.px),
                        text = "Preview"
                    )
                }
            }
        }
    }
}

fun applyControlStyle(
    editorControl: EditorControl,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit
) {
    when (editorControl) {
        EditorControl.Bold -> applyStyle(ControlStyle.Bold(selectedText = getSelectedText()))
        EditorControl.Italic -> applyStyle(ControlStyle.Italic(selectedText = getSelectedText()))
        EditorControl.Link -> {
            onLinkClick()
        }

        EditorControl.Title -> applyStyle(ControlStyle.Title(selectedText = getSelectedText()))
        EditorControl.Subtitle -> applyStyle(ControlStyle.Subtitle(selectedText = getSelectedText()))
        EditorControl.Quote -> applyStyle(ControlStyle.Quote(selectedText = getSelectedText()))
        EditorControl.Code -> applyStyle(ControlStyle.Code(selectedText = getSelectedText()))
        EditorControl.Image -> {
            onImageClick()
        }
    }
}

@Composable
fun EditorControlView(control: EditorControl, onClick: () -> Unit) {
    Box(
        modifier = EditorKeyStyle.toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(4.px)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(control.icon, "${control.name} Icon")
    }

}

@Composable
fun Editor(
    editorVisibility: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextArea(
            attrs = Modifier
                .id(Id.contentInput)
                .fillMaxWidth()
                .height(400.px)
                .resize(Resize.None)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(20.px)
                .backgroundColor(Theme.LightGrey.rgb)
                .fontFamily(FONT_FAMILY)
                .fontWeight(FontWeight.Medium)
                .fontSize(16.px)
                .border(4.px)
                .noBorder()
                .visibility(if (editorVisibility) Visibility.Visible else Visibility.Hidden)
                .placeholder("Type Here...")
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey) {
                        applyStyle(controlStyle = ControlStyle.Break(selectedText = getSelectedText()))
                    }
                }
                .toAttrs()
        )

        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .visibility(if (editorVisibility) Visibility.Hidden else Visibility.Visible)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .margin(top = 8.px)
                .padding(20.px)
                .backgroundColor(Theme.LightGrey.rgb)
                .border(4.px)
                .noBorder()
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {

        }
    }
}

@Composable
private fun CreateButton(onClick: () -> Unit) {
    Button(
        attrs = Modifier
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb)
            .color(Theme.White.rgb)
            .noBorder()
            .borderRadius(4.px)
            .fontFamily(FONT_FAMILY)
            .fontSize(16.px)
            .onClick { onClick() }
            .toAttrs()
    )
    { SpanText("Create") }
}






















