package com.example.fridgemate.datamodel

data class FoodItem(
    var name: String = "",
    var quantity: Int = 1,
    var expireDate: String = ""  // yyyy/mm/dd
)
