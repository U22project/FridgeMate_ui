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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.fridgemate.datamodel.FoodItem

@Composable
fun EditFoodScreen(
    navController: NavController,
    fridgeViewModel: FridgeViewModel,
    modifier: Modifier = Modifier
) {
    val tempItems = remember { mutableStateListOf<FoodItem>() }
    LaunchedEffect(Unit) {
        tempItems.clear()
        tempItems.addAll(fridgeViewModel.tempFoodItems)
    }
    // 追加用の入力欄
    var newItemText: String by remember { mutableStateOf("") }


    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("ViewModelの中身: ${tempItems.joinToString()}", style = MaterialTheme.typography.bodySmall)
        Text("抽出された食材を編集", style = MaterialTheme.typography.titleMedium)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = newItemText,
                onValueChange = { newItemText = it },
                label = { Text("食材を追加") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newItemText.isNotBlank()) {
                        tempItems.add(
                            FoodItem(
                            name = newItemText.trim(),
                            quantity = 1, // デフォルトの個数
                            expireDate = "/" // デフォルトの賞味期限
                            )
                        )
                        newItemText = ""
                    }
                }
            ) {
                Text("追加")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            val editedItems = tempItems
            itemsIndexed(editedItems) { index: Int, item:FoodItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = item.name,
                        onValueChange = { editedItems[index] = item.copy(name = it) },
                        label = { Text("名前") },
                        modifier = Modifier.weight(1.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        value = item.quantity.toString(),
                        onValueChange = {
                            val newVal = it.toIntOrNull() ?: 1
                            editedItems[index] = item.copy(quantity = newVal)
                        },
                        label = { Text("個数") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        value = item.expireDate,
                        onValueChange = { input ->
                            if (Regex("""\d{0,2}/\d{0,2}""").matches(input)) {
                                editedItems[index] = item.copy(expireDate = input)
                            }
                        },
                        label = { Text("賞味期限mm/dd") },
                        modifier = Modifier.weight(1.5f)
                    )
                    IconButton(onClick = { editedItems.removeAt(index) }) {
                        Icon(Icons.Default.Delete, contentDescription = "削除")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val onBackClick = { navController.navigate("CameraScreen") }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Button(
                onClick = onBackClick,
            ) {
                Text("戻る")
            }

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
            ) {
                Text("保存して冷蔵庫へ")
            }
        }

    }
}