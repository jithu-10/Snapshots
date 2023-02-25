package com.example.instagramv1.ui.mainscreen.profilescreen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.BottomSheetSettingsBinding
import com.example.instagramv1.ui.authscreen.AuthActivity
import com.example.instagramv1.ui.mainscreen.UserProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<UserProfileViewModel>()
    private lateinit var settingsBinding : BottomSheetSettingsBinding

    override fun onStart() {
        super.onStart()
        //this forces the sheet to appear at max height even on landscape
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = DataBindingUtil.inflate(inflater,
            R.layout.bottom_sheet_settings, container, false)
        settingsBinding.viewModel = viewModel
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        settingsBinding.privateSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsBinding.privateSwitch.isChecked = isChecked
            viewModel.changePrivacy(isChecked)
        }

        val sharedPref = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val theme = sharedPref.getInt("THEME", MODE_NIGHT_FOLLOW_SYSTEM)
        if(theme == MODE_NIGHT_YES){
            view.findViewById<RadioButton>(R.id.darkBtn).isChecked = true
        }
        else if(theme == MODE_NIGHT_NO){
            view.findViewById<RadioButton>(R.id.lightBtn).isChecked = true
        }
        else if(theme == MODE_NIGHT_FOLLOW_SYSTEM){
            view.findViewById<RadioButton>(R.id.defaultBtn).isChecked = true

        }

        view.findViewById<View>(R.id.aboutLayout).setOnClickListener {
            dismiss()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_page,AboutFragment())
                addToBackStack(null)
                commit()
            }
        }



        view.findViewById<RadioButton>(R.id.darkBtn).setOnClickListener {
            setDefaultNightMode(MODE_NIGHT_YES)
            val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE)
            val edit = sharedPreferences?.edit()
            val value : Int = MODE_NIGHT_YES
            edit?.putInt("THEME",value)
            edit?.apply()
        }

        view.findViewById<RadioButton>(R.id.lightBtn).setOnClickListener {
            setDefaultNightMode(MODE_NIGHT_NO)
            val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE)
            val edit = sharedPreferences?.edit()
            val value : Int = MODE_NIGHT_NO
            edit?.putInt("THEME",value)
            edit?.apply()
        }

        view.findViewById<RadioButton>(R.id.defaultBtn).setOnClickListener {
            setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE)
            val edit = sharedPreferences?.edit()
            val value : Int = MODE_NIGHT_FOLLOW_SYSTEM
            edit?.putInt("THEME",value)
            edit?.apply()
        }

        view.findViewById<ConstraintLayout>(R.id.logoutLayout).setOnClickListener {

//            Intent(requireActivity(),AuthActivity::class.java).apply {
//                val preferences: SharedPreferences = requireActivity().getSharedPreferences("COMMON_PREFERENCE",0x0000)
//                preferences.edit().remove("USER_ID").apply()
//                startActivity(this)
//                requireActivity().finish()
//                viewModelStore.clear()
//            }

            val logoutDialogFragment = LogoutDialogFragment()
            dismiss()
            logoutDialogFragment.show(parentFragmentManager,"dialog")
        }
    }


    private fun fetchData(){
        lifecycleScope.launch{
            val userData = viewModel.getUserProfileData()
            userData.observe(viewLifecycleOwner){
                viewModel.privateAccount = userData.value?.private_account
                if(userData.value?.private_account!=null){
                    settingsBinding.privateSwitch.isChecked = userData.value?.private_account!!
                }
            }

        }
    }
}