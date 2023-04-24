package com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.RegisterRepository
import com.example.instagramv1.data.repository.UserRepository

import com.example.instagramv1.model.UserEditableData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var userId = -1

    var profilePicture : Bitmap? = null
    var originalPicture : Bitmap? = null
    var fullName : String? = null
    var userName : String? = null
    var phone : String? = null
    var email : String? = null

    var galleryImagePath : String? = null

    var removePictureOption = false

    var previousUserName : String? = null
    var previousPhone : String ? = null
    var previousEmail : String ?  = null

    var profilePictureChangedLiveData : MutableLiveData<Int> = MutableLiveData(0)


    suspend fun getUserEditableData() : LiveData<UserEditableData>{
        return userRepository.getUserEditableData(userId)
    }

    suspend fun updateUserEditableData() {
        userRepository.updateUserData(UserEditableData(email!!.lowercase(),phone!!,userName!!,fullName!!,profilePicture),userId)
    }

    suspend fun isUserNameAlreadyExist() : Boolean{
        if(previousUserName == userName){
            return false
        }
        return userRepository.isUserNameAlreadyExist(userName!!)
    }

    suspend fun isEmailAlreadyExist() : Boolean{
        if(previousEmail == email!!.lowercase()){
            return false
        }
        return userRepository.isEmailAlreadyExist(email!!.lowercase())
    }

    suspend fun isPhoneAlreadyExist() : Boolean{
        if(previousPhone == phone){
            return false
        }
        return userRepository.isPhoneAlreadyExist(phone!!)
    }

    fun removeProfilePicture() {
        viewModelScope.launch {
            userRepository.removeProfilePicture(userId)
        }
    }





}