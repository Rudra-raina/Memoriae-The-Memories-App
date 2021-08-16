package com.example.memoriae.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.memoriae.databinding.ActivityForgotPasswordBinding
import com.example.utils.Base
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : Base() {

    private lateinit var mBinding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.tvLogin.setOnClickListener{
            onBackPressed()
        }
        mBinding.btnSubmit.setOnClickListener {
            resetPasswordEmail()
        }
    }

    private fun resetPasswordEmail() {
        if(validateUser()){
            showProgressDialog()
            FirebaseAuth.getInstance().sendPasswordResetEmail(mBinding.etEmail.text.toString().trim{it<=' '})
                .addOnCompleteListener{ task ->
                    hideProgressBar()
                    if(task.isSuccessful){
                        Toast.makeText(this, "Password recovery email sent", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    private fun validateUser():Boolean{
        return when{
            TextUtils.isEmpty(mBinding.etEmail.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please enter your email")
                false
            }
            else->{
                true
            }
        }
    }
}