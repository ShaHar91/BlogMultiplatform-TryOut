package org.example.blogmultiplatform

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(val color: String) {
    Technology(color = Theme.Green.hex),
    Programming(color = Theme.Yellow.hex),
    Design(color = Theme.Purple.hex)
}

enum class Theme(val hex: String) {
    Purple(hex = "8B6DFF"),
    Green(hex = "00FF94"),
    Yellow(hex = "FFEC45")
}