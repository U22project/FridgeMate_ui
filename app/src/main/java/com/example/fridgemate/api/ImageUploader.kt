package com.example.fridgemate.api

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import com.example.fridgemate.BuildConfig


object ImageUploader {

    private const val TAG = "ImageUploader"
    private const val serverUrl = BuildConfig.SERVER_URL + "/test_ocr"
    fun sendImageToServer(imageFile: File, onResult: (String?) -> Unit) {
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image", imageFile.name,
                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url(serverUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "送信失敗: ${e.message}")
                onResult(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.e(TAG, "サーバーエラー: ${response.message}")
                        onResult(null)
                        return
                    }

                    val body = response.body?.string()
                    val text = JSONObject(body ?: "{}").optString("text")
                    onResult(text)
                }
            }
        })
    }
}