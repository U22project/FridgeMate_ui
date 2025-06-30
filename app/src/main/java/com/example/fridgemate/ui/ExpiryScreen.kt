package com.example.fridgemate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fridgemate.components.BottomNavigationBar // ← これを追加

@Composable
fun ExpiryScreen(navController: NavController) {
    Scaffold(
       // bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("賞味期限切れ一覧", style = MaterialTheme.typography.headlineSmall)

            val expiredItems = listOf(
                "卵 - 2025/06/20",
                "牛乳 - 2025/06/25",
                "レタス - 2025/06/22"
            )

            LazyColumn {
                items(expiredItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
