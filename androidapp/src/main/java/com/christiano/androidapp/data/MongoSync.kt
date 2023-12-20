package com.christiano.androidapp.data

import com.christiano.androidapp.models.Post
import com.christiano.androidapp.util.Constants.APP_ID
import com.christiano.androidapp.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object MongoSync : MongoSyncRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user == null) return

        val config = SyncConfiguration.Builder(user, setOf(Post::class))
            .initialSubscriptions {
                add(
                    query = it.query(Post::class),
                    name = "Blog Posts"
                )
            }
            .log(LogLevel.ALL)
            .build()
        realm = Realm.open(config)
    }

    override fun readAllPosts(): Flow<RequestState<List<Post>>> {
        if (user == null) return flow { emit(RequestState.Error(Exception("User not authenticated."))) }

        return try {
            realm.query<Post>()
                .asFlow()
                .map { result ->
                    RequestState.Success(data = result.list)
                }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(e)) }
        }
    }

    override fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>> {
        if (user == null) return flow { emit(RequestState.Error(Exception("User not authenticated."))) }

        return try {
            realm.query<Post>(query = "title CONTAINS[c] $0", query)
                .asFlow()
                .map { result ->
                    RequestState.Success(data = result.list)
                }
        } catch (e: Exception) {
            flow { emit(RequestState.Error(e)) }
        }
    }
}