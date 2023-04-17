package com.example.instagramv1.ui.addpostscreen

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.GalleryViewRecyclerAdapter
import com.example.instagramv1.cropimage.BitmapUtils
import com.example.instagramv1.cropimage.CropState
import com.example.instagramv1.cropimage.CropperView
import com.example.instagramv1.databinding.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CropImageFragment : Fragment(),GalleryViewRecyclerAdapter.OnEventListener {

    var mImageView: CropperView? = null
    private var mBitmap: Bitmap? = null
    private var imageUri : Uri? = null

    private val addPostViewModel by activityViewModels<AddPostViewModel>()
    private lateinit var cropImageBinding: FragmentCropImage2Binding
    private lateinit var galleryRecyclerView : RecyclerView
    private lateinit var galleryViewRecyclerAdapter : GalleryViewRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        cropImageBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_crop_image2, container, false)
        cropImageBinding.addPostViewModel = addPostViewModel
        return cropImageBinding.root
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
                addPostViewModel.galleryImagePath = path
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
        mBitmap = addPostViewModel.orginalImage


        galleryRecyclerView = view.findViewById<RecyclerView>(R.id.galleryRecyclerView)
        galleryRecyclerView.layoutManager = GridLayoutManager(requireActivity(),4)
        galleryViewRecyclerAdapter = GalleryViewRecyclerAdapter(addPostViewModel,this)
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
            requireActivity().finish()
        }


        mImageView = view.findViewById(R.id.imageview)
        val cursor = getImagesCursor()!!
        cursor.moveToFirst()

        if(addPostViewModel.galleryImagePath == null){
            addPostViewModel.galleryImagePath = cursor.getString(1)
        }
        loadNewImage(addPostViewModel.galleryImagePath!!)
        Log.d("Images New ",addPostViewModel.galleryImagePath.toString())
//        loadNewImage(addPostViewModel.galleryImagePath!!)

        cropImageBinding.openCamera.setOnClickListener {
            startCameraIntent()
        }
        view.findViewById<View>(R.id.btnAddPhoto).setOnClickListener {
            startGetPhotoDialogFragment()
        }
        view.findViewById<View>(R.id.btnAddPhotoTv).setOnClickListener{
            startGetPhotoDialogFragment()
        }
        view.findViewById<View>(R.id.btnContinue).setOnClickListener {
            if(cropImage()){
                parentFragmentManager.beginTransaction().apply {
                    addToBackStack(null)
                    replace(R.id.frame_layout, PostImageFragment())
                    commit()
                }
            }
            else{
                //Toast.makeText(requireActivity(),"No Image Found to Post",Toast.LENGTH_SHORT).show()
            }

        }
        view.findViewById<View>(R.id.rotate_button).setOnClickListener { rotateImage() }
        view.findViewById<View>(R.id.snap_button).setOnClickListener { snapImage() }
        mImageView!!.setDebug(true)
        mImageView!!.setGridCallback(object : CropperView.GridCallback {
            override fun onGestureStarted(): Boolean {
//                cropImageBinding.scrollView.scrollTo(0,0)
//                cropImageBinding.scrollView.setOnTouchListener( object : View.OnTouchListener{
//                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                        return false
//                    }
//
//
//                })
//                val imgr: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imgr.hideSoftInputFromWindow(cropImageBinding.tiDescription.windowToken,0)
                return true
            }

            override fun onGestureCompleted(): Boolean {
                return false
            }
        })
    }

    fun toggleGestures() {
        var enabled: Boolean = mImageView!!.isGestureEnabled()
        enabled = !enabled
        mImageView!!.setGestureEnabled(enabled)

    }

    private fun startGetPhotoDialogFragment(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val getPhotoDialogFragment = GetPhotoDialogFragment()
        getPhotoDialogFragment.show(parentFragmentManager,"dialog")
        //getPhotoDialogFragment.dialog?.window?.setLayout((6 * width)/7, (4 * height)/5);
    }

    private fun loadImage(bitmap : Bitmap){
        val display: Display = requireActivity().windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        val maxP = Math.max(bitmap.getWidth(), bitmap.getHeight())
        val scale1280 = maxP.toFloat() / 1280
        Log.i(TAG, "scaled: " + scale1280 + " - " + 1 / scale1280)
        Log.d("Special",mImageView!!.width.toString())
        mImageView!!.maxZoom = width * 2 / 1280f
        Log.d("Special",mImageView!!.maxZoom.toString())
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
//        if (!hasGalleryPermission()) {
//            askForGalleryPermission()
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

    private fun hasImagesPermission(): Boolean{
        return (ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasGalleryPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
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



    private fun cropImage() : Boolean {
        val result = mImageView!!.croppedBitmap
        if (result.state == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(requireActivity(), "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show()
            return false
        }
        val bitmap = result.bitmap
        return if (bitmap != null) {
            Log.d("Cropper", "crop1 bitmap: " + bitmap.width + ", " + bitmap.height)
            addPostViewModel.postImage = bitmap
            true
        } else{
            false
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
        private const val PICTURE_RESULT = 54
    }

    override fun onEventClick() {
        loadNewImage(addPostViewModel.galleryImagePath!!)
    }
}