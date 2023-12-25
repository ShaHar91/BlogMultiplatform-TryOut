package org.example.blogmultiplatform.models

import kotlinx.serialization.Serializable
import org.example.blogmultiplatform.Category


@Serializable
data class Post(
    val _id: String = "",
    val author: String = "",
    val date: Double = 0.0,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String,
    val category: Category,
    val popular: Boolean= false,
    val main: Boolean = false,
    val sponsored: Boolean = false
)

@Serializable
data class SimplePost(
    val _id: String = "",
    val author: String,
    val date: Double,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val category: Category,
    val popular: Boolean= false,
    val main: Boolean = false,
    val sponsored: Boolean = false
)