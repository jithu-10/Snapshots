package com.example.instagramv1.ui.authscreen.registerscreen

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentRegisterBinding
import com.example.instagramv1.ui.authscreen.loginscreen.LoginViewModel
import com.example.instagramv1.utils.ValidationErrorMessage
import com.example.instagramv1.utils.Validators
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel by viewModels<RegisterViewModel>()
    private val activityViewModel by activityViewModels<LoginViewModel>()

    private lateinit var registerBinding: FragmentRegisterBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_register, container, false)

        registerBinding.registerViewModel = viewModel
        registerBinding.lifecycleOwner = requireActivity()
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun addTextWatcher(){
        registerBinding.userNameEt.addTextChangedListener(textWatcher)
        registerBinding.fullNameEt.addTextChangedListener(textWatcher)
        registerBinding.phoneEt.addTextChangedListener(textWatcher)
        registerBinding.emailEt.addTextChangedListener(textWatcher)
        registerBinding.passwordEt.addTextChangedListener(textWatcher)
        registerBinding.confirmPasswordEt.addTextChangedListener(textWatcher)
    }

    private val textWatcher : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            registerBinding.btnSignUp.isEnabled = !(registerBinding.userNameEt.text!!.isBlank()  ||
                    registerBinding.fullNameEt.text!!.isBlank() ||
                    registerBinding.emailEt.text!!.isBlank()  ||
                    registerBinding.phoneEt.text!!.isBlank()  ||
                    registerBinding.passwordEt.text!!.isBlank() ||
                    registerBinding.confirmPasswordEt.text!!.isBlank())
        }

    }


    private fun setupListeners(){
        userNameInputTextOnFocusListener()
        fullNameInputTextOnFocusListener()
        emailInputTextOnFocusListener()
        phoneInputTextOnFocusListener()
        passwordInputTextOnFocusListener()
        confirmPasswordInputTextOnFocusListener()
        addTextWatcher()
        registerBinding.btnSignUp.setOnClickListener {
            registerBinding.fullNameEt.clearFocus()
            registerBinding.userNameEt.clearFocus()
            registerBinding.phoneEt.clearFocus()
            registerBinding.emailEt.clearFocus()
            registerBinding.passwordEt.clearFocus()
            registerBinding.confirmPasswordEt.clearFocus()
            lifecycleScope.launch{

                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                registerBinding.pbLoading.visibility = ProgressBar.VISIBLE
                val result = register()
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if(result){
                    activityViewModel.userInfo = viewModel.userName
                    activityViewModel.password = viewModel.password
                    parentFragmentManager.beginTransaction().apply {
                        addToBackStack(null)
                        replace(R.id.frame_layout,RegisteredSuccessfullyFragment())
                        commit()
                    }
                }

                registerBinding.pbLoading.visibility = ProgressBar.INVISIBLE




            }
        }

        registerBinding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private suspend fun register() : Boolean{
        if(completeValidation()){
            return viewModel.registerUser()
        }
        return false
    }

    private suspend fun completeValidation() : Boolean{
        var filled = true
        val fullNameValidation = Validators.validateFullName(registerBinding.fullNameEt.text.toString())
        if(fullNameValidation != null){
            filled = false
            registerBinding.etFullName.isErrorEnabled = true
            registerBinding.etFullName.error = fullNameValidation.message
        }
        val userNameValidation = Validators.validateUserName(registerBinding.userNameEt.text.toString())
        if(userNameValidation != null){
            filled = false
            registerBinding.etUserName.isErrorEnabled = true
            registerBinding.etUserName.error = userNameValidation.message
        }
        else if(viewModel.isUserNameAlreadyExist()){
            filled = false
            registerBinding.etUserName.isErrorEnabled = true
            registerBinding.etUserName.error = ValidationErrorMessage.USER_NAME_ALREADY_EXIST.message
        }

        val phoneValidation = Validators.validatePhone(registerBinding.phoneEt.text.toString())
        if(phoneValidation != null){
            filled = false
            registerBinding.etPhone.isErrorEnabled = true
            registerBinding.etPhone.error = phoneValidation.message
        }
        else if(viewModel.isPhoneAlreadyExist()){
            filled = false
            registerBinding.etPhone.isErrorEnabled = true
            registerBinding.etPhone.error = ValidationErrorMessage.PHONE_ALREADY_EXIST.message
        }

        val emailValidation = Validators.validateEmail(registerBinding.emailEt.text.toString())
        if(emailValidation != null){
            filled = false
            registerBinding.etEmail.isErrorEnabled = true
            registerBinding.etEmail.error = emailValidation.message
        }
        else if(viewModel.isEmailAlreadyExist()){
            filled = false
            registerBinding.etEmail.isErrorEnabled = true
            registerBinding.etEmail.error = ValidationErrorMessage.EMAIL_ALREADY_EXIST.message
        }

        val passwordValidation = Validators.validatePassword(registerBinding.passwordEt.text.toString())
        if(passwordValidation != null){
            filled = false
            registerBinding.etPassword.isErrorEnabled = true
            registerBinding.etPassword.error = passwordValidation.message
//            registerBinding.passwordEt.setText("")
//            registerBinding.etPassword.isErrorEnabled = false
        }

        val confirmPasswordValidation = Validators.validateConfirmPassword(registerBinding.passwordEt.text.toString(),registerBinding.confirmPasswordEt.text.toString())
        if(confirmPasswordValidation != null){
            filled = false
            registerBinding.etConfirmPassword.isErrorEnabled = true
            registerBinding.etConfirmPassword.error = confirmPasswordValidation.message
//            registerBinding.confirmPasswordEt.setText("")
//            registerBinding.etConfirmPassword.isErrorEnabled = false
        }
        return filled
    }







    private fun validatePassword(): String? {
        val error = Validators.validatePassword(registerBinding.passwordEt.text.toString())
        if(error == ValidationErrorMessage.PASSWORD_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateName(): String? {
        val error = Validators.validateFullName(registerBinding.fullNameEt.text.toString())
        if(error == ValidationErrorMessage.NAME_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateUserName(): String? {
        val error = Validators.validateUserName(registerBinding.userNameEt.text.toString())
        if(error == ValidationErrorMessage.USER_NAME_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateEmail(): String?{
        val error = Validators.validateEmail(registerBinding.emailEt.text.toString())
        if(error == ValidationErrorMessage.EMAIL_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validatePhone(): String?{
        val error =  Validators.validatePhone(registerBinding.phoneEt.text.toString())
        if(error == ValidationErrorMessage.PHONE_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateConfirmPassword() : String?{
        val error =  Validators.validateConfirmPassword(registerBinding.passwordEt.text.toString(),registerBinding.confirmPasswordEt.text.toString())
        if(error == ValidationErrorMessage.CONFIRM_PASSWORD_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun userNameInputTextOnFocusListener() {
        registerBinding.userNameEt.setOnFocusChangeListener { _ , focused ->
            if (!focused) {
                val validation = validateUserName()
                if(validation!=null){
                    registerBinding.etUserName.isErrorEnabled = true
                    registerBinding.etUserName.error = validation
                }
                else{
                    registerBinding.etUserName.isErrorEnabled  = false
                }
            }
            else{
                registerBinding.etUserName.isErrorEnabled = false
            }
        }
    }

    private fun fullNameInputTextOnFocusListener() {
        registerBinding.fullNameEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validateName()
                if(validation!=null){
                    registerBinding.etFullName.isErrorEnabled = true
                    registerBinding.etFullName.error = validation
                }
                else{
                    registerBinding.etFullName.isErrorEnabled  = false
                }
            }
            else{
                registerBinding.etFullName.isErrorEnabled = false
            }
        }
    }

    private fun emailInputTextOnFocusListener() {
        registerBinding.emailEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validateEmail()
                if(validation!=null){
                    registerBinding.etEmail.isErrorEnabled = true
                    registerBinding.etEmail.error = validation
                }
                else{
                    registerBinding.etEmail.isErrorEnabled  = false
                }
            }
            else{
                registerBinding.etEmail.isErrorEnabled = false
            }
        }
    }

    private fun phoneInputTextOnFocusListener() {
        registerBinding.phoneEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validatePhone()
                if(validation!=null){
                    registerBinding.etPhone.isErrorEnabled = true
                    registerBinding.etPhone.error = validation
                }
                else{
                    registerBinding.etPhone.isErrorEnabled  = false
                    registerBinding.etPhone.error = null
                }
            }
            else{
                registerBinding.etPhone.isErrorEnabled = false
            }
        }
    }

    private fun passwordInputTextOnFocusListener() {
        registerBinding.passwordEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validatePassword()
                if(validation!=null){
                    registerBinding.etPassword.isErrorEnabled = true
                    registerBinding.etPassword.error = validation
                }
                else{
                    registerBinding.etPassword.isErrorEnabled  = false
                    registerBinding.etPassword.error = null
                }

                val validation2 = validateConfirmPassword()
                if(validation2!=null){
                    registerBinding.etConfirmPassword.isErrorEnabled = true
                    registerBinding.etConfirmPassword.error = validation2
                }
                else{
                    registerBinding.etConfirmPassword.isErrorEnabled = false
                    registerBinding.etConfirmPassword.error = null
                }
            }
            else{
                registerBinding.etPassword.isErrorEnabled = false
                registerBinding.etConfirmPassword.isErrorEnabled = false
            }
        }
    }

    private fun confirmPasswordInputTextOnFocusListener() {
        registerBinding.confirmPasswordEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validateConfirmPassword()
                if(validation!=null){
                    registerBinding.etConfirmPassword.isErrorEnabled = true
                    registerBinding.etConfirmPassword.error = validation
                }
                else{
                    registerBinding.etConfirmPassword.isErrorEnabled  = false
                    registerBinding.etConfirmPassword.error = null
                }
            }
            else{
                registerBinding.etConfirmPassword.isErrorEnabled = false
            }
        }
    }





}