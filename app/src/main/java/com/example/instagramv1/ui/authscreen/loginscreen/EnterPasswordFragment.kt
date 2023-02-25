package com.example.instagramv1.ui.authscreen.loginscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.ui.mainscreen.MainActivity
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentEnterPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnterPasswordFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel>()
    private lateinit var binding: FragmentEnterPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_enter_password,container,false)
        binding.loginViewModel = viewModel
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        binding.tiPassword.addTextChangedListener(textWatcher)
        val passwordTextInputLayout = view.findViewById<TextInputLayout>(R.id.etPassword)
        val passwordTextInputEditText = view.findViewById<TextInputEditText>(R.id.tiPassword)
        passwordTextInputLayout.setEndIconOnClickListener {

            if(view.findViewById<TextInputEditText>(R.id.tiPassword).transformationMethod is HideReturnsTransformationMethod){

                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_closed_eye)
                passwordTextInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else{

                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_open_eye_24)
                passwordTextInputEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }
        }

    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.btnLogin.isEnabled = binding.tiPassword.text!!.isNotBlank()
        }

    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        view.clearFocus()
    }

    private fun setupListeners(){
        binding.btnLogin.setOnClickListener {
            hideSoftKeyboard(binding.tiPassword)
            lifecycleScope.launch {
                val result = viewModel.login()
                if(result){
                    saveLoginData()
                    startMainActivity()
                }
                else{
                    if(viewModel.password.isNotBlank()){
                        binding.etPassword.isErrorEnabled = true
                        binding.etPassword.error = "Incorrect Password"
                    }


                    viewModel.password = ""

                }
            }

        }
        binding.backImageBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnForgotPassword.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                addToBackStack(null)
                replace(R.id.frame_layout,OTPFragment())
                commit()
            }
        }

        binding.tiPassword.setOnFocusChangeListener{_,focused ->
            if(focused){
                binding.etPassword.isErrorEnabled = false
            }

        }


    }

    private fun saveLoginData(){
        val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE",Context.MODE_PRIVATE) ?: return
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