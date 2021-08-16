package com.example.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorites (
    val userID:String=" ",
    val fav : String=" ",
    val userName:String =" ",
    val title : String =" ",
    val description : String =" ",
    val cityName : String =" ",
    val date : String = " ",
    val location : String =" ",
    val imageURI : String = " ",
    val imageURL : String =" ",
    val privacy : String =" ",
    val rating : Int = 0,
    val memoryID : String =" "
):Parcelable