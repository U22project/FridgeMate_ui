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

    private var _tempFoodItems = mutableStateListOf<String>() // Gemini結果を一時保持
    val tempFoodItems: List<String> get() = _tempFoodItems

    fun addFoodItems(items: List<String>) {
        foodItems.clear()
        foodItems.addAll(items)
    }

    fun setTempFoodItems(items: List<String>) {
        _tempFoodItems.clear()
        _tempFoodItems.addAll(items)
    }

}