package com.example.instagramv1.ui.authscreen.loginscreen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentEnterOTPBinding
import com.example.instagramv1.ui.mainscreen.MainActivity
import kotlinx.coroutines.launch


class EnterOTPFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel> ()
    private lateinit var binding : FragmentEnterOTPBinding
    private var fragmentView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_enter_o_t_p, container, false)
            val view = binding.root
            fragmentView = view
            setupListeners()
            binding.tiOTP.addTextChangedListener(textWatcher)

            return fragmentView
        }

        return fragmentView
    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.btnSubmit.isEnabled = binding.tiOTP.text!!.isNotBlank()
        }

    }




    private fun setupListeners(){
        binding.btnSubmit.setOnClickListener {
            viewModel.otp = binding.tiOTP.text.toString()
            hideSoftKeyboard(binding.tiOTP)
            if(viewModel.otp == viewModel.givenOtp){
                lifecycleScope.launch{
                    viewModel.loginViaOtp()
                    saveLoginData()
                    startMainActivity()
                }

            }
            else{
                if(viewModel.otp.isNotBlank()){
                    binding.etEnterOtp.isErrorEnabled = true
                    binding.etEnterOtp.error = "Invalid OTP"
                }
            }

        }


        binding.backImageBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tiOTP.setOnFocusChangeListener { _, focused ->
            if(focused){
                binding.etEnterOtp.isErrorEnabled = false
            }
        }

    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        view.clearFocus()
    }

    private fun saveLoginData(){
        val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val edit = sharedPreferences.edit()
        val value : Int = viewModel.userId.value!!
        edit.putInt("USER_ID",viewModel.userId.value!!)
        Log.d("User Check",value.toString())
        edit.apply()
    }

    private fun startMainActivity(){
        Intent(requireActivity(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }


}