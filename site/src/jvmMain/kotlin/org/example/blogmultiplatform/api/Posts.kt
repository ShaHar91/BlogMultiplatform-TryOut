package org.example.blogmultiplatform.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.blogmultiplatform.data.MongoDB
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Post
import org.litote.kmongo.id.ObjectIdGenerator

@Api("addpost")
suspend fun addPost(context: ApiContext) {
    try {
        val post = context.req.body?.decodeToString()?.let { Json.decodeFromString<Post>(it) }
        val newPost = post?.copy(id = ObjectIdGenerator.newObjectId<String>().id.toHexString())
        context.res.setBodyText(
            newPost?.let {
                context.data.getValue<MongoDB>().addPost(it).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api("readmyposts")
suspend fun readMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val author = context.req.params["author"] ?: ""

        val myPosts = context.data.getValue<MongoDB>().readMyPosts(skip, author)

        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = myPosts)))
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Error(e.message.toString())))
    }
}

@Api("deleteselectedposts")
suspend fun deleteSelectedPosts(context: ApiContext) {
    try {
        val request = context.req.body?.decodeToString()?.let { Json.decodeFromString<List<String>>(it) }
        context.res.setBodyText(
            request?.let {
                context.data.getValue<MongoDB>().deleteSelectedPosts(ids = it).toString()
            } ?: "false"
        )
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api("searchposts")
suspend fun searchPostsByTitle(context: ApiContext) {
    try {
        val query = context.req.params["query"] ?: ""
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val request = context.data.getValue<MongoDB>().searchPostsByTitle(query, skip)

        context.res.setBodyText(Json.encodeToString(ApiListResponse.Success(data = request)))
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(ApiListResponse.Error(e.message.toString())))
    }
}