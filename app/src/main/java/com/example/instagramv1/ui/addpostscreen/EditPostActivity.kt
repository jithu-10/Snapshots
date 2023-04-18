package com.example.instagramv1.ui.addpostscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.ActivityEditPostBinding
import com.example.instagramv1.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditPostActivity : AppCompatActivity() {

    private val viewModel by viewModels<EditPostViewModel>()
    private lateinit var editPostBinding: ActivityEditPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editPostBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_post)
        initializeUser()

        val postId = intent.getIntExtra("POST_ID", -1)
        viewModel.postId = postId

        lifecycleScope.launch {
            viewModel.getPost().observe(this@EditPostActivity){
                viewModel.postImage = it.post_image
                viewModel.description = it.post_description
                viewModel.location = it.post_location


                loadImage(editPostBinding.postSampleView,it.post_image)
                Log.d("Setting Edit","location : ${it.post_location} ${it.post_description}")
//                editPostBinding.tiDescription.setText(it.post_description)
//                editPostBinding.tiLocation.setText(it.post_location)

                it.post_description?.let {
                    editPostBinding.tiDescription.post {
                        editPostBinding.tiDescription.setText(it)
                    }
                }

                it.post_location?.let {
                    editPostBinding.tiLocation.post {
                        editPostBinding.tiLocation.setText(it)
                    }
                }

                //editPostBinding.executePendingBindings()

            }
        }

        editPostBinding.btnPost.setOnClickListener {
            lifecycleScope.launch{
                viewModel.location = editPostBinding.tiLocation.text.toString()
                viewModel.description = editPostBinding.tiDescription.text.toString()

                viewModel.updatePostDetails()
            }
            finish()

        }

        editPostBinding.imgViewBackBtn.setOnClickListener {
            finish()
        }
    }

    private fun initializeUser(){
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)

        viewModel.userId = userId
    }


}