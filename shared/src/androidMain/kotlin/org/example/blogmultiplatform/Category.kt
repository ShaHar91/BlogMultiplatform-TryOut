package org.example.blogmultiplatform

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(val color: String) {
    Technology(color = ""),
    Programming(color = ""),
    Design(color = "")
}