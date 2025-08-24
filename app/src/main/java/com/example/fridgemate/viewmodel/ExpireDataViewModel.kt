package com.example.fridgemate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import com.example.fridgemate.BuildConfig

private const val serverUrl = BuildConfig.SERVER_URL + "/expiring_soon"
data class ExpiringFood(val name: String, val expire_date: String)

class ExpireDataViewModel : ViewModel() {

    private val _expiringFoods = MutableStateFlow<List<ExpiringFood>>(emptyList())
    val expiringFoods: StateFlow<List<ExpiringFood>> get() = _expiringFoods

    fun fetchExpiringFoods() {
        viewModelScope.launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(serverUrl)
                    .build()

                val response = client.newCall(request).execute()
                val jsonArray = JSONArray(response.body?.string())

                val result = List(jsonArray.length()) { i ->
                    val obj = jsonArray.getJSONObject(i)
                    ExpiringFood(
                        name = obj.getString("name"),
                        expire_date = obj.getString("expire_date")
                    )
                }

                _expiringFoods.value = result

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
