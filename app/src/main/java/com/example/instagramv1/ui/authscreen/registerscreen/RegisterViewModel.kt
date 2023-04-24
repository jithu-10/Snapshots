package com.example.instagramv1.ui.authscreen.registerscreen

import androidx.lifecycle.ViewModel
import com.example.instagramv1.data.repository.RegisterRepository
import com.example.instagramv1.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel(){

    var userName : String = ""
    var fullName : String = ""
    var email : String = ""
    var password : String = ""
    var confirmPassword : String = ""
    var phone : String = ""



    suspend fun isUserNameAlreadyExist() : Boolean{
        return registerRepository.isUserNameAlreadyExist(userName)

    }

    suspend fun isEmailAlreadyExist() : Boolean{
        return registerRepository.isEmailAlreadyExist(email.lowercase())

    }

    suspend fun isPhoneAlreadyExist() : Boolean{

        return registerRepository.isPhoneAlreadyExist(phone)
    }

    suspend fun registerUser() : Boolean{
        val registerData = UserData(userName,fullName,email.lowercase(),phone,password)
        return registerRepository.registerUser(registerData)
    }
}