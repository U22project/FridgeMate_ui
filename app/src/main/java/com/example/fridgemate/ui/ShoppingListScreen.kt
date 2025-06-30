package com.example.fridgemate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fridgemate.components.BottomNavigationBar

@Composable
fun ShoppingListScreen(navController: NavController) {
    val items = remember { mutableStateListOf("にんじん", "牛乳", "パン", "卵") }
    val checkedStates = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        //bottomBar = { BottomNavigationBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 追加処理 */ }) {
                Text("＋")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "お買い物リスト",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items.size) { index ->
                    val item = items[index]
                    val checked = checkedStates[item] ?: false
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, fontWeight = FontWeight.SemiBold)
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { checkedStates[item] = it }
                            )
                        }
                    }
                }
            }
        }
    }
}
