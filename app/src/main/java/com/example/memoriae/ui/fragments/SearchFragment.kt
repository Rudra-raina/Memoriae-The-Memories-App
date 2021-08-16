package com.example.memoriae.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.adapter.SearchMemoryAdapter
import com.example.firestore.FireStoreClass
import com.example.memoriae.R
import com.example.memoriae.databinding.FragmentSearchBinding
import com.example.model.Memory
import com.example.utils.BaseFragment
import com.yarolegovich.discretescrollview.DiscreteScrollView
import java.util.*

class SearchFragment : BaseFragment() {

    private var mBinding : FragmentSearchBinding? = null
    private var cityName : String = " "

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentSearchBinding.inflate(inflater,container, false)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding!!.rg.setOnCheckedChangeListener{_,checkID->
            if(checkID== R.id.rbLocal){
                mBinding!!.llSearchLocal.visibility=View.VISIBLE
                mBinding!!.llSearchGlobal.visibility=View.GONE
            }else{
                mBinding!!.llSearchLocal.visibility=View.GONE
                mBinding!!.llSearchGlobal.visibility=View.VISIBLE
            }
        }

        mBinding!!.ibSearchGlobal.setOnClickListener{
            if(validateEntry()){
                showList()
            }
        }
        mBinding!!.ibSearchLocal.setOnClickListener{
            if(validateEntryLocal()){
                showLocalList()
            }
        }
    }

    private fun validateEntryLocal(): Boolean {
        return when {
            TextUtils.isEmpty(mBinding!!.etSearchLocal.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter City Name")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun showLocalList(){
        showProgressBar()
        cityName= mBinding!!.etSearchLocal.text.toString().trim{it<=' '}.lowercase(Locale.getDefault())
        FireStoreClass().getLocalSearchMemory(this,cityName)
    }
    private fun showList() {
        showProgressBar()
        cityName= mBinding!!.etSearchGlobal.text.toString().trim{it<=' '}.lowercase(Locale.getDefault())
        FireStoreClass().getCityMemory(this,cityName)
    }

    fun listReceivedSuccessfully(memoryCityList: ArrayList<Memory>) {
        hideProgressBar()
        if(memoryCityList.size>0){
            mBinding!!.tvNoMemoryFound.visibility=View.GONE
            mBinding!!.rvCityMemoryList.visibility=View.VISIBLE


            val scrollView: DiscreteScrollView = mBinding!!.rvCityMemoryList
            scrollView.adapter = SearchMemoryAdapter(requireContext(),memoryCityList)

        }else{
            mBinding!!.tvNoMemoryFound.visibility=View.VISIBLE
            mBinding!!.rvCityMemoryList.visibility=View.GONE

        }

    }

    private fun validateEntry(): Boolean {
        return when {
            TextUtils.isEmpty(mBinding!!.etSearchGlobal.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar("Please Enter City Name")
                false
            }
            else -> {
                true
            }
        }
    }

}