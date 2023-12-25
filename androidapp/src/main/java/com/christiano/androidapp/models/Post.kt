package com.christiano.androidapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.example.blogmultiplatform.Category

@Serializable
open class Post : RealmObject {
    @PrimaryKey
    var _id: String = ""
    var author: String = ""
    var date: Long = 0L
    var title: String = ""
    var subtitle: String = ""
    var thumbnail: String = ""
    var category: String = Category.Programming.name

    companion object {
        fun previewData() = Post().also {
            it._id = "123"
            it.author = "Stefan"
            it.date = 1690805975525L
            it.title = "Jetpack Compose Animations"
            it.subtitle = "Jetpack Compose has been promoted from alpha to beta some time ago, which means the API’s last breaking changes are up. One of the greatest changes we can find is in its animations interface, which, in general, makes animations easier to understand, to read, and to build."
            it.thumbnail = "https://miro.medium.com/v2/resize:fit:1200/1*En1_cMdaA5XEm6od4niiNw.jpeg"
            it.category = Category.Design.name
        }
    }

    fun decodeThumbnailImage(): Bitmap? {
        return try {
            val byteArray = Base64.decode(cleanupImageString(thumbnail), Base64.NO_WRAP)
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            null
        }
    }

    private fun cleanupImageString(value: String): String {
        return value
            .replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
    }
}

