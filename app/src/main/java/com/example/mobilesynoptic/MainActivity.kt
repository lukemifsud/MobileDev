package com.example.mobilesynoptic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.jamesdecelis.com/"
class MainActivity : AppCompatActivity() {

    lateinit var vallettaTemp: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherValletta();
        getWeatherRome();
        getWeatherParis();
    }

    private fun getWeatherValletta() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherInterface::class.java)

        val retrofitData = retrofitBuilder.getWeatherValletta()

        retrofitData.enqueue(object : Callback<Valletta?> {
            override fun onResponse(call: Call<Valletta?>, response: Response<Valletta?>) {
                val responseBody = response.body()!!

                val myStringBuilder = StringBuilder()
                myStringBuilder.append(responseBody.condition)
                myStringBuilder.append(responseBody.temp)
                myStringBuilder.append(responseBody.name)




            }

            override fun onFailure(call: Call<Valletta?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getWeatherRome(){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherInterface::class.java)

        val retrofitData = retrofitBuilder.getWeatherRome()
    }

    private fun getWeatherParis(){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherInterface::class.java)

        val retrofitData = retrofitBuilder.getWeatherParis()
    }
}