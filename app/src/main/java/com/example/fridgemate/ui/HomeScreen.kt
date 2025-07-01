package com.example.fridgemate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fridgemate.components.BottomNavigationBar

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        //bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
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
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Profile"
//                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "今日のおすすめのレシピ",
                style = MaterialTheme.typography.titleMedium
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        Text(
                            text = "画像",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("しょうが焼き", fontWeight = FontWeight.SemiBold)
                    Text("10分", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 4.dp)
                            ) {
                                Text("🥕", modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("賞味期限切れ寸前の食材", style = MaterialTheme.typography.titleMedium)
            LazyRow {
                items(5) { _ ->
                    Surface(
                        modifier = Modifier
                            .size(width = 120.dp, height = 80.dp)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("卵", fontWeight = FontWeight.Bold)
                            Text("2024/06/27", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
