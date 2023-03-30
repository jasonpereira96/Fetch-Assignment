package com.example.fetch.network

import com.example.fetch.models.Item
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class NetworkManager() {

    private val client = OkHttpClient()

    fun fetch(cbk: (items: List<Item>) -> Unit) {
        val request = Request.Builder()
            .url("https://fetch-hiring.s3.amazonaws.com/hiring.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                var i = 0
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val json = response.body()?.string()
                    println(json)
                    if (json != null) {
                        val gson = Gson()
                        val items: Array<Item> = gson.fromJson(
                            json,
                            Array<Item>::class.java
                        )
                        cbk(items.asList()
                            .filter { item -> !item.name.equals("") && item.name != null }
                            .sortedWith(Comparator<Item>{ a, b ->
                                when {
                                    a.listId == b.listId -> a.name!!.compareTo(b.name.toString())
                                    else -> a.listId - b.listId
                                }
                            })
                        )
                    }

                }
            }
        })
    }
}