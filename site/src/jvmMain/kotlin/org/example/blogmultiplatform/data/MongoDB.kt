package org.example.blogmultiplatform.data

import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import org.example.blogmultiplatform.models.*
import org.example.blogmultiplatform.utils.CommonConstants.POSTS_PER_PAGE
import org.example.blogmultiplatform.utils.Constants.DATABASE_NAME
import org.example.blogmultiplatform.utils.Constants.MAIN_POSTS_LIMIT
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection

@InitApi
fun initMongoDB(context: InitApiContext) {
    println("Whatever this is!!! \n${System.getenv()}")

    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    System.getenv().forEach { (key, value) ->
        if (key == "MONGODB_URI") {
            context.data.add(MongoDB(context, connectionString = value))
        }
    }
}

class MongoDB(private val context: InitApiContext, private val connectionString: String) : MongoRepository {

    //    private val client = KMongo.createClient()
    private val client = KMongo.createClient(connectionString)
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>()
    private val postCollection = database.getCollection<Post>()
    private val newsletterCollection = database.getCollection<Newsletter>()

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).awaitFirst().wasAcknowledged()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection
            .updateOne(
                Post::id eq post.id,
                mutableListOf(
                    setValue(Post::title, post.title),
                    setValue(Post::subtitle, post.subtitle),
                    setValue(Post::category, post.category),
                    setValue(Post::thumbnail, post.thumbnail),
                    setValue(Post::content, post.content),
                    setValue(Post::main, post.main),
                    setValue(Post::popular, post.popular),
                    setValue(Post::sponsored, post.sponsored),
                )
            )
            .awaitLast()
            .wasAcknowledged()
    }

    override suspend fun readMyPosts(skip: Int, author: String): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::author eq author)
            .sort(descending(SimplePost::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readLatestPosts(skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(
                and(
                    SimplePost::popular eq false,
                    SimplePost::main eq false,
                    SimplePost::sponsored eq false,
                )
            )
            .sort(descending(SimplePost::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSponsoredPosts(): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::sponsored eq true)
            .sort(descending(SimplePost::date))
            .limit(2)
            .toList()
    }

    override suspend fun readPopularPosts(skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::popular eq true)
            .sort(descending(SimplePost::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readMainPosts(): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::main eq true)
            .sort(descending(SimplePost::date))
            .limit(MAIN_POSTS_LIMIT)
            .toList()
    }

    override suspend fun searchPostsByTitle(query: String, skip: Int): List<SimplePost> {
        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::title regex regexQuery)
            .sort(descending(SimplePost::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun searchPostsByCategory(category: Category, skip: Int): List<SimplePost> {
        return postCollection.withDocumentClass(SimplePost::class.java)
            .find(SimplePost::category eq category)
            .sort(descending(SimplePost::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection.find(Post::id eq id).toList().first()
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return postCollection.deleteMany(Post::id `in` ids)
            .awaitLast()
            .wasAcknowledged()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        User::username eq user.username,
                        User::password eq user.password,
                    )
                ).awaitFirst()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(User::id eq id).awaitFirst()
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }

    override suspend fun subscribe(newsletter: Newsletter): String {
        val result = newsletterCollection
            .find(Newsletter::email eq newsletter.email)
            .toList()

        return if (result.isNotEmpty()) {
            "You're already subscribed"
        } else {
            val newEmail = newsletterCollection.insertOne(newsletter)
                .awaitFirst()
                .wasAcknowledged()

            if (newEmail) {
                "Successfully Subscribed!"
            } else {
                "Something went wrong. Please try again later."
            }
        }
    }
}