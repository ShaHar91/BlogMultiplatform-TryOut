package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.http.http
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaPlus
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.models.RandomJoke
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.util.Constants
import org.example.blogmultiplatform.util.Res
import org.example.blogmultiplatform.util.isUserLoggedIn
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Page
@Composable
fun HomePage() {
    isUserLoggedIn {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    var randomJoke: RandomJoke? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val date = localStorage["date"]
        if (date != null) {
            val difference = (Date.now() - date.toDouble())
            val dayHasPassed = difference >= 86400000
            if (dayHasPassed) {
                scope.launch {
                    try {
                        val result = window.http.get(Constants.HUMOR_API_URL).decodeToString()
                        randomJoke = Json.decodeFromString<RandomJoke>(result)
                        localStorage["date"] = Date.now().toString()
                        localStorage["joke"] = result
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            } else {
                try {
                    randomJoke = localStorage["joke"]?.let { Json.decodeFromString<RandomJoke?>(it) }
                } catch (e: Exception) {
                    randomJoke = RandomJoke(id = -1, joke = "Unexepected joke")
                    println(e.message)
                }
            }
        } else {
            scope.launch {
                try {
                    val result = window.http.get(Constants.HUMOR_API_URL).decodeToString()
                    randomJoke = Json.decodeFromString<RandomJoke>(result)
                    localStorage["date"] = Date.now().toString()
                    localStorage["joke"] = result
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    AdminPageLayout {
        AddButton()
        HomeContent(randomJoke)
    }
}

@Composable
fun HomeContent(randomJoke: RandomJoke?) {
    val breakpoint = rememberBreakpoint()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(left = if (breakpoint > Breakpoint.MD) Constants.SIDE_PANEL_WIDTH.px else 0.px),
        contentAlignment = Alignment.Center
    ) {
        if (randomJoke != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(topBottom = 50.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (randomJoke.id != -1) {
                    Image(
                        modifier = Modifier
                            .size(150.px)
                            .margin(bottom = 50.px),
                        src = Res.Image.laugh,
                        description = "Laugh Image"
                    )
                }

                if (randomJoke.joke.contains("Q:")) {
                    SpanText(
                        modifier = Modifier
                            .margin(bottom = 14.px)
                            .fillMaxWidth(40.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb)
                            .fontSize(28.px)
                            .fontFamily(Constants.FONT_FAMILY)
                            .fontWeight(FontWeight.Bold),
                        text = randomJoke.joke.split(":")[1].dropLast(1)
                    )

                    SpanText(
                        modifier = Modifier
                            .fillMaxWidth(40.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.HalfBlack.rgb)
                            .fontFamily(Constants.FONT_FAMILY)
                            .fontSize(20.px)
                            .fontWeight(FontWeight.Normal),
                        text = randomJoke.joke.split(":").last()
                    )
                } else {
                    SpanText(
                        modifier = Modifier
                            .margin(bottom = 14.px)
                            .fillMaxWidth(40.percent)
                            .textAlign(TextAlign.Center)
                            .color(Theme.Secondary.rgb)
                            .fontSize(28.px)
                            .fontFamily(Constants.FONT_FAMILY)
                            .fontWeight(FontWeight.Bold),
                        text = randomJoke.joke
                    )
                }
            }
        } else {
            println("Loading a joke...")
        }
    }
}

@Composable
fun AddButton() {
    val breakpoint = rememberBreakpoint()
    val ctx = rememberPageContext()

    Box(
        modifier = Modifier
            .height(100.vh)
            .fillMaxWidth()
            .position(Position.Fixed)
            // Prevent this box from being clickable and pass every click event through to the other layers
            .pointerEvents(PointerEvents.None),
        contentAlignment = Alignment.BottomEnd
    ) {

        Box(
            modifier = Modifier
                .size(if (breakpoint > Breakpoint.MD) 80.px else 50.px)
                .borderRadius(14.px)
                .cursor(Cursor.Pointer)
                .margin(
                    right = if (breakpoint > Breakpoint.MD) 40.px else 20.px,
                    bottom = if (breakpoint > Breakpoint.MD) 40.px else 20.px
                )
                .backgroundColor(Theme.Primary.rgb)
                .onClick {
                    ctx.router.navigateTo(Screen.AdminCreate.route)
                }
                .pointerEvents(PointerEvents.Auto),

            contentAlignment = Alignment.Center
        ) {
            FaPlus(
                size = IconSize.LG,
                modifier = Modifier
                    .color(Colors.White)
            )
        }
    }
}