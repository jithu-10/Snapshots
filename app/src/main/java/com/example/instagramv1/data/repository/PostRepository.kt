package com.example.instagramv1.data.repository

import androidx.lifecycle.LiveData
import com.example.instagramv1.data.entities.Post
import com.example.instagramv1.model.*

interface PostRepository {

    suspend fun postImage(postData: PostData)

    suspend fun getAllPublicPosts(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getAllPublicPostsAscending(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getAllPostsMostLiked(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getAllPostsMostCommented(userId: Int) : LiveData<List<PostViewData>>



    suspend fun getPostsByLocation(userId: Int, location : String) : LiveData<List<PostViewData>>

    suspend fun getUserPosts(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getUserSavedPosts(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getPost(postId: Int,userId: Int) : LiveData<PostViewData>

    suspend fun getFollowingPosts(userId: Int) : LiveData<List<PostViewData>>

    suspend fun getOtherUserPosts(userId: Int,otherUserId : Int) : LiveData<List<PostViewData>>

    suspend fun addReaction(userId : Int, postId : Int)

    suspend fun removeReaction(userId: Int, postId: Int)

    suspend fun getSavedStatus(userId : Int, postId: Int) : LiveData<Boolean>

    suspend fun getNumberOfReaction(postId: Int) : LiveData<Int>

    suspend fun getNumberOfComments(postId: Int) : LiveData<Int>

    suspend fun isUserReacted(userId: Int,postId: Int) : LiveData<Boolean>

    suspend fun savePost(userId: Int, postId: Int)

    suspend fun removeFromSavedPost(userId: Int, postId: Int)

    suspend fun getPostComments(postId: Int, userId: Int) : LiveData<List<CommentViewData>>

    suspend fun postComment(commentData: CommentData)

    suspend fun deleteComment(commentId: Int)

    suspend fun addCommentReaction(userId : Int, commentId : Int)

    suspend fun removeCommentReaction(userId: Int, commentId : Int)

    suspend fun deletePost(postId: Int)
}