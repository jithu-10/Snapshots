package com.example.instagramv1.ui.addpostscreen

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.instagramv1.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddPostActivity : AppCompatActivity() , DialogInterface.OnCancelListener , DialogInterface.OnDismissListener{

    private val viewModel by viewModels<AddPostViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val openCamera = intent.getIntExtra("OPEN_CAMERA",-1)
        viewModel.openCamera = openCamera

        initializeUser()
        startGetPhotoDialogFragment()

    }

    private fun startCropFragment(){

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, CropImageFragment())
            commit()
        }
    }

    private fun initializeUser(){
        val sharedPref = getSharedPreferences("COMMON_PREFERENCE",Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)

        viewModel.userId = userId
    }



    private fun startGetPhotoDialogFragment(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val getPhotoDialogFragment = GetPhotoDialogFragment()
        getPhotoDialogFragment.show(supportFragmentManager,"dialog")
        //getPhotoDialogFragment.dialog?.window?.setLayout((6 * width)/7, (4 * height)/5);
        //getPhotoDialogFragment.dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog)
    }

    override fun onCancel(dialog: DialogInterface?) {

    }

    override fun onDismiss(dialog: DialogInterface?) {


        if(supportFragmentManager.fragments.size == 1){
            finish()
        }
    }
}