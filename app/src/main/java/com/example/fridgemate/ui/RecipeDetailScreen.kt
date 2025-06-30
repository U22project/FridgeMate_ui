package com.example.fridgemate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fridgemate.components.BottomNavigationBar

@Composable
fun RecipeDetailScreen(navController: NavController) {
    Scaffold(
        //bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "さばの味噌煮",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("画像")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("材料", fontWeight = FontWeight.SemiBold)
            Text("さば、味噌、砂糖、みりん", fontSize = 14.sp, modifier = Modifier.padding(bottom = 16.dp))

            Text("手順", fontWeight = FontWeight.SemiBold)
            Text("1. 材料を用意する\n2. 鍋で煮る\n3. 味を整える", fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* 作る処理 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("作る", fontSize = 16.sp)
            }
        }
    }
}
