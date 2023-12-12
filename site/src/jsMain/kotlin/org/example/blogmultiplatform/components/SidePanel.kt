package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.SVGFillType
import com.varabyte.kobweb.compose.dom.svg.SVGStrokeLineCap
import com.varabyte.kobweb.compose.dom.svg.SVGStrokeLineJoin
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.styles.NavigationItemStyle
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.util.Id
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.logout
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel() {
    val context = rememberPageContext()

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
}

@Composable
fun NavigationItem(
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
fun VectorIcon(
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