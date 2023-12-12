package org.example.blogmultiplatform.util

import com.varabyte.kobweb.browser.api
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.models.UserWithoutPassword

suspend fun checkUserExistence(user: User): UserWithoutPassword? {
    return try {
        val result = window.api.tryPost("usercheck", body = Json.encodeToString(user).encodeToByteArray())

        // Because of this the wrapping try catch is needed
        Json.decodeFromString<UserWithoutPassword>(result.toString())
    } catch (e: Exception) {
        println(e.message)
        null
    }
}