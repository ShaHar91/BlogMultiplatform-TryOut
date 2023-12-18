package org.example.blogmultiplatform.utils

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.example.blogmultiplatform.models.ControlStyle
import org.example.blogmultiplatform.navigation.Screen
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date

@Composable
fun isUserLoggedIn(content: @Composable () -> Unit) {
    val context = rememberPageContext()
    val remembered = remember { localStorage["remember"].toBoolean() }
    val userId = remember { localStorage["userId"] }
    var userIdExists by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        userIdExists = if (!userId.isNullOrEmpty()) checkUserId(id = userId) else false
        if (!remembered || !userIdExists) {
            context.router.navigateTo(Screen.AdminLogin.route)
        }
    }

    if (remembered && userIdExists) {
        content()
    } else {
        println("Loading...")
    }
}

fun logout() {
    localStorage["remember"] = "false"
    localStorage["userId"] = "false"
    localStorage["username"] = "false"
}

fun Modifier.noBorder(): Modifier {
    return this
        .border(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
        .outline(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
}

fun Modifier.placeholder(text: String): Modifier {
    return this.attrsModifier {
        attr("placeholder", text)
    }
}

fun getEditor() = document.getElementById(Id.contentInput) as HTMLTextAreaElement

fun getSelectedIntRange(): IntRange? {
    val editor = getEditor()
    val start = editor.selectionStart
    val end = editor.selectionEnd

    if (start == null || end == null) return null

    return IntRange(start, (end - 1))
}

fun getSelectedText(): String? {
    val range = getSelectedIntRange() ?: return null

    return getEditor().value.substring(range)
}

fun applyStyle(controlStyle: ControlStyle) {
    val selectedText = getSelectedText()
    val selectedIntRange = getSelectedIntRange()
    if (selectedIntRange != null && selectedText != null) {
        getEditor().value = getEditor().value.replaceRange(range = selectedIntRange, replacement = controlStyle.style)
        document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
    }
}

fun Long.parseDateString() = Date(this).toLocaleDateString()

fun Modifier.maxLines(maxLines: Int = Int.MAX_VALUE): Modifier {
    return this.styleModifier {
        property("display", "-webkit-box")
        property("-webkit-line-clamp", maxLines)
        property("line-clamp", maxLines)
        property("-webkit-box-orient", "vertical")
    }
}

fun parseSwitch(posts: List<String>): String {
    return if(posts.size == 1) "1 Post Selected" else "${posts.size} Posts Selected"
}

fun validateEmail(email: String): Boolean {
    val regex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    return regex.toRegex().matches(email)
}