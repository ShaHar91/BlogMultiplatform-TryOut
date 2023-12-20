package com.christiano.androidapp.models

import org.example.blogmultiplatform.CategoryCommon

enum class Category(override val color: String) : CategoryCommon {
    Technology(""),
    Programming(""),
    Design("");

    companion object {
        fun parseName(name: String) = runCatching { Category.valueOf(name) }.getOrElse { Programming }
    }
}