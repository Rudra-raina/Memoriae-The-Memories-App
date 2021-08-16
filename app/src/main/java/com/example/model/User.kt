package com.example.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val userID : String = " ",
    val firstName : String = " ",
    val lastName : String = " ",
    val email : String =" "
) : Parcelable