package com.example.hungryhead.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: MealApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")  // getting the url
            .addConverterFactory(GsonConverterFactory.create())    //converting it to the kotlin file so that it can  be uses
            .build()
            .create(MealApi::class.java)   //lastly give interface

    }
}