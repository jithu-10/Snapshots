package com.example.instagramv1.ui.mainscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.*
import com.example.instagramv1.ui.addpostscreen.AddPostActivity
import com.example.instagramv1.ui.mainscreen.explorescreen.ExploreFragment
import com.example.instagramv1.ui.mainscreen.explorescreen.ExplorePostsViewModel
import com.example.instagramv1.ui.mainscreen.homescreen.HomeFragment
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationFragment
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel
import com.example.instagramv1.ui.searchscreen.SearchViewModel
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<UserProfileViewModel>()
    private val postViewModel by viewModels<PostViewModel>()
    private val notificationViewModel by viewModels<NotificationViewModel>()
    private val explorePostsViewModel by viewModels<ExplorePostsViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()

    private val homeFragment = HomeFragment()
    private val exploreFragment = ExploreFragment()
    private val notificationFragment = NotificationFragment()
    private var start = true


    private var previousItem : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        viewModel.userId = userId

        val theme = sharedPref.getInt("THEME", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        if(theme == AppCompatDelegate.MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else if(theme == AppCompatDelegate.MODE_NIGHT_NO){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else if(theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        fetchData()
        setUpListeners()
        if(savedInstanceState == null){
            initializeUser()
        }



    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_page, fragment)
            .commit()
    }

    private fun replaceFragment2(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_page, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setUpListeners2(){
        val bottomNavigationView = findViewById<NavigationBarView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener {
            Log.d("BackStacker",previousItem.toString()+" "+it.itemId)
            when(it.itemId){
                R.id.home_page -> {
                    if(previousItem != R.id.home_page){
                        previousItem = R.id.home_page
                        replaceFragment2(homeFragment)
                    }
                }

                R.id.explore_page -> {
                    if (previousItem != R.id.explore_page) {
                        previousItem = R.id.explore_page
                        replaceFragment2(exploreFragment)
                    }
                }

                R.id.add_post_page -> {
                    Intent(this, AddPostActivity::class.java).apply {
                        startActivity(this)
                    }
                }

                R.id.notification_page -> {
                    if(previousItem!=R.id.notification_page){
                        previousItem = R.id.notification_page
                        replaceFragment2(notificationFragment)
                    }
                }
            }

            true
        }
    }

    private  fun setUpListeners(){
        val bottomNavigationView = findViewById<NavigationBarView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener {
            Log.d("BackStacker",previousItem.toString()+" "+it.itemId)
            when(it.itemId){
                R.id.home_page -> {
                    if(previousItem!=R.id.home_page){
                        previousItem = R.id.home_page
                        supportFragmentManager.beginTransaction().apply {

                            if(start){
                                start = false
                                replace(R.id.frame_page,homeFragment,"HOME_FRAGMENT")
                            }
                            else{
                                addToBackStack("HOME_FRAGMENT")
                                replace(R.id.frame_page,homeFragment,"HOME_FRAGMENT")

                            }

//                        if(!isFragmentInBackstack(supportFragmentManager,"HOME_FRAGMENT")){
//                            addToBackStack("HOME_FRAGMENT")
//                        }
                            commit()

                        }
                    }
                    else{
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.frame_page)
                        Log.d("Frag Trans",fragment.toString())
                        while(true){
                            if(fragment!=null&& fragment is HomeFragment){
                                break
                            }
                            else{
                                supportFragmentManager.popBackStackImmediate()
                                fragment = supportFragmentManager.findFragmentById(R.id.frame_page)
                                Log.d("Frag Trans",fragment.toString())
                            }
                        }

                    }


                }

                R.id.explore_page -> {

                    if(previousItem != R.id.explore_page){
                        previousItem = R.id.explore_page
                        supportFragmentManager.beginTransaction().apply {
                            addToBackStack("EXPLORE_FRAGMENT")
                            replace(R.id.frame_page,exploreFragment,"EXPLORE_FRAGMENT")

//                        if(!isFragmentInBackstack(supportFragmentManager,"EXPLORE_FRAGMENT")){
//                            addToBackStack("EXPLORE_FRAGMENT")
//                        }

                            commit()
                        }
                    }
                    else{
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.frame_page)
                        Log.d("Frag Trans",fragment.toString())
                        while(true){
                            if(fragment!=null&& fragment is ExploreFragment){
                                break
                            }
                            else{
                                supportFragmentManager.popBackStackImmediate()
                                fragment = supportFragmentManager.findFragmentById(R.id.frame_page)
                                Log.d("Frag Trans",fragment.toString())
                            }
                        }

                    }


                }

                R.id.add_post_page -> {
                    Intent(this, AddPostActivity::class.java).apply {
                        startActivity(this)
                    }
                }

                R.id.notification_page -> {

                    if(previousItem!=R.id.notification_page){
                        supportFragmentManager.beginTransaction().apply {
                            previousItem = R.id.notification_page
                            addToBackStack("NOTIFICATION_FRAGMENT")
                            replace(R.id.frame_page,notificationFragment,"NOTIFICATION_FRAGMENT")

//                        if(!isFragmentInBackstack(supportFragmentManager,"NOTIFICATION_FRAGMENT")){
//                            addToBackStack("NOTIFICATION_FRAGMENT")
//                        }
                            commit()
                        }
                        Log.d("BackStacker","Previous Item Set At Notification : $previousItem")
                    }
                    else{
                        var fragment: Fragment? =
                            supportFragmentManager.findFragmentById(R.id.frame_page)
                        Log.d("Frag Trans",fragment.toString())
                        while(true){
                            if(fragment!=null&& fragment is NotificationFragment){
                                break
                            }
                            else{
                                supportFragmentManager.popBackStackImmediate()
                                fragment = supportFragmentManager.findFragmentById(R.id.frame_page)
                                Log.d("Frag Trans",fragment.toString())
                            }
                        }

                    }
                }
            }

            true
        }

    }

    fun isFragmentInBackstack(fragmentManager: FragmentManager, fragmentTagName: String): Boolean {
        for (entry in 0 until fragmentManager.backStackEntryCount) {
            if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).name) {
                return true
            }
        }
        return false
    }




    private fun attachViews(){



        //val bottomNavigationView = findViewById<NavigationBarView>(R.id.bottom_navigation)
//        if(resources.configuration.orientation  == Configuration.ORIENTATION_LANDSCAPE){
//            MenuItemCompat.setIconTintMode(bottomNavigationView.menu.getItem(R.id.profile_picture),null)
//        }

//        supportFragmentManager.beginTransaction().apply {
//            add(R.id.frame_page,homeFragment,"HOME_FRAGMENT")
//            commit()
//        }




    }

    private fun initializeUser(){
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE",Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)

        val bottomNavigationView = findViewById<NavigationBarView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.home_page


        attachViews()



    }

    private fun fetchData(){
        lifecycleScope.launch{
            viewModel.getUserProfileData().observe(this@MainActivity){
                viewModel.profilePicture = it.profile_picture
                viewModel.privateAccount = it.private_account

            }

        }
    }



//    override fun onBackPressed() {
//        val nav_view = findViewById<NavigationBarView>(R.id.bottom_navigation)
//        Log.d("Back Stack Entries : ",supportFragmentManager.backStackEntryCount.toString())
//        onBackPressedDispatcher.hasEnabledCallbacks()
//        onBackPressedDispatcher.onBackPressed()
////        if (nav_view.selectedItemId == R.id.home_page) {
////            onBackPressedDispatcher.onBackPressed()
////        } else {
////            nav_view.selectedItemId = R.id.home_page
////            //replaceFragment(homeFragment)
////        }
//    }




}