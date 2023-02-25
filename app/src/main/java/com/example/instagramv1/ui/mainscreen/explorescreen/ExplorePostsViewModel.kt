package com.example.instagramv1.ui.mainscreen.explorescreen


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramv1.data.entities.Post
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.model.FilterOptions
import com.example.instagramv1.model.PostViewData
import com.example.instagramv1.ui.mainscreen.PostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExplorePostsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel(){

    var userId = -1;
    var filterOptions = FilterOptions.SORT_BY_DATE_DESC

    suspend fun getAllPosts() : LiveData<List<PostViewData>>{
        return postRepository.getAllPublicPosts(userId).apply {
            Log.d("Special Logs", this.value?.size.toString())
        }
    }

    suspend fun getExplorePosts() : LiveData<List<PostViewData>>{
        return postRepository.getAllPublicPosts(userId)
//        if(filterOptions == FilterOptions.SORT_BY_DATE_ASC){
//            return postRepository.getAllPublicPostsAscending(userId)
//        }
//        else {
//            return postRepository.getAllPublicPosts(userId)
//
//        }
    }


    suspend fun getAllPostsAscending() : LiveData<List<PostViewData>>{
        return postRepository.getAllPublicPostsAscending(userId).apply {
            Log.d("Special Logs", this.value?.size.toString())
        }
    }

    suspend fun getAllPostsByMostLiked() : LiveData<List<PostViewData>>{
        return postRepository.getAllPostsMostLiked(userId)
    }

    suspend fun getAllPostsByMostCommented() : LiveData<List<PostViewData>>{
        return postRepository.getAllPostsMostCommented(userId)
    }


    suspend fun getFollowingPosts() : LiveData<List<PostViewData>>{
        return postRepository.getFollowingPosts(userId)
    }

    suspend fun getUserPosts() : LiveData<List<PostViewData>>{
        return postRepository.getUserPosts(userId)
    }

    suspend fun getUserSavedPosts() : LiveData<List<PostViewData>>{
        return postRepository.getUserSavedPosts(userId)
    }

    suspend fun  getOtherUserPosts(other_user_id : Int) : LiveData<List<PostViewData>>{
        return postRepository.getOtherUserPosts(userId,other_user_id)
    }



}