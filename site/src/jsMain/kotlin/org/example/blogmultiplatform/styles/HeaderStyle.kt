package org.example.blogmultiplatform.styles

import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.anyLink
import com.varabyte.kobweb.silk.style.selectors.hover
import org.example.blogmultiplatform.models.Theme
import org.jetbrains.compose.web.css.ms

val CategoryItemStyle = CssStyle {
    base {
        Modifier
            .color(Colors.White)
            .transition(Transition.of("color", 200.ms))
    }

    // Order of "anyLink" and "hover" DOES matter!
    anyLink{
        Modifier
            .color(Theme.White.rgb)
    }

    hover {
        Modifier
            .color(Theme.Primary.rgb)
    }
}