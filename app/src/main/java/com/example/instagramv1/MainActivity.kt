package com.example.instagramv1

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val exploreFragment = ExploreFragment()
        val registerFragment = RegisterFragment()
        val notificationFragment = NotificationFragment()
        val loginFragment = LoginFragment()



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_page,homeFragment)
            commit()
        }


        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_page,homeFragment)
                        addToBackStack(null)
                        commit()
                    }
                }

                R.id.explore_page -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_page,exploreFragment)
                        addToBackStack(null)
                        commit()
                    }
                }

                R.id.add_post_page -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_page,loginFragment)
                        addToBackStack(null)
                        commit()
                    }
                }

                R.id.notification_page -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_page,notificationFragment)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
            true
        }
    }


}