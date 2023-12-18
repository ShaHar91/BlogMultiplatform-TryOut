package org.example.blogmultiplatform.data

import org.example.blogmultiplatform.models.Newsletter
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.SimplePost
import org.example.blogmultiplatform.models.User

interface MongoRepository {
    suspend fun addPost(post: Post): Boolean
    suspend fun updatePost(post: Post): Boolean
    suspend fun readMyPosts(skip: Int, author: String): List<SimplePost>
    suspend fun readLatestPosts(skip: Int): List<SimplePost>
    suspend fun readSponsoredPosts(): List<SimplePost>
    suspend fun readPopularPosts(skip: Int): List<SimplePost>
    suspend fun readMainPosts(): List<SimplePost>
    suspend fun searchPostsByTitle(query: String, skip: Int): List<SimplePost>
    suspend fun readSelectedPost(id: String): Post
    suspend fun deleteSelectedPosts(ids: List<String>): Boolean
    suspend fun checkUserExistence(user: User): User?
    suspend fun checkUserId(id: String): Boolean
    suspend fun subscribe(newsletter: Newsletter): String
}