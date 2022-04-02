package com.example.myweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.webkit.URLUtil
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_second_screen.*
import org.json.JSONStringer
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
    }

    fun showWeather(view: android.view.View) {
        var name = cityInput.editableText.toString()
        var url = "https://api.openweathermap.org/data/2.5/weather?q=$name&units=metric&appid=1fdd7196c3034152a9e30271e9895034"
        var b=false
        b= URLUtil.isValidUrl(url)
        if(b==true)
        {
            try {
                val xyz = StringRequest(
                    Request.Method.POST, url,
                    { response ->
                        //var check1 = response.getString("cod")
                        if (response.toString() == "404") {
                            Log.d("inside", response.toString())
                            Toast.makeText(this, "City not found", Toast.LENGTH_LONG).show()
                        } else {
                            //Toast.makeText(this,"Code is $check1",Toast.LENGTH_LONG).show()
                            val intent = Intent(this, SecondScreen::class.java)
                            intent.putExtra(SecondScreen.CITY_NAME, name)
                            startActivity(intent)
                        }
                    },
                    { error ->
                       Log.d("errorout", error.localizedMessage)
                    })
                MySingleton.getInstance(this).addToRequestQueue(xyz)
            }
            catch(e:Exception)
            {
                Log.d("catch", "City not found")
                Toast.makeText(this,"City not found",Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            Log.d("catch", "City not found")
            Toast.makeText(this,"City not found",Toast.LENGTH_LONG).show()
        }
    }
}