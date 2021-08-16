package com.example.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Memory(
    val userName:String =" ",
    val userID : String =" ",
    val title : String =" ",
    val description : String =" ",
    val cityName : String =" ",
    val date : String = " ",
    val location : String =" ",
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val imageURI : String = " ",
    val imageURL : String =" ",
    val privacy : String =" ",
    val rating : Int = 0,
    val memoryID : String =" "
) : Parcelable
