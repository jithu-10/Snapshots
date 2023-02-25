package com.example.instagramv1

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.RoomDatabase
import com.example.instagramv1.data.AppDatabase
import com.example.instagramv1.ui.authscreen.AuthActivity
import com.example.instagramv1.ui.mainscreen.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<InjectDataViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        viewModel.context = this


        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        val loaded = sharedPref.getInt("DATA_LOADED",-1)
        if(loaded==-1){
            deleteDatabase("app_database")
            GlobalScope.launch {
                viewModel.addUsers()
                withContext(Dispatchers.Default){
                    val edit = sharedPref.edit()
                    edit.putInt("DATA_LOADED",1)
                    edit.apply()
                    startApp()
                }
            }

        }
        else{
            startApp()
        }

    }


    private fun startApp(){
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)


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


        if(userId!=-1){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        else{
            val i = Intent(this, AuthActivity::class.java)
            startActivity(i)
            finish()

        }
    }




}