package com.example.network

import com.example.model.WeatherObject
import com.example.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {

    @GET(Constants.END_POINT)
    fun getCityData(
        @Query(Constants.QUERY_CITY_NAME) city : String,
        @Query(Constants.QUERY_API_KEY) apiKey : String,
        @Query(Constants.UNITS) units: String
    ) : Call<WeatherObject.WeatherDetails>
}

object WeatherApiImplementation{
    val instance :WeatherApiInterface
    init{
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        instance=retrofit.create(WeatherApiInterface::class.java)
    }
}