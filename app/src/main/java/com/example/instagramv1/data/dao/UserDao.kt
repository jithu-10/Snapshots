package com.example.instagramv1.data.dao

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Connection
import com.example.instagramv1.data.entities.ConnectionRequest
import com.example.instagramv1.data.entities.SearchHistory
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.model.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE user_id = :userId")
    suspend fun getUser(userId : Int) : User

    @Query("SELECT EXISTS(SELECT * FROM account WHERE user_name = :userName)")
    fun isUserNameExist(userName : String) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE email = :email)")
    fun isEmailExist(email : String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE phone = :phone)")
    fun isPhoneExist(phone : String): Boolean

    @Insert
    suspend fun insertUser(user : User) : Long

    @Query("UPDATE user SET user_name = :userName WHERE user_id = :userId")
    suspend fun updateUserName(userName : String, userId: Int)


    @Query("UPDATE user SET full_name = :fullName WHERE user_id = :userId")
    suspend fun updateFullName(fullName : String, userId: Int)

    @Query("UPDATE user SET profile_picture = :profilePicture WHERE user_id = :userId")
    suspend fun updateProfilePicture(profilePicture: Bitmap?, userId: Int)

    @Query("UPDATE user SET profile_picture = :profilePicture WHERE user_id = :userId")
    suspend fun removeProfilePicture(profilePicture: Bitmap?,userId: Int)

    @Query("UPDATE user SET private_account = :privateAccount WHERE user_id = :userId")
    suspend fun updateAccountPrivacy(privateAccount : Boolean, userId: Int)

    @Query("SELECT EXISTS(SELECT * FROM user WHERE user_id = :userId AND private_account = 1)")
    suspend fun isPrivateAccount(userId: Int) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM user WHERE user_id = :userId AND private_account = 1)")
    fun isPrivateAccountLive(userId: Int) : LiveData<Boolean>

    @Query("SELECT account.email, account.phone, user.user_name, user.full_name, user.profile_picture FROM account JOIN user ON account.user_id = user.user_id WHERE account.user_id = :userId")
    fun getUserEditableData(userId: Int) : LiveData<UserEditableData>

    @Query("SELECT \n" +
            "  u.user_id, \n" +
            "  u.user_name, \n" +
            "  u.full_name, \n" +
            "  u.profile_picture, \n" +
            "  u.private_account, \n" +
            "  COUNT(DISTINCT p.post_id) AS num_posts, \n" +
            "  COUNT(DISTINCT c1.followed_user_id) AS num_following, \n" +
            "  COUNT(DISTINCT c2.following_user_id) AS num_followers\n" +
            "FROM \n" +
            "  User u\n" +
            "  LEFT JOIN Post p ON u.user_id = p.user_id\n" +
            "  LEFT JOIN Connection c1 ON u.user_id = c1.following_user_id\n" +
            "  LEFT JOIN Connection c2 ON u.user_id = c2.followed_user_id\n" +
            "WHERE \n" +
            "  u.user_id = :userId" )
    fun getUserProfileData(userId: Int) : LiveData<UserProfileData>

    //+
    //            "GROUP BY \n" +
    //            "  u.user_id, \n" +
    //            "  u.user_name, \n" +
    //            "  u.full_name, \n" +
    //            "  u.profile_picture;"


//    @Query("SELECT u.user_id , u.user_name, u.profile_picture, u.private_account\n" +
//            "FROM user u\n" +
//            "LEFT JOIN connection c ON u.user_id = c.followed_user_id\n" +
//            "WHERE (c.following_user_id IS NULL OR c.following_user_id <> :userId) \n" +
//            "AND u.user_id <> :userId")

    @Query("SELECT u.user_id, u.user_name, u.full_name, u.profile_picture, u.private_account\n" +
            "FROM user u\n" +
            "WHERE u.user_id <> :userId\n" +
            "AND u.user_id NOT IN (\n" +
            "  SELECT c.followed_user_id FROM connection c WHERE c.following_user_id = :userId\n" +
            ")\n" +
            "AND u.user_id NOT IN (\n" +
            "  SELECT cr.followed_user_id FROM connectionRequest cr WHERE cr.following_user_id = :userId\n" +
            ")\n")
    fun getSuggestedUsers(userId: Int) : LiveData<List<UserMiniProfileData>>

    @Query("SELECT u.user_id, u.user_name, u.full_name, u.profile_picture, u.private_account\n" +
            "FROM user u WHERE TRIM(LOWER(u.user_name)) LIKE :name  OR TRIM(LOWER(u.full_name)) LIKE :name")
    fun getUsers(name : String) : LiveData<List<UserMiniProfileData>>

    @Query("SELECT user_id, user_name, full_name, profile_picture, private_account\n" +
            "FROM user\n" +
            "WHERE user_id IN (\n" +
            "  SELECT following_user_id\n" +
            "  FROM connectionRequest\n" +
            "  WHERE followed_user_id = :userId\n" +
            ");")
    fun getRequestedUsers(userId: Int) : LiveData<List<UserMiniProfileData>>

    @Query("SELECT * FROM connectionRequest WHERE followed_user_id = :userId")
    suspend fun getRequestedUsersList(userId: Int) : List<ConnectionRequest>

    @Insert
    suspend fun addConnection(connection : Connection)

    @Query("SELECT user_id, user_name, full_name, profile_picture, private_account\n" +
            "FROM user \n" +
            "WHERE user_id IN (\n" +
            "    SELECT following_user_id\n" +
            "    FROM connection\n" +
            "    WHERE followed_user_id = :userId\n" +
            ")")
    fun getUserFollowers(userId : Int) : LiveData<List<UserMiniProfileData>>


    @Query("SELECT user_id, user_name, full_name, profile_picture, private_account\n" +
            "FROM user \n" +
            "WHERE user_id IN (\n" +
            "    SELECT followed_user_id\n" +
            "    FROM connection\n" +
            "    WHERE following_user_id = :userId\n" +
            ")")
    fun getUserFollowings(userId: Int) : LiveData<List<UserMiniProfileData>>

    @Query("SELECT profile_picture FROM user WHERE user_id = :userId")
    suspend fun getUserProfilePicture(userId: Int) : Bitmap?

    @Query("SELECT user_name FROM user WHERE user_id = :userId")
    suspend fun getUserName(userId: Int) : String

    @Query("SELECT COUNT(*) FROM connection WHERE followed_user_id = :userId")
    fun getNoOfFollowers(userId: Int) : LiveData<Int>

    @Query("SELECT COUNT(*) FROM connection WHERE following_user_id = :userId")
    fun getNoOfFollowings(userId: Int) : LiveData<Int>

    @Query("SELECT User.user_name AS broadcaster_user_name, User.profile_picture AS broadcaster_profile_picture, \n" +
            "       Notification.broadcaster_user_id, Notification.message\n" +
            "FROM User\n" +
            "JOIN Notification ON User.user_id = Notification.broadcaster_user_id\n" +
            "WHERE Notification.receiver_user_id = :userId ORDER BY Notification.notification_id DESC")
    fun getNotifications(userId: Int) : LiveData<List<NotificationData>>

    @Query("SELECT Notification.notification_id AS notification_id , User.user_name AS broadcaster_user_name, User.profile_picture AS broadcaster_profile_picture, \n" +
            "       Notification.broadcaster_user_id AS broadcaster_user_id, Notification.notification_type AS notification_type, Notification.content_id AS content_id \n" +
            "FROM User\n" +
            "JOIN notificationUpdate Notification  ON User.user_id = Notification.broadcaster_user_id\n" +
            "WHERE Notification.receiver_user_id = :userId ORDER BY Notification.notification_id DESC")
    fun getNotificationUpdates(userId: Int) : LiveData<List<NotificationViewData>>

    @Insert
    suspend fun addSearchHistory(searchHistory: SearchHistory)

    @Query("DELETE FROM searchHistory WHERE user_id = :userId AND history_text = :historyText ")
    suspend fun deleteSearchHistory(userId: Int,historyText : String)

    @Query("DELETE FROM searchHistory WHERE user_id = :userId")
    suspend fun deleteAllSearchHistory(userId: Int)

    @Query("SELECT EXISTS(SELECT * FROM searchHistory WHERE user_id = :userId AND history_text = :historyText)")
    suspend fun containsSearchText(historyText: String,userId: Int) : Boolean

    @Query("SELECT history_text FROM searchHistory where user_id = :userId ORDER BY id DESC LIMIT 20 ")
    fun getSearchHistory(userId : Int) : LiveData<List<String>>

}