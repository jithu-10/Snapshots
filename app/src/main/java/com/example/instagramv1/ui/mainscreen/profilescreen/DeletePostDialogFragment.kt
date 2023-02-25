package com.example.instagramv1.ui.mainscreen.profilescreen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.instagramv1.R
import com.example.instagramv1.ui.addpostscreen.GetPhotoDialogFragment
import com.example.instagramv1.ui.mainscreen.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletePostDialogFragment : DialogFragment() {

    private val viewModel by activityViewModels<PostViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.delete_post_dialog_fragment,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
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

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = arguments?.getInt("POST_ID") ?: return
        view.findViewById<View>(R.id.deleteBtn).setOnClickListener {
            deletePost(postId)
            dismiss()
        }

        view.findViewById<View>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }
    }

    private fun deletePost(postId : Int){
        Toast.makeText(requireActivity(),"Post Deleted", Toast.LENGTH_SHORT).show()
        viewModel.deletePost(postId)
        dismiss()
    }
}