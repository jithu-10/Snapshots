package com.example.instagramv1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instagramv1.data.entities.Account

@Dao
interface AccountDao {

    @Query("SELECT user_id FROM account WHERE email = :email AND password = :password")
    suspend fun getUserByEmail(email : String,password : String): Int

    @Query("SELECT user_id FROM account WHERE email = :email")
    suspend fun getUserByEmail(email : String) : Int

    @Query("SELECT EXISTS(SELECT * FROM account WHERE email = :email AND password = :password)")
    suspend fun isUserMailPasswordMatched(email : String, password: String) : Boolean

    @Query("SELECT user_id FROM account WHERE phone = :phone AND password = :password")
    suspend fun getUserByPhone(phone : String,password : String): Int

    @Query("SELECT user_id FROM account WHERE phone = :phone")
    suspend fun getUserByPhone(phone : String): Int

    @Query("SELECT EXISTS(SELECT * FROM account WHERE phone = :phone AND password = :password)")
    suspend fun isUserPhonePasswordMatched(phone : String, password: String) : Boolean

    @Query("SELECT user_id FROM account WHERE user_name = :userName AND password = :password")
    suspend fun getUserByUserName(userName : String, password: String) : Int

    @Query("SELECT EXISTS(SELECT * FROM account WHERE user_name = :userName AND password = :password)")
    suspend fun isUserNamePasswordMatched(userName : String, password: String) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE user_name = :userName)")
    suspend fun isUserNameExist(userName : String) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE email = :email)")
    suspend fun isEmailExist(email : String): Boolean

    @Query("SELECT EXISTS(SELECT * FROM account WHERE phone = :phone)")
    suspend fun isPhoneExist(phone : String): Boolean

    @Insert
    suspend fun insertAccount(account: Account)

    @Query("SELECT phone FROM account WHERE user_id = :userId")
    fun getUserPhone(userId : Int) : LiveData<String>

    @Query("SELECT user_name FROM account WHERE user_id = :userId")
    fun getUserName(userId: Int) : LiveData<String>

    @Query("SELECT email FROM account WHERE user_id = :userId")
    fun getUserEmail(userId : Int) : LiveData<String>

    @Query("UPDATE account SET phone = :phone WHERE user_id = :userId")
    suspend fun updatePhone(phone : String, userId: Int)

    @Query("UPDATE account SET email = :email WHERE user_id = :userId")
    suspend fun updateEmail(email : String, userId: Int)

    @Query("UPDATE account SET user_name = :userName WHERE user_id = :userId")
    suspend fun updateUserName(userName : String, userId: Int)
}