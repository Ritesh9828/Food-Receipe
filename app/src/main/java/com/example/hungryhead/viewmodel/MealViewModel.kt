package com.example.hungryhead.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungryhead.db.MealDatabase
import com.example.hungryhead.pojo.Meal
import com.example.hungryhead.pojo.MealList
import com.example.hungryhead.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MealViewModel(
    val mealDatabase: MealDatabase
): ViewModel() {
    private var mealDetailsLiveData= MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                 if(response.body()!=null){
                     mealDetailsLiveData.value=response.body()!!.meals[0]
                 }
                else
                    return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("MealActivity",t.message.toString())
            }

        })
    }
    //function so that we can listen this live data from outside
    fun observeMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    //function to update the content of favourite part
    fun insert(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

}