package com.example.instagramv1.ui.authscreen.loginscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentLoginBinding
import com.example.instagramv1.ui.authscreen.registerscreen.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel>()
    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login, container, false)
        loginBinding.loginViewModel = viewModel
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        loginBinding.userNameOrEmailEt.addTextChangedListener(textWatcher)

    }

    private val textWatcher : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            loginBinding.btnContinue.isEnabled = loginBinding.userNameOrEmailEt.text!!.isNotBlank()
        }

    }

    private fun setupListeners(){
        loginBinding.btnCreateAccount.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                addToBackStack(null)
                replace(R.id.frame_layout,RegisterFragment())
                commit()
            }
        }

        loginBinding.btnContinue.setOnClickListener {
            hideSoftKeyboard(loginBinding.userNameOrEmailEt)
            viewModel.password = ""
            lifecycleScope.launch {
                val result = viewModel.checkUserInfo()
                if(result){
                    parentFragmentManager.beginTransaction().apply {
                        addToBackStack(null)
                        replace(R.id.frame_layout,EnterPasswordFragment())
                        commit()
                    }
                }
                else{
                    if(viewModel.userInfo.isNotBlank()){
                        loginBinding.etUsernameOrEmail.isErrorEnabled = true
                        loginBinding.etUsernameOrEmail.error = "User not exist"
                    }



                }
            }
        }

        loginBinding.btnForgotPassword.setOnClickListener {
            viewModel.otpUserInfo = ""
            parentFragmentManager.beginTransaction().apply {
                addToBackStack(null)
                replace(R.id.frame_layout,OTPFragment())
                commit()
            }
        }

        loginBinding.userNameOrEmailEt.setOnFocusChangeListener{_,focused ->
            if(focused){
                loginBinding.etUsernameOrEmail.isErrorEnabled = false
            }
        }

    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        view.clearFocus()
    }

}