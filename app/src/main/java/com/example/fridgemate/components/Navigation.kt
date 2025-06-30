package com.example.fridgemate.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fridgemate.ui.HomeScreen
import com.example.fridgemate.ui.SearchScreen
import com.example.fridgemate.ui.ShoppingListScreen
import com.example.fridgemate.ui.RecipeListScreen
import com.example.fridgemate.ui.RecipeDetailScreen
import com.example.fridgemate.ui.InventoryScreen
import com.example.fridgemate.ui.ExpiryScreen


@Composable
fun FridgeMateNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("shopping") {
            ShoppingListScreen(navController = navController)
        }
        composable("recipes") {
            RecipeListScreen(navController = navController)
        }
        composable("recipe_detail") {
            RecipeDetailScreen(navController = navController)
        }
        composable("inventory") {
            InventoryScreen(navController = navController)
        }
        composable("expiry") {
            ExpiryScreen(navController = navController)
        }

    }
}
