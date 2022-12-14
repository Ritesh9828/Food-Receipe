package com.example.hungryhead.retrofit

import com.example.hungryhead.pojo.CategoryList
import com.example.hungryhead.pojo.MealsByCategoryList
import com.example.hungryhead.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal():Call<MealList>

    @GET("lookup.php")
    fun getMealDetail(@Query("i")id:String):Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c")categoryName:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategories(@Query("c")categoryName: String):Call<MealsByCategoryList>


}