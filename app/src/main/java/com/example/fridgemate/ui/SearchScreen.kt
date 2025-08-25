package com.example.fridgemate.ui

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fridgemate.BuildConfig
import com.example.fridgemate.api.RecipeItem
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

private const val foodUrl = BuildConfig.SERVER_URL + "/get_food_items"
private const val categoryUrl = BuildConfig.SERVER_URL + "/get_recipe_categories"
private const val recipeSearchUrl = "https://app.rakuten.co.jp/services/api/Recipe/CategoryRanking/20170426"
private const val applicationId = "1081684173276999312"

@Composable
fun SearchScreen(navController: NavController) {
    var foodItems by remember { mutableStateOf(listOf<String>()) }
    var categoryMap by remember { mutableStateOf(mapOf<String, String>()) }
    var recipeResults by remember { mutableStateOf(listOf<RecipeItem>()) }

    LaunchedEffect(Unit) {
        fetchFoodItems { items -> foodItems = items }
        fetchRecipeCategories { map -> categoryMap = map }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    val matchedCategoryIds = matchRandomCategoriesByFood(foodItems, categoryMap)
                    fetchRecipesByCategoryIds(matchedCategoryIds) { results ->
                        recipeResults = results
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("レシピを検索")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("冷蔵庫を基にレシピを検索", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text("レシピ結果▼", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))

                }

                items(recipeResults) { recipe ->
                    RecipeCard(recipe)
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: RecipeItem) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.recipeUrl))
                context.startActivity(intent)
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(recipe.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "▶ レシピを開く",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
    }
}

fun fetchFoodItems(onResult: (List<String>) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder().url(foodUrl).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("❌ 食材取得失敗: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val items = mutableListOf<String>()
            response.body?.string()?.let { body ->
                try {
                    val jsonArray = JSONArray(body)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val ingredient = obj.getString("ingredients")
                        val quantity = obj.getString("quantity")
                        items.add("$ingredient x$quantity")
                    }
                } catch (e: Exception) {
                    println("❌ JSONエラー: ${e.message}")
                }
            }
            Handler(Looper.getMainLooper()).post {
                onResult(items)
            }
        }
    })
}

fun fetchRecipeCategories(onResult: (Map<String, String>) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder().url(categoryUrl).build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("❌ カテゴリ取得失敗: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val resultMap = mutableMapOf<String, String>()
            response.body?.string()?.let { body ->
                try {
                    val jsonArray = JSONArray(body)
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val id = obj.getString("category_id")
                        val name = obj.getString("category_name")
                        resultMap[name] = id
                    }
                } catch (e: Exception) {
                    println("❌ カテゴリJSON解析エラー: ${e.message}")
                }
            }
            Handler(Looper.getMainLooper()).post {
                onResult(resultMap)
            }
        }
    })
}

fun matchRandomCategoriesByFood(
    foodItems: List<String>,
    categoryMap: Map<String, String>
): Set<String> {
    val matched = mutableSetOf<String>()
    for (food in foodItems) {
        val candidates = mutableListOf<String>()
        for ((catName, catId) in categoryMap) {
            val catSubstrings = generateSubstrings(catName, 2)
            if (catSubstrings.any { food.contains(it) }) {
                candidates.add(catId)
            }
        }
        if (candidates.isNotEmpty()) {
            matched.add(candidates.random())
        }
    }
    return matched
}

fun generateSubstrings(text: String, minLength: Int): List<String> {
    val result = mutableListOf<String>()
    for (i in 0..text.length - minLength) {
        for (j in (i + minLength)..text.length) {
            result.add(text.substring(i, j))
        }
    }
    return result
}

fun fetchRecipesByCategoryIds(categoryIds: Set<String>, onResult: (List<RecipeItem>) -> Unit) {
    val client = OkHttpClient()
    val results = mutableListOf<RecipeItem>()
    var completed = 0

    if (categoryIds.isEmpty()) {
        onResult(emptyList())
        return
    }

    for (categoryId in categoryIds) {
        val url = "$recipeSearchUrl?applicationId=$applicationId&categoryId=$categoryId"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                completed++
                if (completed == categoryIds.size) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(results)
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val json = JSONObject(response.body?.string() ?: "")
                    val array = json.getJSONArray("result")
                    for (i in 0 until minOf(3, array.length())) {
                        val item = array.getJSONObject(i)
                        results.add(
                            RecipeItem(
                                title = item.getString("recipeTitle"),
                                imageUrl = item.getString("foodImageUrl"),
                                recipeUrl = item.getString("recipeUrl")
                            )
                        )
                    }
                } catch (e: Exception) {
                    println("❌ レシピ解析エラー: ${e.message}")
                }
                completed++
                if (completed == categoryIds.size) {
                    Handler(Looper.getMainLooper()).post {
                        onResult(results)
                    }
                }
            }
        })
    }
}
