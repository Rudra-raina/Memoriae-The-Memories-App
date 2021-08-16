package com.example.memoriae.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adapter.PersonalMemoryAdapter
import com.example.firestore.FireStoreClass
import com.example.memoriae.R
import com.example.memoriae.databinding.FragmentPersonalBinding
import com.example.memoriae.ui.activities.AddEditMemory
import com.example.memoriae.ui.activities.LoginActivity
import com.example.model.Memory
import com.example.shopperista.utils.SwipeToDeleteCallback
import com.example.shopperista.utils.SwipeToEditCallback
import com.example.utils.BaseFragment
import com.google.firebase.auth.FirebaseAuth

class PersonalFragment : BaseFragment() {

    private var mBinding : FragmentPersonalBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding= FragmentPersonalBinding.inflate(inflater,container,false)
        return mBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.personal_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add ->{
                val intent= Intent(requireContext(),AddEditMemory::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout->{
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
                Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getListFromFireStore()
    }

    private fun getListFromFireStore(){
        showProgressBar()
        FireStoreClass().getMemoriesList(this)
    }

    fun listReceivedSuccessfully(memoryList: ArrayList<Memory>) {
        hideProgressBar()
        if(memoryList.size>0){
            mBinding!!.rvPersonal.visibility=View.VISIBLE
            mBinding!!.tvPersonal.visibility=View.GONE

            mBinding!!.rvPersonal.layoutManager=LinearLayoutManager(requireContext())
            val rvAdapter=PersonalMemoryAdapter(requireContext(),memoryList)
            mBinding!!.rvPersonal.adapter=rvAdapter



            val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = mBinding!!.rvPersonal.adapter as PersonalMemoryAdapter
                    adapter.notifyDeleteItem(this@PersonalFragment,viewHolder.adapterPosition)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(mBinding!!.rvPersonal)

            val editSwipeHandler = object : SwipeToEditCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = mBinding!!.rvPersonal.adapter as PersonalMemoryAdapter
                    adapter.notifyEditItem(this@PersonalFragment, viewHolder.adapterPosition)
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(mBinding!!.rvPersonal)

        }else{
            mBinding!!.rvPersonal.visibility=View.GONE
            mBinding!!.tvPersonal.visibility=View.VISIBLE
        }

    }

    fun deleteSuccessful() {
        hideProgressBar()
        Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show()
        getListFromFireStore()
    }


}