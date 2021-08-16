package com.example.memoriae.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adapter.GlobalMemoryAdapter
import com.example.firestore.FireStoreClass
import com.example.memoriae.R
import com.example.memoriae.databinding.FragmentGlobalBinding
import com.example.memoriae.ui.activities.FavoriteMemoriesActivity
import com.example.model.Memory
import com.example.utils.BaseFragment

class GlobalFragment : BaseFragment() {

    private var mBinding : FragmentGlobalBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.global_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorites->{
                val intent= Intent(requireContext(),FavoriteMemoriesActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentGlobalBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

    override fun onResume() {
        super.onResume()
        showProgressBar()
        FireStoreClass().getAllMemoriesList(this)
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<Memory>) {
        hideProgressBar()
        if(memoryList.size>0){
            mBinding!!.rvGlobalMemoryList.layoutManager= LinearLayoutManager(requireContext())
            val rvAdapter= GlobalMemoryAdapter(requireContext(),memoryList)
            mBinding!!.rvGlobalMemoryList.adapter=rvAdapter
        }else{
            Toast.makeText(requireContext(), "Error! Please try after some time", Toast.LENGTH_SHORT).show()
        }

    }
}