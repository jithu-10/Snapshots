package com.example.instagramv1.ui.mainscreen.profilescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.instagramv1.R
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<PostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_options_bottomsheetfragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = arguments?.getInt("POST_ID") ?: return
        view.findViewById<View>(R.id.deleteLayout).setOnClickListener {
            //deletePost(postId)
            startDeletePostDialogFragment(postId)
        }

//        view.findViewById<View>(R.id.imgViewDelete).setOnClickListener {
//            deletePost(postId)
//        }
    }

    private fun deletePost(postId : Int){
        Toast.makeText(requireActivity(),"Post Deleted",Toast.LENGTH_SHORT).show()
        viewModel.deletePost(postId)
        dismiss()
    }

    private fun startDeletePostDialogFragment(postId: Int){
        val deletePostDialogFragment = DeletePostDialogFragment().apply {
            arguments = Bundle().apply {
                putInt("POST_ID",postId)
            }
        }
        dismiss()
        deletePostDialogFragment.show(parentFragmentManager,"dialog")
    }



}