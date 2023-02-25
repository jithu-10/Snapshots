package com.example.instagramv1.data.repository.impl

import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.instagramv1.data.dao.AccountDao
import com.example.instagramv1.data.dao.SampleDao
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.data.repository.SampleRepository
import com.example.instagramv1.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SampleRepositoryImpl(
    private val sampleDao: SampleDao,
    private val accountDao: AccountDao
) : SampleRepository {






    override suspend fun getUserEmail(userId: Int): LiveData<String> {
        return sampleDao.getUserEmail(userId)
    }

    override suspend fun getUserPhone(userId: Int): LiveData<String> {
        return sampleDao.getUserPhone(userId)
    }


//    override suspend fun getUserProfileMutableData(userId: Int): LiveData<UserProfileMutableData2> {
//        val user = sampleDao.getUser(userId)
//        val userEmail  = sampleDao.getUserEmail(userId)
//        val value = userEmail.value
//
//
//        val userPhone = sampleDao.getUserPhone(userId)
//
//        val userProfileMutableData  = MediatorLiveData<UserProfileMutableData2>()
//
//        userProfileMutableData.addSource(user){
//            userProfileMutableData.value?.profilePicture = user.value?.profilePicture
//            userProfileMutableData.value?.userName = user.value?.userName
//            userProfileMutableData.value?.userId = user.value?.userId
//            userProfileMutableData.value?.fullName = user.value?.fullName
//        }
//
//        userProfileMutableData.addSource(userPhone){
//            userProfileMutableData.value?.phone = userPhone.value
//        }
//
//        userProfileMutableData.addSource(userEmail){
//            userProfileMutableData.value?.email = userEmail.value
//        }
//
//        return userProfileMutableData
//    }




//        val userProfileMutableData : LiveData<UserProfileMutableData> = MutableLiveData(
//            UserProfileMutableData(
//                user.value!!.userId,
//                user.value!!.userName,
//                user.value!!.fullName,
//                user.value!!.profilePicture,
//                userPhone.value!!,
//                userEmail.value!!
//            )
//        )
//        return userProfileMutableData

}