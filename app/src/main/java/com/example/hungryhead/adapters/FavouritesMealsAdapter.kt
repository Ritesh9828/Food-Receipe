package com.example.hungryhead.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hungryhead.databinding.MealItemBinding
import com.example.hungryhead.pojo.Meal

class FavouritesMealsAdapter:RecyclerView.Adapter<FavouritesMealsAdapter.FavouritesMealsAdapterViewHolder>() {

    inner class FavouritesMealsAdapterViewHolder(val binding:MealItemBinding):RecyclerView.ViewHolder(binding.root)

    //diffUtil - if you add or delete any item in favourites then periodically refessing using notify data set change
    //will remove all the items that is why it is used so that only changes will be shown
    private var diffUtil=object :DiffUtil.ItemCallback<Meal>(){
        //compare the primary key between the old item and the new item
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
           return oldItem.idMeal==newItem.idMeal
        }

        // compare the whole object
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
             return oldItem==newItem
        }

    }
    // use this variable to update or delete a list
    val differ= AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesMealsAdapterViewHolder {
        return FavouritesMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )

    }

    override fun onBindViewHolder(holder: FavouritesMealsAdapterViewHolder, position: Int) {
          val meal=differ.currentList[position]
          Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
          holder.binding.tvMealName.text=meal.strMeal
    }

    override fun getItemCount(): Int {
          return differ.currentList.size
    }


}