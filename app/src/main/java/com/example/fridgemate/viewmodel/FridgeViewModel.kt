package com.example.fridgemate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FridgeViewModel : ViewModel() {
//    val foodList = mutableStateListOf<String>()
//
//    fun addFoodItems(items: List<String>) {
//        foodList.addAll(items)
//    }
    var foodItems = mutableStateListOf<String>()
        private set

    var tempFoodItems = mutableStateListOf<String>()
        private set

    fun addFoodItems(items: List<String>) {
        foodItems.clear()
        foodItems.addAll(items)
    }

    fun setTempFoodItems(items: List<String>) {
        tempFoodItems.clear()
        tempFoodItems.addAll(items)
    }
}