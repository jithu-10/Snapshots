package com.example.instagramv1.ui.authscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.instagramv1.R
import com.example.instagramv1.ui.authscreen.loginscreen.LoginFragment
import com.example.instagramv1.ui.authscreen.loginscreen.LoginViewModel
import com.example.instagramv1.ui.authscreen.registerscreen.RegisterFragment
import com.example.instagramv1.ui.authscreen.registerscreen.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().apply {
                add(R.id.frame_layout, LoginFragment())
                commit()
            }
        }

    }
}