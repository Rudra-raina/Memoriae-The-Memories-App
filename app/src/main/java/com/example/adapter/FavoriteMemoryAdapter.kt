package com.example.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriae.databinding.FavoriteRvItemBinding
import com.example.memoriae.databinding.PersonalRvItemBinding
import com.example.memoriae.ui.activities.FavoriteMemoryDetailsActivity
import com.example.memoriae.ui.activities.MemoryDetailsActivity
import com.example.model.Favorites
import com.example.utils.Constants
import com.example.utils.GlideLoader
import kotlinx.android.synthetic.main.favorite_rv_item.view.*
import kotlinx.android.synthetic.main.favorite_rv_item.view.tvName
import kotlinx.android.synthetic.main.personal_rv_item.view.*
import kotlinx.android.synthetic.main.personal_rv_item.view.tvDate
import kotlinx.android.synthetic.main.search_rv_item.view.*
import java.util.*

class FavoriteMemoryAdapter(
    private val context : Context,
    private val list : ArrayList<Favorites>)  : RecyclerView.Adapter<FavoriteMemoryAdapter.MyViewHolder>(){

    class MyViewHolder ( val binding : FavoriteRvItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FavoriteRvItemBinding.inflate(LayoutInflater.from(context),parent,false))
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
            "${model.cityName[0].toString().uppercase(Locale.getDefault())}${model.cityName.substring(1)}"
        Log.e("City",model.cityName)
        holder.binding.tvDescription.text = model.description
        holder.binding.tvRating.text = model.rating.toString()
        holder.itemView.tvDate.text = model.date
        holder.itemView.tvName.text="- ${model.userName}"

        holder.itemView.setOnClickListener{
            val intent = Intent(context,FavoriteMemoryDetailsActivity::class.java)
            intent.putExtra(Constants.favMemoryDetails,model)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}