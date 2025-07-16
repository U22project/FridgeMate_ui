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
import com.example.fridgemate.ui.CameraScreen
import com.example.fridgemate.ui.EditFoodScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fridgemate.viewmodel.FridgeViewModel


@Composable
fun FridgeMateNavGraph(navController: NavHostController,fridgeViewModel: FridgeViewModel) {
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
        composable("CameraScreen") {
            CameraScreen(navController = navController,
                fridgeViewModel = fridgeViewModel
            )
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
        composable("edit_food") {
            EditFoodScreen(
                navController = navController,
                fridgeViewModel = fridgeViewModel
            )
        }

    }
}
