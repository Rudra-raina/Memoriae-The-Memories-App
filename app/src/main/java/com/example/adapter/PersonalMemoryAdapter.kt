package com.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestore.FireStoreClass
import com.example.memoriae.databinding.PersonalRvItemBinding
import com.example.memoriae.ui.activities.AddEditMemory
import com.example.memoriae.ui.activities.MemoryDetailsActivity
import com.example.memoriae.ui.fragments.PersonalFragment
import com.example.model.Memory
import com.example.utils.Constants
import com.example.utils.GlideLoader
import kotlinx.android.synthetic.main.personal_rv_item.view.*

class PersonalMemoryAdapter(private val context : Context, private var list : ArrayList<Memory>) :
                                                                        RecyclerView.Adapter<PersonalMemoryAdapter.MyViewHolder>(){

    class MyViewHolder ( val binding :PersonalRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(PersonalRvItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        if (position % 4 == 0) {
            holder.binding.tvColor.setBackgroundColor(Color.parseColor("#FECE2F"))
            holder.binding.tvColor.setTextColor(Color.parseColor("#FECE2F"))
        } else if (position % 4 == 1) {
            holder.binding.tvColor.setBackgroundColor(Color.parseColor("#F999cd"))
            holder.binding.tvColor.setTextColor(Color.parseColor("#F999cd"))

        } else if (position % 4 == 2) {
            holder.binding.tvColor.setBackgroundColor(Color.parseColor("#9ACD32"))
            holder.binding.tvColor.setTextColor(Color.parseColor("#9ACD32"))
        } else {
            holder.binding.tvColor.setBackgroundColor(Color.parseColor("#F5DEB3"))
            holder.binding.tvColor.setTextColor(Color.parseColor("#F5DEB3"))
        }

        GlideLoader(context).loadPicture(model.imageURL, holder.binding.ivCircleImage)
        holder.binding.tvTitle.text = model.title
        holder.binding.tvCityName.text =
            "${model.cityName[0].toString().toUpperCase()}${model.cityName.substring(1)}"
        holder.binding.tvDescription.text = model.description
        holder.binding.tvRating.text = model.rating.toString()
        holder.itemView.tvDate.text = model.date

        holder.itemView.setOnClickListener{
            val intent=Intent(context,MemoryDetailsActivity::class.java)
            intent.putExtra(Constants.MEMORY_DETAILS,model)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyDeleteItem(fragment: PersonalFragment, position: Int) {
        fragment.showProgressBar()
        FireStoreClass().deleteMemory(fragment,list[position].memoryID)
        notifyItemRemoved(position)

    }

    fun notifyEditItem(fragment: PersonalFragment, position: Int){
        val intent = Intent(context,AddEditMemory::class.java)
        intent.putExtra(Constants.PASS_MEMORY,list[position])
        fragment.startActivity(intent)
        notifyItemChanged(position)
    }
}