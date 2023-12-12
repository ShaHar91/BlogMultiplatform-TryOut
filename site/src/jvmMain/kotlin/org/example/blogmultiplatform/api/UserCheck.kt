package org.example.blogmultiplatform.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.blogmultiplatform.data.MongoDB
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.models.UserWithoutPassword
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Api("usercheck")
suspend fun userCheck(context: ApiContext) {
    try {
        // Get the data from the request and parse it to User object
        val userRequest = context.req.body?.decodeToString()?.let { Json.decodeFromString<User>(it) }
        // If the data in the request is a User object, check if the user exists in the MongoDB database
        val user = userRequest?.let { context.data.getValue<MongoDB>().checkUserExistence(User(username = it.username, password = hashPassword(it.password))) }

        if (user != null) {
            context.res.setBodyText(Json.encodeToString<UserWithoutPassword>(UserWithoutPassword(id = user.id, username = user.username)))
        } else {
            context.res.setBodyText(Json.encodeToString(Exception("User doesn't exist")))
        }
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e))
    }
}

@Api("checkuserid")
suspend fun checkUserId(context: ApiContext) {
    try {
        val idRequest = context.req.body?.decodeToString()?.let { Json.decodeFromString<String>(it) }
        val result = idRequest?.let { context.data.getValue<MongoDB>().checkUserId(it) }

        if (result != null) {
            context.res.setBodyText(Json.encodeToString(result))
        } else {
            context.res.setBodyText(Json.encodeToString(false))
        }
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(false))
    }
}

private fun hashPassword(password: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()

    for (byte in hashBytes) {
        hexString.append(String.format("%02x", byte))
    }

    return hexString.toString()
}