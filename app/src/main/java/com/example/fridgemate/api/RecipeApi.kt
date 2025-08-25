package com.example.fridgemate.api

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.math.log

data class RecipeItem(
    val title: String,
    val imageUrl: String,
    val recipeUrl: String
)

object RecipeApi {
    private const val BASE_URL =
        "https://app.rakuten.co.jp/services/api/Recipe/CategoryRanking/20170426"
    private const val APP_ID = "1081684173276999312" // ← 自分のキーに差し替える

    private val client = OkHttpClient()

    fun fetchRecipes(onResult: (List<RecipeItem>) -> Unit) {
        val url = "$BASE_URL?applicationId=$APP_ID"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                val result = mutableListOf<RecipeItem>()

                val json = JSONObject(response.body?.string() ?: "")
                val recipes = json.getJSONArray("result")

                for (i in 0 until recipes.length()) {
                    val item = recipes.getJSONObject(i)
                    result.add(
                        RecipeItem(
                            title = item.getString("recipeTitle"),
                            imageUrl = item.getString("foodImageUrl"),
                            recipeUrl = item.getString("recipeUrl")
                        )
                    )
                }
                onResult(result)
            }
        })
    }
}
