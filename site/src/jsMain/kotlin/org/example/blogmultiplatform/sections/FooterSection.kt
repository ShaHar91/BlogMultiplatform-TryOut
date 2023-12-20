package org.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.text.SpanText
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.jetbrains.compose.web.css.px

@Composable
fun FooterSection(

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb)
            .padding(topBottom = 50.px),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
        ) {
            SpanText(
                modifier = Modifier.fontFamily(FONT_FAMILY)
                    .fontSize(14.px)
                    .color(Theme.White.rgb)
                    .fontWeight(FontWeight.Medium),
                text = "Copyright © 2023 - "
            )

            SpanText(
                modifier = Modifier.fontFamily(FONT_FAMILY)
                    .color(Theme.Primary.rgb)
                    .fontSize(14.px)
                    .fontWeight(FontWeight.Medium),
                text = "Christiano Bolla"
            )
        }
    }

}