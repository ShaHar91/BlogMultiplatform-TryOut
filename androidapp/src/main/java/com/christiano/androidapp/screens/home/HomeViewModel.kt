package com.christiano.androidapp.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christiano.androidapp.data.MongoSync
import com.christiano.androidapp.models.Post
import com.christiano.androidapp.util.Constants.APP_ID
import com.christiano.androidapp.util.RequestState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _allPosts: MutableState<RequestState<List<Post>>> = mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts

    init {
        viewModelScope.launch {
            Log.d("TAG", "Hello--")
            App.create(APP_ID).login(Credentials.anonymous())
            Log.d("TAG", "Maybe--")
            fetchAllPosts()
            Log.d("TAG", "Bye!!")
        }
    }

    private fun fetchAllPosts() {
        viewModelScope.launch {
            MongoSync.readAllPosts().collectLatest { _allPosts.value = it }
        }
    }
}