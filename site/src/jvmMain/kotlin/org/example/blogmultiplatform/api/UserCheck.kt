package org.example.blogmultiplatform.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import org.example.blogmultiplatform.data.MongoDB
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.models.UserWithoutPassword
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Api("usercheck")
suspend fun userCheck(context: ApiContext) {
    try {
        // Get the data from the request and parse it to User object
        val userRequest = context.req.getBody<User>()
        // If the data in the request is a User object, check if the user exists in the MongoDB database
        val user = userRequest?.let { context.data.getValue<MongoDB>().checkUserExistence(User(username = it.username, password = hashPassword(it.password))) }

        if (user != null) {
            context.res.setBody<UserWithoutPassword>(UserWithoutPassword(_id = user._id, username = user.username))
        } else {
            context.res.setBody(Exception("User doesn't exist"))
        }
    } catch (e: Exception) {
        context.res.setBody(e)
    }
}

@Api("checkuserid")
suspend fun checkUserId(context: ApiContext) {
    try {
        val idRequest = context.req.getBody<String>()
        val result = idRequest?.let { context.data.getValue<MongoDB>().checkUserId(it) }

        if (result != null) {
            context.res.setBody(result)
        } else {
            context.res.setBody(false)
        }
    } catch (e: Exception) {
        context.res.setBody(false)
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