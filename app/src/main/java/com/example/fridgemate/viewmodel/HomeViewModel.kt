package com.example.fridgemate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fridgemate.api.RecipeApi
import com.example.fridgemate.api.RecipeItem

class HomeViewModel : ViewModel() {
    private val _recipes = mutableStateListOf<RecipeItem>()
    val recipes: List<RecipeItem> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        RecipeApi.fetchRecipes { items ->
            _recipes.clear()
            _recipes.addAll(items)
        }
    }
}