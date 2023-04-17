package com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.instagramv1.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoveFollowerDialogFragment : DialogFragment() {

    private val viewModel by activityViewModels<ConnectionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.remove_follower_dialog_fragment,container,false)
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
        val otherUserId = arguments?.getInt("OTHER_USER_ID") ?: return
        view.findViewById<View>(R.id.removeBtn).setOnClickListener {
            removeFollower(otherUserId)
            dismiss()
        }

        view.findViewById<View>(R.id.allowAccessBtn).setOnClickListener {
            dismiss()
        }

    }

    private fun removeFollower(otherUserId : Int){
        viewModel.removeFollower(otherUserId)
        dismiss()
    }
}