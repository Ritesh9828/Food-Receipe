package com.example.hungryhead.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hungryhead.activities.CategoryMealsActivity
import com.example.hungryhead.activities.MainActivity
import com.example.hungryhead.activities.MealActivity
import com.example.hungryhead.adapters.CategoriesAdapter
import com.example.hungryhead.adapters.MostPopularAdapter
import com.example.hungryhead.databinding.FragmentHomeBinding
import com.example.hungryhead.fragments.bottomsheet.MealBottomSheetFragment
import com.example.hungryhead.pojo.MealsByCategory
import com.example.hungryhead.pojo.Meal
import com.example.hungryhead.viewmodel.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding  //instance of our view binding
    private lateinit var viewModel: HomeViewModel  //to get an instance of home view model that we will you use
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter  //creating a popular item adapter
    private lateinit var categoriesAdapter:CategoriesAdapter


    companion object {
        const val MEAl_ID = "com.example.hungryhead.fragments.idMeal"
        const val MEAl_NAME = "com.example.hungryhead.fragments.nameMeal"
        const val MEAl_THUMB = "com.example.hungryhead.fragments.thumbMeal"
        const val CATEGORY_NAME="com.example.hungryhead.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel=(activity as MainActivity).viewModel   // from main activity
        popularItemsAdapter = MostPopularAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()  //to get the meal list into the adapter
        viewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()    //view model created in home view model
        observePopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()



    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={category ->
            val intent=Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter=CategoriesAdapter()
        binding.recViewCategories.apply{
            layoutManager=GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories->
          categoriesAdapter.setCategoryList(categories)

                


        })
    }

    //function for over popular item to go into the Meal Activity to show its details etc.
    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAl_ID, meal.idMeal)
            intent.putExtra(MEAl_NAME, meal.strMeal)
            intent.putExtra(MEAl_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    //recycler view creation of popular items
    private fun preparePopularItemsRecyclerView() {
        //using the popular view item in fragment_home xml
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter

        }


    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner,
            { mealList ->
                //set the adapter in the recycler view adapter
                popularItemsAdapter.setMeals(mealsByCategoryList = mealList as ArrayList<MealsByCategory>)


            })
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAl_ID, randomMeal.idMeal)
            intent.putExtra(MEAl_NAME, randomMeal.strMeal)
            intent.putExtra(
                MEAl_THUMB,
                randomMeal.strMealThumb
            )  //for declaring all the data we fetch from the api to the new activity

            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal


            })


    }

}