package com.example.instagramv1.data.repository.impl

import com.example.instagramv1.data.dao.AccountDao
import com.example.instagramv1.data.dao.UserDao
import com.example.instagramv1.data.entities.User
import com.example.instagramv1.data.repository.LoginRepository

class LoginRepositoryImpl(
    private val accountDao : AccountDao
) : LoginRepository {
    override suspend fun isEmailExist(email: String): Boolean {
        return accountDao.isEmailExist(email)
    }

    override suspend fun isPhoneExist(phone: String): Boolean {
        return accountDao.isPhoneExist(phone)
    }

    override suspend fun isUserNameExist(userName: String): Boolean {
        return accountDao.isUserNameExist(userName)
    }

    override suspend fun login(userInfo: String, password: String) : Int {
        if(accountDao.isUserMailPasswordMatched(userInfo,password)) {
            return accountDao.getUserByEmail(userInfo, password)
        }
        else if (accountDao.isUserNamePasswordMatched(userInfo,password)) {
            return accountDao.getUserByUserName(userInfo, password)
        }
        else if(accountDao.isUserPhonePasswordMatched(userInfo,password)){
            return accountDao.getUserByPhone(userInfo, password)
        }
        return -1


    }

    override suspend fun loginViaOtp(userInfo: String) : Int{
        if(accountDao.isEmailExist(userInfo)){
            return accountDao.getUserByEmail(userInfo)
        }
        else if(accountDao.isPhoneExist(userInfo)){
            return accountDao.getUserByPhone(userInfo)
        }
        return -1
    }

}