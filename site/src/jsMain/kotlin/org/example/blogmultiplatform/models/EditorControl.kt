package org.example.blogmultiplatform.models

import org.example.blogmultiplatform.utils.Res

enum class EditorControl(
    val icon: String
) {
    Bold(Res.Icon.bold),
    Italic(Res.Icon.italic),
    Link(Res.Icon.link),
    Title(Res.Icon.title),
    Subtitle(Res.Icon.subtitle),
    Quote(Res.Icon.quote),
    Code(Res.Icon.code),
    Image(Res.Icon.image),
}