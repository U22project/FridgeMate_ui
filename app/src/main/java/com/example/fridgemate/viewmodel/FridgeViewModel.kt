package com.example.fridgemate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.fridgemate.datamodel.FoodItem

class FridgeViewModel : ViewModel() {
//    val foodList = mutableStateListOf<String>()
//
//    fun addFoodItems(items: List<String>) {
//        foodList.addAll(items)
//    }
var foodItems = mutableStateListOf<FoodItem>()
    private set

    private var _tempFoodItems = listOf<FoodItem>()
    val tempFoodItems: List<FoodItem> get() = _tempFoodItems

    fun addFoodItems(items: List<FoodItem>) {
        foodItems.clear()
        foodItems.addAll(items)
    }

    fun setTempFoodItems(items: List<FoodItem>) {
        _tempFoodItems = items
    }
}
