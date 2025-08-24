package com.example.fridgemate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fridgemate.components.BottomNavigationBar
import com.example.fridgemate.viewmodel.FridgeViewModel
import com.example.fridgemate.viewmodel.HomeViewModel
import com.example.fridgemate.viewmodel.ExpireDataViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.lazy.items


@Composable
fun HomeScreen(navController: NavController,fridgeViewModel: FridgeViewModel = viewModel(),expireDataViewModel: ExpireDataViewModel = viewModel()) {
    //
    val homeViewModel: HomeViewModel = viewModel<HomeViewModel>()
    val recipes = homeViewModel.recipes
    val expiringFoods = expireDataViewModel.expiringFoods.collectAsState().value

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
                            AsyncImage(  // Coilライブラリ必要
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

            Text("賞味期限切れ寸前の食材", style = MaterialTheme.typography.titleMedium)
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
                            Text(food.expire_date, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
