package edu.skku.cs.week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val et = findViewById<EditText>(R.id.editText)

        val client = OkHttpClient()
        val host = "https://api.tvmaze.com"

        btn.setOnClickListener {
            val path = "/search/shows?q=" + et.text
            val req = Request.Builder().url(host+path).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val data = response.body!!.string()

                        val typeToken = object : TypeToken<List<Show>>() {}.type
                        val id = Gson().fromJson<List<Show>>(data, typeToken)[0].show?.id

                        val path2 = "/shows/" + id.toString() + "/episodes"
                        val req = Request.Builder().url(host+path2).build()
                        client.newCall(req).enqueue(object : Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use{
                                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                                    val data = response.body!!.string()

                                    val typeToken = object : TypeToken<List<Episode>>() {}.type
                                    val episodeList = Gson().fromJson<List<Episode>>(data, typeToken)

                                    CoroutineScope(Dispatchers.Main).launch {
                                        val listAdapter = ListViewAdapter(this@MainActivity, episodeList)
                                        var listView = findViewById<ListView>(R.id.listView)
                                        listView.adapter = listAdapter
                                    }
                                }
                            }
                        })
                    }
                }
            })
        }
    }
}