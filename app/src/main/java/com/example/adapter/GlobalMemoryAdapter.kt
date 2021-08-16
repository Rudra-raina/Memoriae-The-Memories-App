package com.example.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriae.databinding.GlobalRvItemOneBinding
import com.example.memoriae.ui.activities.MemoryDetailsActivity
import com.example.model.Memory
import com.example.utils.Constants
import com.example.utils.GlideLoader
import kotlinx.android.synthetic.main.search_rv_item.view.*

class GlobalMemoryAdapter(private val context : Context, private var list : ArrayList<Memory>) :
                                                        RecyclerView.Adapter<GlobalMemoryAdapter.MyViewHolder>(){

    class MyViewHolder ( val binding : GlobalRvItemOneBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(GlobalRvItemOneBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        when(position%4){
            0 -> {
                holder.binding.tvColor.setBackgroundColor(Color.parseColor("#FECE2F"))
                holder.binding.tvColor.setTextColor(Color.parseColor("#FECE2F"))
            }
            1->{
                holder.binding.tvColor.setBackgroundColor(Color.parseColor("#F999cd"))
                holder.binding.tvColor.setTextColor(Color.parseColor("#F999cd"))
            }
            2->{
                holder.binding.tvColor.setBackgroundColor(Color.parseColor("#9ACD32"))
                holder.binding.tvColor.setTextColor(Color.parseColor("#9ACD32"))
            }
            else->{
                holder.binding.tvColor.setBackgroundColor(Color.parseColor("#F5DEB3"))
                holder.binding.tvColor.setTextColor(Color.parseColor("#F5DEB3"))
            }
        }

        when(model.rating){
            1->{
                holder.itemView.starOne.visibility= View.VISIBLE
                holder.itemView.starTwo.visibility= View.GONE
                holder.itemView.starThree.visibility= View.GONE
                holder.itemView.starFour.visibility= View.GONE
                holder.itemView.starFive.visibility= View.GONE
            }
            2->{
                holder.itemView.starOne.visibility= View.VISIBLE
                holder.itemView.starTwo.visibility= View.VISIBLE
                holder.itemView.starThree.visibility= View.GONE
                holder.itemView.starFour.visibility= View.GONE
                holder.itemView.starFive.visibility= View.GONE
            }
            3->{
                holder.itemView.starOne.visibility= View.VISIBLE
                holder.itemView.starTwo.visibility= View.VISIBLE
                holder.itemView.starThree.visibility= View.VISIBLE
                holder.itemView.starFour.visibility= View.GONE
                holder.itemView.starFive.visibility= View.GONE
            }
            4->{
                holder.itemView.starOne.visibility= View.VISIBLE
                holder.itemView.starTwo.visibility= View.VISIBLE
                holder.itemView.starThree.visibility= View.VISIBLE
                holder.itemView.starFour.visibility= View.VISIBLE
                holder.itemView.starFive.visibility= View.GONE
            }
            else->{
                holder.itemView.starOne.visibility= View.VISIBLE
                holder.itemView.starTwo.visibility= View.VISIBLE
                holder.itemView.starThree.visibility= View.VISIBLE
                holder.itemView.starFour.visibility= View.VISIBLE
                holder.itemView.starFive.visibility= View.VISIBLE
            }
        }

        GlideLoader(context).loadPicture(model.imageURL, holder.binding.ivCircleImage)
        holder.binding.tvTitle.text = model.title
        holder.binding.tvCityName.text ="${model.cityName[0].toString().toUpperCase()}${model.cityName.substring(1)}"
        holder.binding.tvDescription.text = model.description
        holder.itemView.tvName.text="${model.date}, ${model.userName}"

        holder.itemView.setOnClickListener{
            val intent= Intent(context, MemoryDetailsActivity::class.java)
            intent.putExtra(Constants.MEMORY_DETAILS,model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}