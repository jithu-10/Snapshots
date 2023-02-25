package com.example.instagramv1.data.repository.impl

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.instagramv1.data.dao.*
import com.example.instagramv1.data.entities.*
import com.example.instagramv1.data.repository.PostRepository
import com.example.instagramv1.model.*
import java.util.*

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val reactionDao : ReactionDao,
    private val commentDao : CommentDao,
    private val commentReactionDao: CommentReactionDao,
    private val savedPostDao: SavedPostDao,
    private val notificationDao: NotificationDao
) : PostRepository {

    override suspend fun postImage(postData: PostData) {
        val post =
            Post(
                postData.userId,
                Date(),
                postData.image,
                postData.description,
                postData.location
            )
        postDao.insertPost(post)
    }


    override suspend fun getAllPublicPosts(userId : Int): LiveData<List<PostViewData>> {
        return postDao.getAllPublicPosts(userId).apply {
            Log.d("Special Logs", this.value?.size.toString())
        }
        //return postDao.getFollowingPosts(userId)
    }

    override suspend fun getAllPublicPostsAscending(userId: Int): LiveData<List<PostViewData>> {
        return postDao.getAllPublicPostsAscending(userId)
    }

    override suspend fun getAllPostsMostLiked(userId: Int): LiveData<List<PostViewData>> {
        return postDao.getAllPostsMostLiked(userId)
    }

    override suspend fun getAllPostsMostCommented(userId: Int): LiveData<List<PostViewData>> {
        return postDao.getAllPostsMostCommented(userId)
    }

    override suspend fun getFollowingPosts(userId: Int) : LiveData<List<PostViewData>>{
        return postDao.getFollowingPosts(userId)
    }

    override suspend fun getPostsByLocation(
        userId: Int,
        location: String
    ): LiveData<List<PostViewData>> {
        var _location = location.trim().lowercase()
        _location = "$_location%"
        return postDao.getPostByLocation(userId,_location).apply {
            Log.d("Search Places",this.value?.size.toString())
        }
    }

    override suspend fun getUserPosts(userId: Int): LiveData<List<PostViewData>> {
        return postDao.getUserPosts(userId)
    }

    override suspend fun getUserSavedPosts(userId: Int): LiveData<List<PostViewData>> {
        return postDao.getUserSavedPosts(userId)
    }

    override suspend fun getPost(postId: Int, userId: Int): LiveData<PostViewData> {
        return postDao.getPost(postId,userId)
    }

    override suspend fun getOtherUserPosts(
        userId: Int,
        otherUserId: Int
    ): LiveData<List<PostViewData>> {
        return postDao.getOtherUserPosts(userId,otherUserId);

    }

    override suspend fun addReaction(userId: Int, postId: Int) {
        if(reactionDao.getReactions(postId).contains(userId)){
            return
        }
        reactionDao.insertReaction(Reaction(postId,userId))
        val postOwnerId = postDao.getPostOwnerId(postId)
        if(postOwnerId!=userId){
            notificationDao.addNotification(Notification(userId,postOwnerId,"liked your Post",Date()))
        }

    }

    override suspend fun removeReaction(userId: Int, postId: Int) {
        reactionDao.deleteReaction(userId,postId)
        val postOwnerId = postDao.getPostOwnerId(postId)
        if(postOwnerId!=userId){
            notificationDao.deleteNotification(userId,postOwnerId,"liked your Post")
        }
    }

    override suspend fun getSavedStatus(userId: Int, postId: Int) : LiveData<Boolean>{
        return savedPostDao.getSavedStatus(postId, userId)
    }

    override suspend fun getNumberOfReaction(postId: Int): LiveData<Int> {
        return reactionDao.getNumberOfReaction(postId)
    }



    override suspend fun getNumberOfComments(postId: Int): LiveData<Int> {
        return commentDao.getNumberOfComments(postId)
    }

    override suspend fun isUserReacted(userId : Int,postId: Int) : LiveData<Boolean>{
        return reactionDao.isUserReacted(userId,postId)
    }

    override suspend fun savePost(userId: Int, postId: Int) {
        savedPostDao.insertSavedPost(SavedPost(postId,userId))
        val postOwnerId = postDao.getPostOwnerId(postId)
        if(postOwnerId!=userId){
            notificationDao.addNotification(Notification(userId,postOwnerId,"saved your Post",Date()))
        }
    }

    override suspend fun removeFromSavedPost(userId: Int, postId: Int) {
        savedPostDao.deleteSavedPost(userId,postId)
        val postOwnerId = postDao.getPostOwnerId(postId)
        if(postOwnerId!=userId){
            notificationDao.deleteNotification(userId,postOwnerId,"saved your Post")
        }
    }

    override suspend fun getPostComments(postId: Int, userId: Int) : LiveData<List<CommentViewData>>{
        return commentDao.getComments(postId,userId)
    }

    override suspend fun postComment(commentData: CommentData) {
        commentDao.insertComment(Comment(commentData.userId,commentData.postId,Date(),commentData.commentDesc))
        val postOwnerId = postDao.getPostOwnerId(commentData.postId)

        if(postOwnerId!=commentData.userId){
            notificationDao.addNotification(Notification(commentData.userId,postOwnerId,"commented on your Post",Date()))
        }
    }

    override suspend fun deleteComment(commentId: Int) {
        commentDao.deleteComment(commentId)
    }

    override suspend fun addCommentReaction(userId: Int, commentId: Int) {
        if(commentReactionDao.getReactions(commentId).contains(userId)){
            return
        }
        commentReactionDao.insertCommentReaction(CommentReaction(commentId,userId))
        val commentOwnerId = commentDao.getCommentOwnerId(commentId)
        Log.d("Notification Special","userId : $userId")
        Log.d("Notification Special","commentId : $commentId")
        Log.d("Notification Special","commentOwnerId : $commentOwnerId")
        if(commentOwnerId!=userId){
            notificationDao.addNotification(Notification(userId,commentOwnerId,"liked your Comment",Date()))
        }
    }

    override suspend fun removeCommentReaction(userId: Int, commentId: Int) {
        commentReactionDao.deleteReaction(userId,commentId)
        val commentOwnerId = commentDao.getCommentOwnerId(commentId)
        Log.d("Notification Special","userId : $userId")
        Log.d("Notification Special","commentId : $commentId")
        Log.d("Notification Special","commentOwnerId : $commentOwnerId")
        if(commentOwnerId!=userId){
            notificationDao.deleteNotification(userId,commentOwnerId,"liked your Comment")
        }
    }

    override suspend fun deletePost(postId: Int) {
        postDao.deletePost(postId)
    }


}