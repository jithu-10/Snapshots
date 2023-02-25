package com.example.instagramv1.data.repository.impl

import com.example.instagramv1.data.dao.AccountDao
import com.example.instagramv1.data.dao.UserDao
import com.example.instagramv1.data.entities.Account
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.data.repository.RegisterRepository
import com.example.instagramv1.model.UserData
import kotlinx.coroutines.delay
import java.util.*

class RegisterRepositoryImpl(
    private val accountDao: AccountDao,
    private val userDao: UserDao
)
    : RegisterRepository{
    override suspend fun isUserNameAlreadyExist(userName: String): Boolean {
        return accountDao.isUserNameExist(userName)
    }

    override suspend fun isEmailAlreadyExist(email: String): Boolean {
        return accountDao.isEmailExist(email)
    }

    override suspend fun isPhoneAlreadyExist(phone: String): Boolean {
        return accountDao.isPhoneExist(phone)
    }

    override suspend fun registerUser(userData: UserData) : Boolean{
        val user = User(userData.userName,userData.fullName,null,false)
        val userID =userDao.insertUser(user)
        val account =
            Account(userData.email,
                userData.phone,
                userData.password,
                Date(),
                user.userName,
                userID.toInt())

        accountDao.insertAccount(account)
        return true
    }
}