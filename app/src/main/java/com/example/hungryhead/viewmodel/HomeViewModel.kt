package com.example.hungryhead.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungryhead.db.MealDatabase
import com.example.hungryhead.pojo.*
import com.example.hungryhead.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private var mealDatabase: MealDatabase
):ViewModel() {   //view model contains only the logic code
    private var randomMealLiveData= MutableLiveData<Meal>()
    private var popularItemsLiveData=MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData=MutableLiveData<List<Category>>()
    private var favouriteMealsLiveData=mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData=MutableLiveData<Meal>()

    private var saveStateRandomMeal:Meal ?=null // for saving the state while rotating the mobile phone
    fun getRandomMeal(){
          saveStateRandomMeal?.let { randomMeal->
              randomMealLiveData.postValue(randomMeal)
              return
          }

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(
                call: Call<MealList>,
                response: Response<MealList>
            ) {  //we got the response from the library
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value=randomMeal
                    saveStateRandomMeal=randomMeal

                } else {
                    return
                }
            }

            override fun onFailure(
                call: Call<MealList>,
                t: Throwable
            ) {    //api can't fetch the image so failed
                Log.d("Homefragment", t.message.toString())
            }


        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body()!=null){
                    popularItemsLiveData.value=response.body()!!.meals

                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Homefragment", t.message.toString())
            }

        })
    }

    fun getCategories(){   //funcion to get categories using the retrofit call
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                  response.body()?.let{categoryList ->                    //in kotlin we do by this way also rather than what we did in other retrofit calls
                  categoriesLiveData.postValue(categoryList.categories)
                  }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal=response.body()?.meals?.first()
                meal?.let { meal->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                   Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun insert(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().update(meal)
        }
    }

    //function to delete the content of favourite part
    fun delete(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }


    //function to observe the live data in categories
    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData

    }

    //function to observe the favourite part of the live data
    fun  observeFavouritesMealsLiveDate():LiveData<List<Meal>>{
        return favouriteMealsLiveData
    }

    fun observeBottomSheetMeal(): LiveData<Meal> =bottomSheetMealLiveData

}