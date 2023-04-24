package com.example.instagramv1.ui.authscreen.registerscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.ui.authscreen.loginscreen.LoginViewModel
import com.example.instagramv1.ui.mainscreen.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisteredSuccessfullyFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registered_successfully, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch{
            val result = viewModel.login()
            if(result){
                saveLoginData()
                delay(1000)
                startMainActivity()
            }
//            delay(3000)
//            withContext(Dispatchers.Main){
//                parentFragmentManager.popBackStack()
//                parentFragmentManager.popBackStack()
//            }
        }
    }

    private fun saveLoginData(){
        val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val edit = sharedPreferences.edit()
        val value : Int = viewModel.userId.value!!
        edit.putInt("USER_ID",viewModel.userId.value!!)
        edit.apply()
    }

    private fun startMainActivity(){
        Intent(requireActivity(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

}