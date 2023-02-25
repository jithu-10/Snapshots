package com.example.instagramv1.data.repository

import androidx.lifecycle.LiveData
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.model.*

interface SampleRepository {


    suspend fun getUserEmail(userId : Int) : LiveData<String>
    suspend fun getUserPhone(userId : Int) : LiveData<String>

}