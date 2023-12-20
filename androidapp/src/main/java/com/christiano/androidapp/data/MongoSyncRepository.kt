package com.christiano.androidapp.data

import com.christiano.androidapp.models.Post
import com.christiano.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<Post>>>
}