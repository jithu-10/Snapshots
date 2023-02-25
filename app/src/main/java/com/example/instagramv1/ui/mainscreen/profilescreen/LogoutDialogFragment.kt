package com.example.instagramv1.ui.mainscreen.profilescreen

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.instagramv1.R
import com.example.instagramv1.ui.authscreen.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialogFragment : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.logout_dialog_fragment,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            900,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.logoutBtn).setOnClickListener {
            Intent(requireActivity(), AuthActivity::class.java).apply {
                val preferences: SharedPreferences = requireActivity().getSharedPreferences("COMMON_PREFERENCE",0x0000)
                preferences.edit().remove("USER_ID").apply()
                startActivity(this)
                requireActivity().finish()
                viewModelStore.clear()
            }
        }

        view.findViewById<View>(R.id.cancelBtn).setOnClickListener {
            dismiss()
        }
    }
}