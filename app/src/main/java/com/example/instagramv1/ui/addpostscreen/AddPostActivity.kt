package com.example.instagramv1.ui.addpostscreen

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.instagramv1.R
import com.example.instagramv1.utils.loadImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddPostActivity : AppCompatActivity() , DialogInterface.OnCancelListener , DialogInterface.OnDismissListener{

    private val viewModel by viewModels<AddPostViewModel>()
    private var imageUri : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val openCamera = intent.getIntExtra("OPEN_CAMERA",-1)
        viewModel.openCamera = openCamera

        initializeUser()
        if(viewModel.openCamera == 1){
            startCameraIntent()
        }
        else{
            startCropFragment()
        }



    }

    private val registerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val path = getRealPathFromURI(imageUri)!!
            viewModel.galleryImagePath = path
            loadNewImage(path)
            //}
        } else {
            deleteImage(imageUri)
            imageUri = null
            if(viewModel.openCamera == 1){
                viewModel.openCamera = -1
                finish()
            }
        }
    }

    private fun deleteImage(uri: Uri?) {
        if (uri != null) {
            val contentResolver = this.contentResolver
            contentResolver.delete(uri, null, null)
        }
    }

    private fun startCameraIntent(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        registerResult.launch(intent)
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




    private fun loadNewImage(filePath: String) {
        val bitmap = BitmapFactory.decodeFile(filePath)
        viewModel.postImage = bitmap
        viewModel.orginalImage = bitmap
        startCropFragment()

    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = this.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)
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

    companion object{
        private const val PICTURE_RESULT = 55
    }
}