package com.example.memoriae.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.firestore.FireStoreClass
import com.example.memoriae.R
import com.example.memoriae.databinding.ActivityMemoriesDetailsBinding
import com.example.model.Memory
import com.example.model.WeatherObject
import com.example.network.WeatherApiImplementation
import com.example.utils.Base
import com.example.utils.Constants
import com.example.utils.GlideLoader
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MemoryDetailsActivity : Base() {

    private lateinit var mBinding : ActivityMemoriesDetailsBinding
    private  var mModel : Memory? = null
    private var city : String = " "
    private var isFavorite:String=" "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityMemoriesDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if(intent.hasExtra(Constants.MEMORY_DETAILS)) {
            mModel = intent.getParcelableExtra(Constants.MEMORY_DETAILS)!!
            GlideLoader(this).loadPicture(mModel!!.imageURL, mBinding.ivMemoryImage)
            mBinding.tvTitle.text = mModel!!.title
            mBinding.tvDescription.text = mModel!!.description
            mBinding.tvAddress.text = mModel!!.location
            mBinding.tvDate.text = mModel!!.date
            mBinding.cityName.text = mModel!!.cityName.uppercase(Locale.getDefault())
            city = mModel!!.cityName
            getWeatherData()

            when (mModel!!.rating) {
                1 -> {
                    mBinding.starOne.visibility = View.VISIBLE
                    mBinding.starTwo.visibility = View.GONE
                    mBinding.starThree.visibility = View.GONE
                    mBinding.starFour.visibility = View.GONE
                    mBinding.starFive.visibility = View.GONE
                }
                2 -> {
                    mBinding.starOne.visibility = View.VISIBLE
                    mBinding.starTwo.visibility = View.VISIBLE
                    mBinding.starThree.visibility = View.GONE
                    mBinding.starFour.visibility = View.GONE
                    mBinding.starFive.visibility = View.GONE
                }
                3 -> {
                    mBinding.starOne.visibility = View.VISIBLE
                    mBinding.starTwo.visibility = View.VISIBLE
                    mBinding.starThree.visibility = View.VISIBLE
                    mBinding.starFour.visibility = View.GONE
                    mBinding.starFive.visibility = View.GONE
                }
                4 -> {
                    mBinding.starOne.visibility = View.VISIBLE
                    mBinding.starTwo.visibility = View.VISIBLE
                    mBinding.starThree.visibility = View.VISIBLE
                    mBinding.starFour.visibility = View.VISIBLE
                    mBinding.starFive.visibility = View.GONE
                }
                else -> {
                    mBinding.starOne.visibility = View.VISIBLE
                    mBinding.starTwo.visibility = View.VISIBLE
                    mBinding.starThree.visibility = View.VISIBLE
                    mBinding.starFour.visibility = View.VISIBLE
                    mBinding.starFive.visibility = View.VISIBLE
                }
            }
        }


        mBinding.tvLocation.setOnClickListener{
            val strUri = "http://maps.google.com/maps?q= ${mModel!!.location}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
            startActivity(intent)
        }


        setUpToolbar()
    }


    private fun setUpToolbar() {
        setSupportActionBar(mBinding.memoryDetailToolbar)
        val actionBar=supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar.title="Memory Details"
        mBinding.memoryDetailToolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun getWeatherData() {
        val data : Call<WeatherObject.WeatherDetails> =WeatherApiImplementation.instance.getCityData(
            city,Constants.API_KEY_VALUE,"metric")

        data.enqueue(object : Callback<WeatherObject.WeatherDetails>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherObject.WeatherDetails>, response: Response<WeatherObject.WeatherDetails>) {
                val weatherData=response.body()

                if(weatherData==null){
                    mBinding.tvInvalid.visibility= View.VISIBLE
                    mBinding.tvClimate.visibility=View.GONE
                }else{
                    mBinding.tvInvalid.visibility= View.GONE
                    mBinding.tvClimate.visibility=View.VISIBLE

                    val climate=weatherData.weather[0].description
                    mBinding.tvClimate.text="${climate[0].toUpperCase()}${climate.substring(1)}  " +
                            "${weatherData.main.temp.toInt()}°C"

                }
            }

            override fun onFailure(call: Call<WeatherObject.WeatherDetails>, t: Throwable) {
                Log.e("Error","Error is fetching data",t)
            }

        })
    }
}