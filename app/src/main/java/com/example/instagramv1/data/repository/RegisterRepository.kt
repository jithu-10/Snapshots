package com.example.instagramv1.data.repository

import com.example.instagramv1.model.UserData

interface RegisterRepository {

    suspend fun isUserNameAlreadyExist(userName : String) : Boolean

    suspend fun isEmailAlreadyExist(email : String) : Boolean

    suspend fun isPhoneAlreadyExist(phone : String) : Boolean

    suspend fun registerUser(userData: UserData) : Boolean
}