package com.example.instagramv1.ui.searchscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.data.repository.UserRepository
import com.example.instagramv1.model.PostViewData
import com.example.instagramv1.model.UserMiniProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val postRepository: PostRepository,
    val userRepository: UserRepository
) : ViewModel() {

    var userId = -1

    suspend fun getPostByLocation(location : String) : LiveData<List<PostViewData>>{
        if(location.isBlank()){
            return MutableLiveData(emptyList())
        }
        return postRepository.getPostsByLocation(userId,location)
    }

    suspend fun getUsersByName(name : String) : LiveData<List<UserMiniProfileData>>{
        if(name.isBlank()){
            return MutableLiveData(emptyList())
        }
        return userRepository.getUsers(name)
    }

    suspend fun getSearchHistory() : LiveData<List<String>>{
        return userRepository.getUserSearchHistory(userId)
    }

    fun addSearchHistory(historyText : String){
        viewModelScope.launch {
            Log.d("Search History",historyText)
            userRepository.addSearchHistory(userId,historyText)
        }
    }

    fun deleteSearchHistory(historyText: String){
        viewModelScope.launch {
            userRepository.deleteSearchHistory(userId,historyText)
        }
    }

    fun deleteAllSearchHistory(){
        viewModelScope.launch {
            userRepository.deleteAllSearchHistory(userId)
        }
    }

}