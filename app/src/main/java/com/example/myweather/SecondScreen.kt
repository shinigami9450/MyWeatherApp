package com.example.myweather

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_second_screen.*
import org.json.JSONArray
import org.json.JSONObject
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.net.URL
import io.alterac.blurkit.BlurLayout
class SecondScreen : AppCompatActivity() {
    companion object {
        const val CITY_NAME = "city_name"
    }

    var blurLayout: BlurLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second_screen)
        load()
        blurLayout = findViewById(R.id.blurLayout)
    }
    override fun onStart() {
        super.onStart()
        blurLayout?.startBlur()
    }

    override fun onStop() {
        blurLayout?.pauseBlur()
        super.onStop()
    }
    fun BacktoHome(view: android.view.View) {
        val intent2=Intent(this, MainActivity::class.java)
        startActivity(intent2)
    }
    fun load()
    {
        progressBar.visibility=View.VISIBLE
        val city = intent.getStringExtra(CITY_NAME)
        cityName.text = "$city"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=1fdd7196c3034152a9e30271e9895034"
        val jsonObject = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                var jsonArray=response.getJSONArray("weather")
                var weatherObject=jsonArray.getJSONObject(0)
                var description=weatherObject.getString("main")
                var wimg=weatherObject.getString("icon")
                var tempObject=response.getJSONObject("main")
                var maintemp=tempObject.getString("temp")
                var feelstemp=tempObject.getString("feels_like")
                var mintemp=tempObject.getString("temp_min")
                var maxtemp=tempObject.getString("temp_max")
                Glide.with(this).load("https://openweathermap.org/img/w/${wimg}.png").into(Wicon)
                var bgurl="https://api.unsplash.com/search/photos?query=$description&client_id=CLvbE1byl0H0WlCXpj1HRVkuAGFkinceLxC8kb4JwrE"
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, bgurl, null,
                    { response2 ->
                        var bglink=response2.getJSONArray("results").getJSONObject((0..10).random()).getJSONObject("urls").getString("regular")
                        Log.d("unplash",bglink)
                        Glide.with(this).load(bglink).listener(object: RequestListener<Drawable>{

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility=View.GONE
                                return false
                            }

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility=View.GONE
                                return false
                            }

                        }).into(homeBG)
                    },
                    { error2 ->
                        Log.d("unplash","Inside unplash")
                    }
                )
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
                desc.text=description
                mainTemp.text=maintemp+"°"
                feelsTemp.text=feelstemp+"°"
                maxTemp.text=maxtemp+"°"
                minTemp.text=mintemp+"°"

            },
            { Log.d("error", it.localizedMessage) })

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObject)

    }
}