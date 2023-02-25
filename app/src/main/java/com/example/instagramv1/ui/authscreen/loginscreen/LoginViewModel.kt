package com.example.instagramv1.ui.authscreen.loginscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramv1.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository : LoginRepository
) : ViewModel() {

    var userInfo : String  = ""
    var password : String = ""
    var otpUserInfo : String = ""
    var otp : String = ""
    var givenOtp : String = ""

    private var _userId = MutableLiveData<Int>(-1)
    val userId : LiveData<Int> = _userId


    suspend fun checkUserInfo() : Boolean{

        if(loginRepository.isEmailExist(userInfo) || loginRepository.isUserNameExist(userInfo) || loginRepository.isPhoneExist(userInfo)){
            return true
        }
        return false
    }

    suspend fun checkOtpUserInfo() : Boolean{

        if(loginRepository.isEmailExist(otpUserInfo) || loginRepository.isPhoneExist(otpUserInfo)){
            return true
        }
        return false
    }





    suspend fun login() : Boolean{
        _userId.value = loginRepository.login(userInfo,password)
        if(_userId.value != -1){
            return true
        }
        return false
    }

    suspend fun loginViaOtp() {
        _userId.value = loginRepository.loginViaOtp(otpUserInfo)

    }





}