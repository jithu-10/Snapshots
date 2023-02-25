package com.example.instagramv1.ui.addpostscreen

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.instagramv1.R
import com.example.instagramv1.cropimage.BitmapUtils
import com.example.instagramv1.databinding.GetPhotoDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GetPhotoDialogFragment : DialogFragment() {

    private lateinit var getPhotoDialogFragmentBinding: GetPhotoDialogFragmentBinding
    private val addPostViewModel by activityViewModels<AddPostViewModel>()
    private var imageUri : Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getPhotoDialogFragmentBinding = DataBindingUtil.inflate(inflater,
        R.layout.get_photo_dialog_fragment,container,false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        return getPhotoDialogFragmentBinding.root
    }

    override fun onStart() {
        super.onStart()
//        dialog?.let {
//            it.window?.setWindowWidthPercentage(85)
//        }
        dialog?.window?.setLayout(
            900,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        close = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(addPostViewModel.openCamera==1){
            startCameraIntent2()
        }
        getPhotoDialogFragmentBinding.layoutTakePhoto.setOnClickListener {
            startCameraIntent2()
        }

        getPhotoDialogFragmentBinding.layoutChooseImage.setOnClickListener {
            startGalleryIntent()
        }

        getPhotoDialogFragmentBinding.layoutRemoveCurrentPicture.visibility = View.GONE

    }

//    fun Window.setWindowWidthPercentage(value: Int) {
//
//        // Set the width of the dialog proportional to Required value of the screen width
//        val size = Point()
//        val display: Display = this.windowManager?.defaultDisplay!!
//        display.getSize(size)
//        setLayout((size.x * value/100), WindowManager.LayoutParams.WRAP_CONTENT)
//        setGravity(Gravity.CENTER)
//        // Set the window Transparent for the Dialog
//        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        addFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH)
//    }


    private fun startGalleryIntent() {

//        if (!hasGalleryPermission()) {
//            askForGalleryPermission()
//
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if(!hasImagesPermission()){
//                askForImagesPermission()
//
//            }
//        }
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)


    }

    private  fun startCameraIntent(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    private fun startCameraIntent2(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, PICTURE_RESULT)
//        if(addPostViewModel.openCamera==1){
//            dismiss()
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            val photo = data?.extras!!["data"] as Bitmap?
//            addPostViewModel.postImage = photo
//            addPostViewModel.orginalImage = photo
//            Log.d("Special Log",photo.toString())
//            openCropImageFragment()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            PICTURE_RESULT ->  if (resultCode == RESULT_OK) {
                try {

                    loadNewImage(getRealPathFromURI(imageUri)!!)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else{
                if(addPostViewModel.openCamera==1){
                    dismiss()
                }
            }
        }
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)
    }


    private fun hasGalleryPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasImagesPermission(): Boolean{
        return (ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }

    private fun askForGalleryPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_READ_PERMISSION
        )
    }

    private fun askForImagesPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_CODE_READ_PERMISSION
        )
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val uri = result?.data?.data
            val absPath = BitmapUtils.getFilePathFromUri(requireActivity(),uri!!)
            loadNewImage(absPath!!)
        }
    }



    private fun loadNewImage(filePath: String) {
        val bitmap = BitmapFactory.decodeFile(filePath)
        addPostViewModel.postImage = bitmap
        addPostViewModel.orginalImage = bitmap
        openCropImageFragment()

    }

    companion object {
        private const val REQUEST_CODE_READ_PERMISSION = 22
        private const val REQUEST_GALLERY = 21
        private const val CAMERA_REQUEST = 23
        private const val TAG = "MainActivity"
        private var close = true
        private const val PICTURE_RESULT = 54
    }



    private fun openCropImageFragment(){
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, CropImageFragment())
            close = false
            dismiss()

            commit()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(close){
            val activity: Activity? = activity
            if (activity is DialogInterface.OnDismissListener) {
                (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
            }
        }

    }




}