package com.example.memoriae.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.firestore.FireStoreClass
import com.example.memoriae.databinding.ActivityRegisterBinding
import com.example.model.User
import com.example.utils.Base
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : Base() {

    private lateinit var mBinding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.tvLogin.setOnClickListener{
            onBackPressed()
        }
        mBinding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        if(validateUser()){
            showProgressDialog()

            val email = mBinding.etEmail.text.toString().trim{it<=' '}
            val password = mBinding.etPassword.text.toString().trim{it<=' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val firebaseUser : FirebaseUser =  task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            mBinding.etFirstName.text.toString().trim{it<=' '},
                            mBinding.etLastName.text.toString().trim{it<=' '},
                            mBinding.etEmail.text.toString().trim{it<=' '}
                        )
                        FireStoreClass().registerUser(this,user)
                    }else{
                        hideProgressBar()
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    fun userRegistrationSuccess() {
        hideProgressBar()
        Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun validateUser(): Boolean {
        return when{
            TextUtils.isEmpty(mBinding.etFirstName.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your First Name")
                false
            }
            TextUtils.isEmpty(mBinding.etLastName.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your Last Name")
                false
            }
            TextUtils.isEmpty(mBinding.etEmail.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your Email")
                false
            }
            TextUtils.isEmpty(mBinding.etPassword.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter Your Password")
                false
            }
            TextUtils.isEmpty(mBinding.etConfirmPassword.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Confirm Your Password")
                false
            }
            mBinding.etPassword.text.toString().trim{it<=' '}!=mBinding.etConfirmPassword.text.toString().trim{it<=' '} -> {
                showErrorSnackBar("Passwords Do Not Match")
                false
            }
            else -> {
                true
            }
        }

    }
}