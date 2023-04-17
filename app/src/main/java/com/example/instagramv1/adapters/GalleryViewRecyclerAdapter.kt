package com.example.instagramv1.adapters

import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramv1.R
import com.example.instagramv1.databinding.GalleryViewBinding
import com.example.instagramv1.ui.addpostscreen.AddPostViewModel
import com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen.EditProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.coroutineContext


class GalleryViewRecyclerAdapter(private val viewModel : ViewModel,private val eventListener: OnEventListener)  :
    RecyclerView.Adapter<GalleryViewRecyclerAdapter.GalleryViewHolder>() {


    lateinit var cursor : Cursor
    var selectedItemView : View? = null
    var prevPosition : Int? = null



    inner class GalleryViewHolder(private val galleryViewBinding: GalleryViewBinding) : RecyclerView.ViewHolder(galleryViewBinding.root){

        fun bind(imagePath : String,position: Int){


            galleryViewBinding.galleryViewCardView.setOnClickListener {
                galleryViewBinding.galleryViewCardView2.visibility = View.VISIBLE
                if(viewModel is AddPostViewModel){
                    viewModel.galleryImagePath = imagePath
                }
                if(viewModel is EditProfileViewModel){
                    viewModel.galleryImagePath = imagePath
                }

                eventListener.onEventClick()
                notifyItemChanged(prevPosition!!)
                prevPosition = position
                //selectedItemView?.visibility = View.GONE
                //selectedItemView = galleryViewBinding.galleryViewCardView2

            }

            galleryViewBinding.galleryViewCardView2.setOnClickListener {

            }

            Glide.with(galleryViewBinding.galleryViewImageView)
                .load(imagePath)
                .into(galleryViewBinding.galleryViewImageView)
            if(viewModel is AddPostViewModel){
                if(imagePath == viewModel.galleryImagePath){
                    galleryViewBinding.galleryViewCardView2.visibility = View.VISIBLE
                    prevPosition = position
                    //selectedItemView = galleryViewBinding.galleryViewCardView2
                }
                else{
                    galleryViewBinding.galleryViewCardView2.visibility = View.GONE
                }
            }
            else if(viewModel is EditProfileViewModel){
                if(imagePath == viewModel.galleryImagePath){
                    galleryViewBinding.galleryViewCardView2.visibility = View.VISIBLE
                    prevPosition = position
                    //selectedItemView = galleryViewBinding.galleryViewCardView2
                }
                else{
                    galleryViewBinding.galleryViewCardView2.visibility = View.GONE
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val galleryViewBinding : GalleryViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.gallery_view, parent, false)
        return GalleryViewHolder(galleryViewBinding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        cursor.moveToPosition(position)
        Log.d("Images Path","Cursor Position : ${cursor.position}")
        Log.d("Images Path","Position : $position")
        val filePath  = cursor.getString(1)
        //Log.d("Images Path","file Path $filePath")
        if(filePath!=null){
            holder.bind(filePath,position)
        }

    }

    override fun getItemCount(): Int {

        return cursor.count

    }

    interface OnEventListener{
        fun onEventClick()
    }



}