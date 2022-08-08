package com.example.hungryhead.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hungryhead.databinding.PopularItemsBinding
import com.example.hungryhead.pojo.MealsByCategory

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    private var mealsList=ArrayList<MealsByCategory>()
    var onLongItemClick:((MealsByCategory)->Unit)?=null
    lateinit var onItemClick:((MealsByCategory)->Unit)

    fun setMeals(mealsByCategoryList: ArrayList<MealsByCategory>){
        this.mealsList=mealsByCategoryList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnClickListener {
            onLongItemClick?.invoke(mealsList[position])

            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size

    }

    class PopularMealViewHolder( var binding:PopularItemsBinding):RecyclerView.ViewHolder(binding.root)

}