package org.example.blogmultiplatform.data

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.example.blogmultiplatform.models.*
import org.example.blogmultiplatform.utils.CommonConstants.POSTS_PER_PAGE
import org.example.blogmultiplatform.utils.Constants.DATABASE_NAME
import org.example.blogmultiplatform.utils.Constants.MAIN_POSTS_LIMIT
import org.example.blogmultiplatform.Category

@InitApi
fun initMongoDB(context: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )

//     Do this for pushing to the remote
    System.getenv().forEach { (key, value) ->
        if (key == "MONGODB_URI") {
            context.data.add(MongoDB(context, connectionString = value))
        }
    }

//    // Do this for local things
//    context.data.add(MongoDB(context, ""))
}

class MongoDB(private val context: InitApiContext, connectionString: String) : MongoRepository {

    // Do this for local things
    private val client = MongoClient.create()

    // Do this for pushing to the remote
//    private val client = MongoClient.create(connectionString)

    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>("user")
    private val postCollection = database.getCollection<Post>("post")
    private val newsletterCollection = database.getCollection<Newsletter>("newsletter")

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection
            .updateOne(
                Filters.eq(Post::_id.name, post._id),
                mutableListOf(
                    Updates.set(Post::title.name, post.title),
                    Updates.set(Post::subtitle.name, post.subtitle),
                    Updates.set(Post::category.name, post.category),
                    Updates.set(Post::thumbnail.name, post.thumbnail),
                    Updates.set(Post::content.name, post.content),
                    Updates.set(Post::main.name, post.main),
                    Updates.set(Post::popular.name, post.popular),
                    Updates.set(Post::sponsored.name, post.sponsored),
                )
            )
            .wasAcknowledged()
    }

    override suspend fun readMyPosts(skip: Int, author: String): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.eq(SimplePost::author.name, author))
            .sort(descending(SimplePost::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readLatestPosts(skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(
                Filters.and(
                    Filters.eq(SimplePost::main.name, false),
                    Filters.eq(SimplePost::popular.name, false),
                    Filters.eq(SimplePost::sponsored.name, false),
                )
            )
            .sort(descending(SimplePost::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSponsoredPosts(): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.eq(SimplePost::sponsored.name, true))
            .sort(descending(SimplePost::date.name))
            .limit(2)
            .toList()
    }

    override suspend fun readPopularPosts(skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.eq(SimplePost::popular.name, true))
            .sort(descending(SimplePost::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readMainPosts(): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.eq(SimplePost::main.name, true))
            .sort(descending(SimplePost::date.name))
            .limit(MAIN_POSTS_LIMIT)
            .toList()
    }

    override suspend fun searchPostsByTitle(query: String, skip: Int): List<SimplePost> {
        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.regex(SimplePost::title.name, regexQuery.pattern))
            .sort(descending(SimplePost::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun searchPostsByCategory(category: Category, skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(Filters.eq(SimplePost::category.name, category))
            .sort(descending(SimplePost::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection.find(Filters.eq(Post::_id.name, id)).toList().first()
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return postCollection.deleteMany(Filters.`in`(Post::_id.name, ids))
            .wasAcknowledged()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    Filters.and(
                        Filters.eq(User::username.name, user.username),
                        Filters.eq(User::password.name, user.password),
                    )
                ).firstOrNull()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(Filters.eq(User::_id.name, id))
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun subscribe(newsletter: Newsletter): String {
        val result = newsletterCollection
            .find(Filters.eq(Newsletter::email.name, newsletter.email))
            .toList()

        return if (result.isNotEmpty()) {
            "You're already subscribed"
        } else {
            val newEmail = newsletterCollection.insertOne(newsletter)
                .wasAcknowledged()

            if (newEmail) {
                "Successfully Subscribed!"
            } else {
                "Something went wrong. Please try again later."
            }
        }
    }
}