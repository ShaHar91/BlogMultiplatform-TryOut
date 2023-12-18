package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.SVGFillType
import com.varabyte.kobweb.compose.dom.svg.SVGStrokeLineCap
import com.varabyte.kobweb.compose.dom.svg.SVGStrokeLineJoin
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.NavigationItemStyle
import org.example.blogmultiplatform.utils.Constants.COLLAPSED_PANEL_HEIGHT
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.Res
import org.example.blogmultiplatform.utils.logout
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel(onMenuClick: () -> Unit) {
    val breakpoint = rememberBreakpoint()

    if (breakpoint > Breakpoint.MD) {
        SidePanelInternal()
    } else {
        CollapsedSidePanel(onMenuClick)
    }
}

@Composable
private fun SidePanelInternal() {
    Column(
        modifier = Modifier
            .padding(top = 50.px, leftRight = 40.px)
            .width(SIDE_PANEL_WIDTH.px)
            .height(100.vh) // full screen height
            .position(Position.Fixed)
            .backgroundColor(Theme.Secondary.rgb)
            .zIndex(9)
    ) {
        Image(
            modifier = Modifier.margin(bottom = 60.px),
            src = Res.Image.logo,
            description = "Logo Image"
        )

        NavigationItems()
    }
}

@Composable
fun NavigationItems() {
    val context = rememberPageContext()

    SpanText(
        modifier = Modifier
            .fontFamily(FONT_FAMILY)
            .fontSize(14.px)
            .color(Theme.HalfWhite.rgb)
            .margin(bottom = 30.px),
        text = "Dashboard"
    )

    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        selected = context.route.path == Screen.AdminHome.route,
        title = "Home",
        icon = Res.PathIcon.home
    ) { context.router.navigateTo(Screen.AdminHome.route) }

    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        selected = context.route.path == Screen.AdminCreate.route,
        title = "Create Post",
        icon = Res.PathIcon.create
    ) { context.router.navigateTo(Screen.AdminCreate.route) }

    NavigationItem(
        modifier = Modifier.margin(bottom = 24.px),
        selected = context.route.path == Screen.AdminMyPosts.route,
        title = "My Posts",
        icon = Res.PathIcon.posts
    ) { context.router.navigateTo(Screen.AdminMyPosts.route) }

    NavigationItem(
        title = "Logout",
        icon = Res.PathIcon.logout
    ) {
        logout()
        context.router.navigateTo(Screen.AdminLogin.route)
    }
}

@Composable
private fun NavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String,
    icon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = NavigationItemStyle.toModifier()
            .then(modifier)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        VectorIcon(
            modifier = Modifier.margin(right = 10.px),
            pathData = icon,
            selected = selected
        )

        SpanText(
            modifier = Modifier
                .id(Id.navigationText)
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(selected) {
                    Modifier.color(Theme.Primary.rgb)
                },
            text = title
        )
    }
}

@Composable
private fun VectorIcon(
    modifier: Modifier = Modifier,
    pathData: String,
    selected: Boolean
) {
    Svg(
        attrs = modifier
            .id(Id.svgParent)
            .width(24.px)
            .height(24.px)
            .toAttrs {
                viewBox(0, 0, 24, 24)
                fill(SVGFillType.None)
            }
    ) {

        Path(
            attrs = Modifier
                .id(Id.vectorIcon)
                .thenIf(selected) {
                    Modifier.styleModifier {
                        property("stroke", Theme.Primary.hex)
                    }
                }
                .toAttrs {
                    d(pathData)
                    strokeWidth(2)
                    strokeLineCap(SVGStrokeLineCap.Round)
                    strokeLineJoin(SVGStrokeLineJoin.Round)
                }
        )
    }
}

@Composable
fun CollapsedSidePanel(
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(COLLAPSED_PANEL_HEIGHT.px)
            .padding(leftRight = 24.px)
            .backgroundColor(Theme.Secondary.rgb),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaBars(
            modifier = Modifier
                .margin(right = 24.px)
                .color(Colors.White)
                .cursor(Cursor.Pointer)
                .onClick {
                    onMenuClick()
                },
            size = IconSize.XL
        )

        Image(
            modifier = Modifier.width(80.px),
            src = Res.Image.logo,
            description = "Logo Image"
        )
    }
}

@Composable
fun OverflowSidePanel(
    onMenuClose: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()

    var translateX by remember { mutableStateOf((-100).percent) }
    var opacity by remember { mutableStateOf(0.percent) }

    LaunchedEffect(key1 = breakpoint) {
        translateX = 0.percent
        opacity = 100.percent

        if (breakpoint > Breakpoint.MD) {
            scope.launch {
                translateX = (-100).percent
                opacity = 0.percent

                delay(500)
                onMenuClose()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
            .backgroundColor(Theme.HalfBlack.rgb)
            .opacity(opacity)
            .transition(CSSTransition(property = "opacity", duration = 300.ms))
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(all = 24.px)
                .width(if (breakpoint < Breakpoint.MD) 50.percent else 25.percent)
                .backgroundColor(Theme.Secondary.rgb)
                .zIndex(9)
                .translateX(translateX)
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .transition(CSSTransition(property = "translate", duration = 300.ms))
        ) {
            Row(
                modifier = Modifier.margin(bottom = 60.px, top = 24.px),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 20.px)
                        .color(Colors.White)
                        .onClick {
                            scope.launch {
                                translateX = (-100).percent
                                opacity = 0.percent

                                delay(500)
                                onMenuClose()
                            }
                        }
                        .cursor(Cursor.Pointer),
                    size = IconSize.LG
                )

                Image(
                    modifier = Modifier.width(80.px),
                    src = Res.Image.logo,
                    description = "Logo Image"
                )
            }

            content()
        }
    }
}