package com.example.fridgemate.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fridgemate.viewmodel.FridgeViewModel

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.LaunchedEffect

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import android.util.Log

@Composable
fun EditFoodScreen(
    navController: NavController,
    fridgeViewModel: FridgeViewModel,
    modifier: Modifier = Modifier
) {
    val tempItems = remember { mutableStateListOf<String>() }
    LaunchedEffect(Unit) {
        tempItems.clear()
        tempItems.addAll(fridgeViewModel.tempFoodItems)
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("ViewModelの中身: ${tempItems.joinToString()}", style = MaterialTheme.typography.bodySmall)
        Text("抽出された食材を編集", style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(tempItems) { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = item,
                        onValueChange = { tempItems[index] = it },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { tempItems.removeAt(index) }) {
                        Icon(Icons.Default.Delete, contentDescription = "削除")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val client = OkHttpClient()
                val json = JSONArray(tempItems).toString()
                val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    .url("http://192.168.50.77:5000/add_food_items")
                    .post(requestBody)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("API", "POST失敗: ${e.message}")
                    }
                    override fun onResponse(call: Call, response: Response) {
                        Log.d("API", "POST成功: ${response.body?.string()}")
                    }
                })

                fridgeViewModel.addFoodItems(tempItems.toList())
                Handler(Looper.getMainLooper()).post {
                    navController.navigate("home")
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("保存して冷蔵庫へ")
        }
    }
}