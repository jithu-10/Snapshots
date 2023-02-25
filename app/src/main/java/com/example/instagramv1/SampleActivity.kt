package com.example.instagramv1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.ui.addpostscreen.CropImageFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen.EditProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        startCropFragment()
    }

    private fun startCropFragment(){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, CropImageFragment())
            commit()
        }
    }
}

