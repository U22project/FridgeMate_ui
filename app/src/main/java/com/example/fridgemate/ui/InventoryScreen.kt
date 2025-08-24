package com.example.fridgemate.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import com.example.fridgemate.BuildConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color

private const val serverUrl = BuildConfig.SERVER_URL + "/get_food_items"
private const val deleteUrl = BuildConfig.SERVER_URL + "/delete_food_item"

data class FoodItem(
    val ingredients: String,
    val expiration_date: String,
    val quantity: Int
)

@Composable
fun InventoryScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("冷蔵庫")
    var foodItems by remember { mutableStateOf(listOf<FoodItem>()) }

    // APIから食材リストを取得
    LaunchedEffect(Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(serverUrl)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // エラー処理（必要ならログ出力）
            }
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { body ->
                    val jsonArray = JSONArray(body)
                    val items = mutableListOf<FoodItem>()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        items.add(
                            FoodItem(
                                ingredients = obj.optString("ingredients", ""),
                                expiration_date = obj.optString("expiration_date", ""), // ここを修正
                                quantity = obj.optInt("quantity", 0)
                            )
                        )
                    }
                    Handler(Looper.getMainLooper()).post {
                        foodItems = items
                    }
                }
            }
        })
    }

    fun fetchFoodItems() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(serverUrl)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { body ->
                    val jsonArray = JSONArray(body)
                    val items = mutableListOf<FoodItem>()
                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        items.add(
                            FoodItem(
                                ingredients = obj.getString("ingredients"),
                                expiration_date = obj.getString("expiration_date"),
                                quantity = obj.getInt("quantity")
                            )
                        )
                    }
                    Handler(Looper.getMainLooper()).post {
                        foodItems = items
                    }
                }
            }
        })
    }

    // 削除処理
    fun deleteItem(item: FoodItem) {
        val client = OkHttpClient()
        val json = """
            {
                "ingredients": "${item.ingredients}",
                "expiration_date": "${item.expiration_date}",
                "quantity": ${item.quantity}
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(deleteUrl)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                response.close()
                Handler(Looper.getMainLooper()).post {
                    fetchFoodItems()
                }
            }
        })
    }

    LaunchedEffect(Unit) {
        fetchFoodItems()
    }

    Scaffold(
        //bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("商品名", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("個数", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("賞味期限", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(7.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foodItems.size) { index ->
                    val item = foodItems[index]
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.ingredients, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Text("${item.quantity}個", modifier = Modifier.weight(1f))
                            Text(item.expiration_date, modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { deleteItem(item) },
                                modifier = Modifier
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "削除",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}