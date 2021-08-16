package com.example.utils

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.memoriae.R
import com.google.android.material.snackbar.Snackbar

open class Base : AppCompatActivity() {

    private lateinit var mProgressDialog : Dialog

    fun showErrorSnackBar(message:String){
        val snackBar : Snackbar = Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(ContextCompat.getColor(this,R.color.white))

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_color_one))
        snackBar.show()
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(this,R.style.myDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressBar(){
        mProgressDialog.dismiss()
    }
}