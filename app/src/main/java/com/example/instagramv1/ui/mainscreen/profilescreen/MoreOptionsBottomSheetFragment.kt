package com.example.instagramv1.ui.mainscreen.profilescreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.instagramv1.R
import com.example.instagramv1.ui.addpostscreen.EditPostActivity
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
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
            startDeletePostDialogFragment(postId)
        }
        view.findViewById<View>(R.id.editLayout).setOnClickListener {
            startEditPostActivity(postId)
        }


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

    private fun startEditPostActivity(postId: Int){
        val intent = Intent(requireActivity(),EditPostActivity::class.java).apply {
            putExtra("POST_ID",postId)
        }
        dismiss()
        startActivity(intent)
    }



}