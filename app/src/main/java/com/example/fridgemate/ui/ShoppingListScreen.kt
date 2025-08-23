package com.example.fridgemate.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fridgemate.datastore.ShoppingListManager
import kotlinx.coroutines.launch

@Composable
fun ShoppingListScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val shoppingList = remember { mutableStateListOf<String>() }
    var newItem by remember { mutableStateOf("") }

    // 初回読み込み
    LaunchedEffect(Unit) {
        val saved = ShoppingListManager.loadList(context)
        shoppingList.addAll(saved)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("買い物メモ", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(shoppingList) { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(
                        text = item,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    IconButton(onClick = {
                        shoppingList.removeAt(index)
                        coroutineScope.launch {
                            ShoppingListManager.saveList(context, shoppingList)
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "削除")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            TextField(
                value = newItem,
                onValueChange = { newItem = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("メモを追加") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newItem.isNotBlank()) {
                    shoppingList.add(newItem.trim())
                    newItem = ""
                    coroutineScope.launch {
                        ShoppingListManager.saveList(context, shoppingList)
                    }
                }
            }) {
                Text("追加")
            }
        }
    }
}


