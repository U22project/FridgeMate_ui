package com.example.fridgemate.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fridgemate.viewmodel.FridgeViewModel
import com.example.fridgemate.viewmodel.HomeViewModel
import com.example.fridgemate.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

// --- データクラスを直書き ---
data class ExpiringFood(val name: String, val expire_date: String)

@Composable
fun HomeScreen(
    navController: NavController,
    fridgeViewModel: FridgeViewModel = viewModel(),
) {
    val homeViewModel: HomeViewModel = viewModel()
    val recipes = homeViewModel.recipes

    // --- ExpireDataViewModel 相当を直書き ---
    var expiringFoods by remember { mutableStateOf<List<ExpiringFood>>(emptyList()) }
    LaunchedEffect(Unit) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(BuildConfig.SERVER_URL + "/expiring_soon")
                .build()

            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                val body = response.body?.string()
                Log.d("HomeScreen", "APIレスポンス: $body")

                val jsonArray = JSONArray(body)
                val result = List(jsonArray.length()) { i ->
                    val obj = jsonArray.getJSONObject(i)
                    ExpiringFood(
                        name = obj.getString("ingredients"),
                        expire_date = obj.getString("expiration_date")
                    )
                }
                expiringFoods = result
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "エラー: ${e.message}")
        }
    }
    // --- ここまで ---

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FridgeMate",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "今日のおすすめのレシピ",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow {
                items(recipes.size) { i ->
                    val item = recipes[i]
                    Surface(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 2.dp
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            )
                            Text(item.title, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("賞味期限の近い食材", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(expiringFoods) { food ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(food.name, fontWeight = FontWeight.Bold)
                            Text(food.expire_date, fontSize = 14.sp, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
