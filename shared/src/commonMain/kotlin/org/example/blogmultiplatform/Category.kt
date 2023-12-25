package org.example.blogmultiplatform

expect enum class Category: CategoryColor {
    Technology,
    Programming,
    Design
}

interface CategoryColor {
    val color: String
}

fun String.parseCategoryName() = runCatching { Category.valueOf(this) }.getOrElse { Category.Programming }