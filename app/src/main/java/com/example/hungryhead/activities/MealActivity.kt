package com.example.hungryhead.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.hungryhead.R
import com.example.hungryhead.databinding.ActivityMealBinding
import com.example.hungryhead.db.MealDatabase
import com.example.hungryhead.fragments.HomeFragment
import com.example.hungryhead.pojo.Meal
import com.example.hungryhead.viewmodel.MealViewModel
import com.example.hungryhead.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId:String
    private lateinit var mealName:String
    private lateinit var mealThumb:String
    private lateinit var binding:ActivityMealBinding
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm:MealViewModel //instance of the view model we created in MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase=MealDatabase.getInstance(this)
        val viewModelFactory=MealViewModelFactory(mealDatabase)
        mealMvvm= ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        //mealMvvm=ViewModelProviders.of(this)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailsLiveData()  //observing the live data

        onYoutubeImageCLick()
        onFavouriteClick()

    }

    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener{
           mealToSave?.let {
               mealMvvm.insert(it)
               Toast.makeText(this,"Meal Saved",Toast.LENGTH_LONG).show()
           }
        }
    }

    private fun onYoutubeImageCLick() {
        binding.imgYoutube.setOnClickListener{
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave:Meal?=null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this,object :Observer<Meal>{
            @SuppressLint("SetTextI18n")
            override fun onChanged(t: Meal?) {
                onResponseCase()
                  val meal=t
                mealToSave=meal

                binding.tvCategory.text="Category : ${meal!!.strCategory}"
                binding.tvArea.text="Area : ${meal.strArea}"
                binding.tvInstructionsSteps.text=meal.strInstructions

                youtubeLink= meal.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }


    private fun getMealInformationFromIntent() {
        val intent=intent
        mealId=intent.getStringExtra(HomeFragment.MEAl_ID)!!
        mealName=intent.getStringExtra(HomeFragment.MEAl_NAME)!!
        mealThumb=intent.getStringExtra(HomeFragment.MEAl_THUMB)!!

    }

    //when we need to show only the progress bar
    private fun loadingCase(){
        binding.progressBar.visibility=View.VISIBLE
        binding.btnAddToFav.visibility= View.INVISIBLE
        binding.tvInstructions.visibility= View.INVISIBLE
        binding.tvArea.visibility= View.INVISIBLE
        binding.tvCategory.visibility= View.INVISIBLE
        binding.imgYoutube.visibility= View.INVISIBLE

    }

    //when we need to show details fetched from the api
    private fun onResponseCase(){
        binding.progressBar.visibility=View.INVISIBLE
        binding.btnAddToFav.visibility= View.VISIBLE
        binding.tvInstructions.visibility= View.VISIBLE
        binding.tvArea.visibility= View.VISIBLE
        binding.tvCategory.visibility= View.VISIBLE
        binding.imgYoutube.visibility= View.VISIBLE


    }
}