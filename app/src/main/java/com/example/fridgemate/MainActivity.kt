package com.example.fridgemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.fridgemate.components.BottomNavigationBar
import com.example.fridgemate.ui.FridgeMateNavGraph
import com.example.fridgemate.ui.theme.FridgeMateTheme // ← これが必要

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FridgeMateApp()
        }
    }
}

@Composable
fun FridgeMateApp() {
    FridgeMateTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            // padding を使って画面全体に適用
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                FridgeMateNavGraph(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FridgeMateApp()
}
