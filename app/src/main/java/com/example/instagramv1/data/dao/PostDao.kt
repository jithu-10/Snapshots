package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Post
import com.example.instagramv1.model.PostViewData


@Dao
interface PostDao {

    @Query("SELECT * FROM post WHERE post_id = :postId")
    suspend fun getPost(postId : Int) : Post

    @Insert
    suspend fun insertPost(post : Post)

    @Query("DELETE FROM post WHERE post_id = :postId")
    suspend fun deletePost(postId: Int)

    @Query("UPDATE post SET description = :description WHERE post_id = :postId")
    suspend fun updateDescription(description : String , postId: Int)

    @Query("UPDATE post SET location = :location WHERE post_id = :postId")
    suspend fun updateLocation(location : String , postId: Int)

    @Query("SELECT * FROM post WHERE user_id = :userId")
    suspend fun getAllPostsByUserId(userId : Int) : List<Post>

    @Query("SELECT user_id FROM post WHERE post_id = :postId")
    suspend fun getPostOwnerId(postId: Int) : Int

    @Query("SELECT * FROM post")
    fun getAllPosts() : List<Post>

    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "  COUNT(DISTINCT reaction.user_id) AS post_reaction_count, \n" +
            "  COUNT(DISTINCT comment.comment_id) AS post_comments_count, \n" +
            "  CASE WHEN EXISTS (\n" +
            "    SELECT 1 FROM reaction WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "  ) THEN 1 ELSE 0 END AS user_reacted\n" +
            "FROM \n" +
            "  post \n" +
            "  LEFT JOIN reaction ON post.post_id = reaction.post_id\n" +
            "  LEFT JOIN comment ON post.post_id = comment.post_id\n" +
            "  INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE \n" +
            " post.post_id = :postId\n" +
            "GROUP BY \n" +
            "  post.post_id")
    fun getPost(postId: Int,userId: Int) : LiveData<PostViewData>


    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE user.private_account = 0 AND user.user_id <> :userId GROUP BY Post.post_id ORDER BY Post.post_id DESC")
    fun getAllPublicPosts(userId: Int) : LiveData<List<PostViewData>>




    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "INNER JOIN savedPost ON post.post_id = savedPost.post_id\n"+
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "LEFT JOIN Connection ON user.user_id = Connection.followed_user_id \n" +
            "WHERE savedPost.user_id = :userId AND ((user.user_id = post.user_id AND user.private_account = 0) OR (Connection.following_user_id = :userId AND Connection.followed_user_id = post.user_id) OR post.user_id = :userId) GROUP BY Post.post_id ORDER BY savedPost.id DESC")
    fun getUserSavedPosts(userId: Int) : LiveData<List<PostViewData>>

    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE user.private_account = 0 AND user.user_id <> :userId GROUP BY Post.post_id")
    fun getAllPublicPostsAscending(userId: Int) : LiveData<List<PostViewData>>

    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE user.private_account = 0 AND user.user_id <> :userId GROUP BY Post.post_id ORDER BY post_comments_count DESC")
    fun getAllPostsMostCommented(userId: Int) : LiveData<List<PostViewData>>

    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE user.private_account = 0 AND user.user_id <> :userId GROUP BY Post.post_id ORDER BY post_reaction_count DESC")
    fun getAllPostsMostLiked(userId: Int) : LiveData<List<PostViewData>>




    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "       COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "       CASE WHEN EXISTS (\n" +
            "           SELECT 1 FROM reaction \n" +
            "           WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+
            "FROM post \n" +
            "LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE user.private_account = 0 AND TRIM(LOWER(post.location)) LIKE :location GROUP BY Post.post_id")
    fun getPostByLocation(userId: Int,location : String) : LiveData<List<PostViewData>>



    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "       post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "  COUNT(DISTINCT reaction.user_id) AS post_reaction_count, \n" +
            "  COUNT(DISTINCT comment.comment_id) AS post_comments_count, \n" +
            "  CASE WHEN EXISTS (\n" +
            "    SELECT 1 FROM reaction WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "  ) THEN 1 ELSE 0 END AS user_reacted\n" +
            "FROM \n" +
            "  post \n" +
            "  LEFT JOIN reaction ON post.post_id = reaction.post_id\n" +
            "  LEFT JOIN comment ON post.post_id = comment.post_id\n" +
            "  INNER JOIN user ON post.user_id = user.user_id\n" +
            "WHERE \n" +
            "  post.user_id = :userId\n" +
            "GROUP BY \n" +
            "  post.post_id ORDER BY Post.post_id DESC")
    fun getUserPosts(userId : Int) : LiveData<List<PostViewData>>




    @Query("SELECT \n" +
            "    User.user_id AS post_owner_id, \n" +
            "    User.user_name AS post_owner_user_name, \n" +
            "    User.profile_picture AS post_owner_profile_picture, \n" +
            "    Post.post_id, \n" +
            "    Post.image AS post_image, \n" +
            "    Post.description AS post_description, \n" +
            "    Post.location AS post_location, \n" +
            "    COUNT(DISTINCT Reaction.id) AS post_reaction_count, \n" +
            "    COUNT(DISTINCT Comment.comment_id) AS post_comments_count, \n" +
            "    CASE WHEN EXISTS (\n" +
            "       SELECT 1 FROM reaction \n" +
            "        WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "       ) THEN 1 ELSE 0 END AS user_reacted\n"+

            "FROM \n" +
            "    User \n" +
            "    JOIN Post ON User.user_id = Post.user_id \n" +
            "    LEFT JOIN Reaction ON Post.post_id = Reaction.post_id \n" +
            "    LEFT JOIN Comment ON Post.post_id = Comment.post_id \n" +
            "    LEFT JOIN Connection ON User.user_id = Connection.followed_user_id \n" +
            "WHERE \n" +
            "    (User.user_id = :otherUserId AND User.private_account = 0) OR Connection.following_user_id = :userId AND Connection.followed_user_id = :otherUserId\n" +
            "GROUP BY \n" +
            "    Post.post_id, \n" +
            "    User.user_id, \n" +
            "    Reaction.user_id ORDER BY Post.post_id DESC\n")
    fun getOtherUserPosts(userId: Int,otherUserId : Int) : LiveData<List<PostViewData>>


    @Query("SELECT user.user_id AS post_owner_id, user.user_name AS post_owner_user_name, user.profile_picture AS post_owner_profile_picture, \n" +
            "                   post.post_id, post.image AS post_image, post.description AS post_description, post.location AS post_location, \n" +
            "                   COUNT(DISTINCT reaction.user_id) AS post_reaction_count, COUNT(DISTINCT comment.comment_id) AS post_comments_count,\n" +
            "                   CASE WHEN EXISTS (\n" +
            "                       SELECT 1 FROM reaction \n" +
            "                       WHERE reaction.post_id = post.post_id AND reaction.user_id = :userId\n" +
            "                   ) THEN 1 ELSE 0 END AS user_reacted\n" +
            "            FROM post \n" +
            "            LEFT JOIN reaction ON post.post_id = reaction.post_id \n" +
            "            LEFT JOIN comment ON post.post_id = comment.post_id \n" +
            "            INNER JOIN user ON post.user_id = user.user_id\n" +
            "            WHERE user.user_id = :userId OR user.user_id IN (\n" +
            "              SELECT followed_user_id FROM Connection WHERE following_user_id = :userId\n" +
            "            ) \n" +
            "            GROUP BY Post.post_id ORDER BY Post.post_id DESC\n")
    fun getFollowingPosts(userId: Int) : LiveData<List<PostViewData>>



    //"GROUP BY Post.post_id;

    // IF(COUNT(r.user_id) > 0, TRUE, FALSE) AS user_liked_post

}