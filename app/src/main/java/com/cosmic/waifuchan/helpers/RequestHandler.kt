package com.cosmic.waifuchan.helpers

import com.cosmic.waifuchan.models.NsfwModel
import com.cosmic.waifuchan.models.SfwModel
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

interface RequestHandler {
    suspend fun getSfwPics(exclude: List<String>, category: String = "waifu"): SfwModel
    suspend fun getNsfwPics(exclude: List<String>, category: String = "neko"): NsfwModel
}

@Serializable
data class ExcludeJson(val exclude: List<String>)

class RequestHandlerImpl(private val client: ApiClient): RequestHandler {
    override suspend fun getSfwPics(exclude: List<String>, category: String): SfwModel {
        val result = client.client.post(
            "https://api.waifu.pics/many/sfw/$category"
        ){
            setBody(ExcludeJson(exclude = emptyList()))
        }
        println(result.body<String>())
        return result.body<SfwModel>()
    }

    override suspend fun getNsfwPics(exclude: List<String>, category: String): NsfwModel {
        println(category)
        val result = client.client.post(
            "https://api.waifu.pics/many/nsfw/$category"
        ){
            setBody(ExcludeJson(exclude = emptyList()))
        }
        println(result.body<String>())
        return result.body<NsfwModel>()
    }
}