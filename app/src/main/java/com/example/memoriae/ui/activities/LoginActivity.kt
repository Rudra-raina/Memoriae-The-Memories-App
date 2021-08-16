package com.example.memoriae.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.example.firestore.FireStoreClass
import com.example.memoriae.databinding.ActivityLoginBinding
import com.example.model.User
import com.example.utils.Base
import com.example.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : Base() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.tvRegister.setOnClickListener{
            val intent= Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        mBinding.tvForgotPassword.setOnClickListener{
            val intent= Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        mBinding.btnLogin.setOnClickListener {
            loginRegisteredUser()
        }
    }

    private fun loginRegisteredUser() {
        if(validateUser()){
            showProgressDialog()
            val email=mBinding.etEmail.text.toString().trim {it<=' ' }
            val password=mBinding.etPassword.text.toString().trim {it<=' ' }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        FireStoreClass().getUserDetails(this)
                    }else{
                        hideProgressBar()
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    private fun validateUser(): Boolean {
        return when{
            TextUtils.isEmpty(mBinding.etEmail.text.toString().trim {it<=' ' }) ->{
                showErrorSnackBar("Please Enter Your Email ID")
                false
            }
            TextUtils.isEmpty(mBinding.etPassword.text.toString().trim {it<=' ' }) ->{
                showErrorSnackBar("Please Enter Your Password")
                false
            }
            else ->{
                true
            }
        }
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressBar()
        val intent = Intent(this, FragmentActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
        startActivity(intent)
        finish()
    }


}