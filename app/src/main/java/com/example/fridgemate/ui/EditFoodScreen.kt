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


@Composable
fun EditFoodScreen(
    navController: NavController,
    fridgeViewModel: FridgeViewModel,
    modifier: Modifier = Modifier
) {
    val tempItems = remember { mutableStateListOf<String>() }

    LaunchedEffect(fridgeViewModel.tempFoodItems) {
        tempItems.clear()
        tempItems.addAll(fridgeViewModel.tempFoodItems)
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // EditFoodScreen.kt 内
        Text("ViewModelの中身: ${fridgeViewModel.tempFoodItems.joinToString()}", style = MaterialTheme.typography.bodySmall)
        //Text("ViewModelの中身: ${fridgeViewModel.tempFoodItems.joinToString()}", style = MaterialTheme.typography.bodySmall)
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
                // 保存して ViewModel に渡す
                fridgeViewModel.addFoodItems(tempItems.toList())
//                navController.navigate("fridge")

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
