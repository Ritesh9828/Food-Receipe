package com.example.hungryhead.db

import android.content.Context
import androidx.room.*

import com.example.hungryhead.pojo.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase :RoomDatabase(){
    abstract fun mealDao():MealDao

    companion object{
        @Volatile
        var INSTANCE:MealDatabase?=null

        fun getInstance(context: Context):MealDatabase{
            if(INSTANCE==null){
                INSTANCE=Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                "meal.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as MealDatabase
        }
    }
}