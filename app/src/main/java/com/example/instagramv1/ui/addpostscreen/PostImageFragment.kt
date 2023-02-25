package com.example.instagramv1.ui.addpostscreen

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentPostImageBinding
import com.example.instagramv1.ui.mainscreen.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PostImageFragment : Fragment() {

    private val addPostViewModel by activityViewModels<AddPostViewModel>()
    private lateinit var postImageBinding: FragmentPostImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        postImageBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_post_image, container, false)
        postImageBinding.addPostViewModel = addPostViewModel
        return postImageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

//        if(addPostViewModel.location.isBlank()){
//            postImageBinding.notGivenLocation.visibility = View.VISIBLE
//        }
//        else{
//            postImageBinding.notGivenLocation.visibility = View.GONE
//        }
//
//        if(addPostViewModel.description.isBlank()){
//            postImageBinding.notGivenDescription.visibility = View.VISIBLE
//        }
//        else{
//            postImageBinding.notGivenDescription.visibility = View.GONE
//        }

        postImageBinding.btnPost.setOnClickListener {
            lifecycleScope.launch {
                addPostViewModel.postImage()
                postImageBinding.pbLoading.visibility = ProgressBar.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                //delay(3000)
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                requireActivity().finish()
            }
        }

//        postImageBinding.tiDescription.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> // if enter is pressed start calculating
//            if (keyCode == KeyEvent.KEYCODE_ENTER
//            ) {
//
//                // get EditText text
//                val text = (v as EditText).text.toString()
//
//                // find how many rows it cointains
//                val editTextRowCount = text.split("\\n").toTypedArray().size
//
//                // user has input more than limited - lets do something
//                // about that
//                if (editTextRowCount >= 5) {
//
//                    // find the last break
//                    val lastBreakIndex = text.lastIndexOf("\n")
//
//                    // compose new text
//                    val newText = text.substring(0, lastBreakIndex)
//
//                    // add new text - delete old one and append new one
//                    // (append because I want the cursor to be at the end)
//                    (v as EditText).setText("")
//                    (v as EditText).append(newText)
//                }
//            }
//            false
//        })
    }

}