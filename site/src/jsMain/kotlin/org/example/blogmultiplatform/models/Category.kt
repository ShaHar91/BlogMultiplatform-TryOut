package org.example.blogmultiplatform.models

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(val color: String) {
    Technology(color = Theme.Green.hex),
    Programming(color = Theme.Yellow.hex),
    Design(color = Theme.Purple.hex);

    companion object {
        fun parseName(name: String) = runCatching { Category.valueOf(name) }.getOrElse { Programming }
    }
}