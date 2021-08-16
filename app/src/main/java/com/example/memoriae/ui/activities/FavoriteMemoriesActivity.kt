package com.example.memoriae.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.FavoriteMemoryAdapter
import com.example.firestore.FireStoreClass
import com.example.memoriae.R
import com.example.memoriae.databinding.ActivityFavoriteMemoriesBinding
import com.example.model.Favorites
import com.example.utils.Base

class FavoriteMemoriesActivity : Base() {

    private lateinit var mBinding : ActivityFavoriteMemoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityFavoriteMemoriesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(mBinding.favoriteMemoryToolbar)
        val actionBar=supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBar.title="Favorite Memories"
        mBinding.favoriteMemoryToolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog()
        FireStoreClass().getFavoriteMemoriesList(this)
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<Favorites>) {
        if(memoryList.size>0){
            hideProgressBar()
            mBinding.rvFav.visibility= View.VISIBLE
            mBinding.tvNoFavoritesFound.visibility=View.GONE

            mBinding.rvFav.layoutManager=LinearLayoutManager(this)
            val adapter=FavoriteMemoryAdapter(this,memoryList)
            mBinding.rvFav.adapter=adapter

        }else{
            hideProgressBar()
            mBinding.rvFav.visibility= View.GONE
            mBinding.tvNoFavoritesFound.visibility=View.VISIBLE
        }
    }

}