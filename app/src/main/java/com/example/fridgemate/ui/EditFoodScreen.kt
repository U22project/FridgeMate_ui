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
import android.widget.Toast

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.fridgemate.datamodel.FoodItem
import com.example.fridgemate.BuildConfig

private const val serverUrl = BuildConfig.SERVER_URL + "/add_food_items"
// 入力途中は 0〜2桁 + 任意の「/」+ 0〜2桁 を許容
private val inputRegex = Regex("""\d{0,2}(/(\d{0,2})?)?""")

// 保存時は MM/DD（ゼロ埋め必須）を厳格チェック
private val strictMmDdRegex = Regex("""^(0[1-9]|1[0-2])/(0[1-9]|[12]\d|3[01])$""")

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
                            expireDate = "" // デフォルトの賞味期限
                            )
                        )
                        newItemText = ""
                    }
                }
            ) {
                Text("追加")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("商品名", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("個数", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Text("賞味期限", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            val editedItems = tempItems
            itemsIndexed(editedItems) { index: Int, item:FoodItem ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = item.name,
                        onValueChange = { editedItems[index] = item.copy(name = it) },
                        modifier = Modifier.weight(1.5f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        value = item.quantity.toString(),
                        onValueChange = {
                            val newVal = it.toIntOrNull() ?: 1
                            editedItems[index] = item.copy(quantity = newVal)
                        },
//                        label = { Text("個数") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextField(
                        value = item.expireDate,
                        onValueChange = { input ->
                            var newInput = input

                            // 4桁ぴったり数字が入力された時だけ MM/DD に整形
                            if (Regex("""^\d{4}$""").matches(newInput)) {
                                newInput = newInput.substring(0, 2) + "/" + newInput.substring(2, 4)
                            }

                            // 入力途中（0〜2桁 + / + 0〜2桁）は許可
                            if (inputRegex.matches(newInput)) {
                                editedItems[index] = item.copy(expireDate = newInput)
                            }
                        },
//                        label = { Text("賞味期限mm/dd") },
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
                    // 保存時の厳格チェック（MM/DD 必須）
                    val nameInvalid = tempItems.any { it.name.isBlank() }
                    val invalid = tempItems.any { it.expireDate.isNotBlank() && !strictMmDdRegex.matches(it.expireDate) }

                    when {
                        nameInvalid -> {
                            Toast.makeText(
                                navController.context,
                                "保存エラー: 食材名が空欄のものがあります",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        invalid -> {
                            Toast.makeText(
                                navController.context,
                                "保存エラー: 賞味期限は MM/DD 形式（例: 01/01）で入力してください",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            val client = OkHttpClient()
                            val jsonArray = JSONArray()
                            tempItems.forEach { item ->
                                val jsonObj = org.json.JSONObject().apply {
                                    put("name", item.name)
                                    put("quantity", item.quantity)
                                    put("expireDate", item.expireDate)
                                }
                                jsonArray.put(jsonObj)
                            }

                            val json = jsonArray.toString()
                            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
                            val request = Request.Builder()
                                .url(serverUrl)
                                .post(requestBody)
                                .build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    Log.e("API", "POST失敗: ${e.message}")
                                }
                                override fun onResponse(call: Call, response: Response) {
                                    Log.d("API", "POST成功: ${response.body?.string()}")
                                } })

                            fridgeViewModel.addFoodItems(tempItems.toList())
                            Handler(Looper.getMainLooper()).post {
                                navController.navigate("inventory")
                            }
                        }
                    }
                },
            ) { Text("冷蔵庫に入れる") }

        }

    }
}