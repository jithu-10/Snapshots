package com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.GalleryViewRecyclerAdapter
import com.example.instagramv1.cropimage.BitmapUtils
import com.example.instagramv1.cropimage.CropState
import com.example.instagramv1.cropimage.CropperView
import com.example.instagramv1.databinding.FragmentProfilePictureCropBinding
import com.example.instagramv1.ui.addpostscreen.CropImageFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class ProfilePictureCropFragment : Fragment(),GalleryViewRecyclerAdapter.OnEventListener {

    private val viewModel by activityViewModels <EditProfileViewModel>()
    private lateinit var profilePictureCropBinding: FragmentProfilePictureCropBinding
    private val fragmentView: View? = null
    var mImageView: CropperView? = null
    private var mBitmap: Bitmap? = null
    private var imageUri : Uri? = null
    private lateinit var galleryViewRecyclerAdapter : GalleryViewRecyclerAdapter
    private lateinit var galleryRecyclerView : RecyclerView


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        profilePictureCropBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_profile_picture_crop, container, false)
        profilePictureCropBinding.editProfileViewModel = viewModel
        return profilePictureCropBinding.root
    }

    override fun onResume() {
        super.onResume()
        if(getImagesCursor()!!.count > 0){
            view?.findViewById<View>(R.id.no_images_found)?.visibility = View.GONE
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.profilePicture = viewModel.originalPicture
                    viewModel.galleryImagePath = null
                    parentFragmentManager.popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getImagesCursor(): Cursor? {

        val cursor: Cursor?
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )
        cursor = requireActivity().contentResolver
            .query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.ImageColumns._ID + " DESC"
            )
        return cursor
    }


    private fun startCameraIntent(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        registerResult.launch(intent)
    }

    private val registerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            val path = getRealPathFromURI(imageUri)!!
            viewModel.galleryImagePath = path
            galleryViewRecyclerAdapter.cursor = getImagesCursor()!!
            galleryViewRecyclerAdapter.notifyDataSetChanged()
            loadNewImage(path)

        } else {
            deleteImage(imageUri)
            imageUri = null
        }
    }

    private fun deleteImage(uri: Uri?) {
        if (uri != null) {
            val contentResolver = requireActivity().contentResolver
            contentResolver.delete(uri, null, null)
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBitmap = viewModel.originalPicture



        galleryRecyclerView = view.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        galleryRecyclerView.layoutManager = GridLayoutManager(requireActivity(),4)
        galleryViewRecyclerAdapter = GalleryViewRecyclerAdapter(viewModel,this)
        galleryViewRecyclerAdapter.cursor = getImagesCursor()!!
        galleryRecyclerView.adapter = galleryViewRecyclerAdapter

        val verticalDecorator = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        val horizontalDecorator = DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL)

        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.new_divider,resources.newTheme())
        if(drawable!=null){
            verticalDecorator.setDrawable(drawable)
            horizontalDecorator.setDrawable(drawable)
        }


        galleryRecyclerView.addItemDecoration(verticalDecorator);
        galleryRecyclerView.addItemDecoration(horizontalDecorator);


        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            viewModel.profilePicture = viewModel.originalPicture
            viewModel.galleryImagePath = null
            parentFragmentManager.popBackStack()
        }


        mImageView = view.findViewById(R.id.imageview)
        //loadImage(mBitmap!!)

        val cursor = getImagesCursor()!!
        cursor.moveToFirst()

        if(viewModel.galleryImagePath == null){
            if(cursor.count>0){
                view.findViewById<View>(R.id.no_images_found).visibility = View.GONE
                viewModel.galleryImagePath = cursor.getString(1)
                loadNewImage(viewModel.galleryImagePath!!)
            }
            else{
                view.findViewById<View>(R.id.no_images_found).visibility = View.VISIBLE
            }


        }
        else{
            val file = File(viewModel.galleryImagePath!!)
            if(file.exists()){
                loadNewImage(viewModel.galleryImagePath!!)
            }
            else{
                if(cursor.count>0){
                    view.findViewById<View>(R.id.no_images_found).visibility = View.GONE
                    viewModel.galleryImagePath = cursor.getString(1)
                    loadNewImage(viewModel.galleryImagePath!!)
                }
                else{
                    view.findViewById<View>(R.id.no_images_found).visibility = View.VISIBLE
                }
            }

        }
        //loadNewImage(viewModel.galleryImagePath!!)

        profilePictureCropBinding.openCamera.setOnClickListener {
            startCameraIntent()
        }

        view.findViewById<View>(R.id.btnAddPhoto).setOnClickListener {
            startGetPhotoDialogFragment()
        }
        view.findViewById<View>(R.id.btnAddPhotoTv).setOnClickListener{
            startGetPhotoDialogFragment()
        }
        view.findViewById<View>(R.id.btnContinue).setOnClickListener {
            cropImage()
            parentFragmentManager.popBackStack()
        }
        view.findViewById<View>(R.id.rotate_button).setOnClickListener { rotateImage() }
        view.findViewById<View>(R.id.snap_button).setOnClickListener { snapImage() }
        mImageView!!.setDebug(true)
        mImageView!!.setGridCallback(object : CropperView.GridCallback {
            override fun onGestureStarted(): Boolean {
                return true
            }

            override fun onGestureCompleted(): Boolean {
                return false
            }
        })
    }





    private fun startGetPhotoDialogFragment(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val getPhotoDialogFragment = GetProfilePhotoDialogFragment()
        getPhotoDialogFragment.show(parentFragmentManager,"dialog")
        getPhotoDialogFragment.dialog?.window?.setLayout((6 * width)/7, (4 * height)/5);
    }

    private fun loadImage(bitmap : Bitmap){
        val display: Display = requireActivity().windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        val maxP = Math.max(bitmap.getWidth(), bitmap.getHeight())
        val scale1280 = maxP.toFloat() / 1280


        mImageView!!.maxZoom = width * 2 / 1280f

        mBitmap = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.getWidth() / scale1280).toInt(),
            (bitmap.getHeight() / scale1280).toInt(),
            true
        )
        mImageView!!.setImageBitmap(mBitmap)
    }

    private fun loadNewImage(filePath: String) {
        Log.i(TAG, "load image: $filePath")
        mBitmap = BitmapFactory.decodeFile(filePath)
        Log.d("Images New ",mBitmap.toString())
        val display: Display = requireActivity().windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        Log.i(TAG, "bitmap: " + mBitmap!!.getWidth() + " " + mBitmap!!.getHeight())
        val maxP = Math.max(mBitmap!!.getWidth(), mBitmap!!.getHeight())
        val scale1280 = maxP.toFloat() / 1280
        Log.i(TAG, "scaled: " + scale1280 + " - " + 1 / scale1280)
        mImageView!!.maxZoom = width * 2 / 1280f
        mBitmap = Bitmap.createScaledBitmap(
            mBitmap!!,
            (mBitmap!!.getWidth() / scale1280).toInt(),
            (mBitmap!!.getHeight() / scale1280).toInt(),
            true
        )
        mImageView!!.setImageBitmap(mBitmap)

    }


    private fun startGalleryIntent() {
        if (!hasGalleryPermission()) {
            askForGalleryPermission()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(!hasImagesPermission()){
                askForImagesPermission()

            }
        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun hasImagesPermission(): Boolean{
        return (ActivityCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasGalleryPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }

    private fun askForGalleryPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
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



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent()
                return
            }
        }
        Toast.makeText(requireActivity(), "Gallery permission not granted", Toast.LENGTH_SHORT).show()
    }



    private fun cropImage() {
        val result = mImageView!!.croppedBitmap
        if (result.state == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(requireActivity(), "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show()
            return
        }
        val bitmap = result.bitmap
        if (bitmap != null) {
            Log.d("Cropper", "crop1 bitmap: " + bitmap.width + ", " + bitmap.height)
            viewModel.profilePicture = bitmap

        }

    }



    private fun rotateImage() {
        if (mBitmap == null) {
            Log.e(TAG, "bitmap is not loaded yet")
            return
        }
        mBitmap = BitmapUtils.rotateBitmap(mBitmap!!, 90F)
        mImageView!!.setImageBitmap(mBitmap)
    }

    private fun snapImage() {
        mImageView!!.cropToCenter()
    }

    companion object {
        private const val REQUEST_CODE_READ_PERMISSION = 22
        private const val REQUEST_GALLERY = 21
        private const val TAG = "MainActivity"
    }

    override fun onEventClick() {
        loadNewImage(viewModel.galleryImagePath!!)
    }



}