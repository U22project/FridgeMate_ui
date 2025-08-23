package com.example.fridgemate.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "shopping_list")

object ShoppingListManager {
    private val LIST_KEY = stringSetPreferencesKey("shopping_items")

    suspend fun saveList(context: Context, items: List<String>) {
        context.dataStore.edit { prefs ->
            prefs[LIST_KEY] = items.toSet()
        }
    }

    suspend fun loadList(context: Context): List<String> {
        val prefs = context.dataStore.data.first()
        return prefs[LIST_KEY]?.toList() ?: emptyList()
    }
}
