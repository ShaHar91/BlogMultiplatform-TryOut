package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.util.Constants.FONT_FAMILY
import org.example.blogmultiplatform.util.Res
import org.jetbrains.compose.web.css.px

@Page
@Composable
fun SuccessPage() {
    LaunchedEffect(Unit) {
        delay(3000)
        window.history.back()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .margin(bottom = 24.px),
            src = Res.Icon.checkmark,
            description = "Checkmark icon"
        )

        SpanText(
            modifier = Modifier
                .fontFamily(FONT_FAMILY)
                .fontSize(24.px),
            text = "Post Successfully Created!"
        )

        SpanText(
            modifier = Modifier
                .color(Theme.HalfBlack.rgb)
                .fontFamily(FONT_FAMILY)
                .fontSize(18.px),
            text = "Redirecting you back..."
        )
    }
}