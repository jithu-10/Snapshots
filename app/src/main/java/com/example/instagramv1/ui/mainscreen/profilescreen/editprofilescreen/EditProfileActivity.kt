package com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<EditProfileViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeUser()




    }


    private fun initializeUser(){
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)

        viewModel.userId = userId
//        viewModel.userProfileMutableData.observe(this@EditProfileActivity){
//            viewModel.profilePicture = it.profilePicture
//            viewModel.fullName = it.fullName
//            viewModel.userName = it.userName
//            viewModel.email = it.email
//        }

//        }
//        lifecycleScope.launch{
//
//        }

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.edit_profile_frame, EditProfileFragment())
                commit()
            }

    }
}