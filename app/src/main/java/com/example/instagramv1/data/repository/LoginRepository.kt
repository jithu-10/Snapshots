package com.example.instagramv1.data.repository

interface LoginRepository {

    suspend fun isEmailExist(email : String) : Boolean

    suspend fun isPhoneExist(phone : String) : Boolean

    suspend fun isUserNameExist(userName : String) : Boolean

    suspend fun login(userInfo : String , password : String) : Int

    suspend fun loginViaOtp(userInfo : String) :Int


}