package com.example.instagramv1.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.instagramv1.data.AppDatabase
import com.example.instagramv1.data.repository.*
import com.example.instagramv1.data.repository.impl.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app : Application) : AppDatabase{
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRegisterRepository(db : AppDatabase): RegisterRepository{
         return RegisterRepositoryImpl(db.accountDao(),db.userDao())
    }

    @Provides
    @Singleton
    fun provideLoginRepository(db : AppDatabase) : LoginRepository{
        return LoginRepositoryImpl(db.accountDao())
    }

    @Provides
    @Singleton
    fun provideInjectDataRepository(db : AppDatabase) : InjectDataRepository{
        return InjectDataRepositoryImpl(db.accountDao(),db.userDao(),db.postDao())
    }

    @Provides
    @Singleton
    fun provideUserRepository(db : AppDatabase) : UserRepository{
        return UserRepositoryImpl(db.userDao(),db.accountDao(),db.connectionDao(),db.notificationDao())
    }

    @Provides
    @Singleton
    fun providePostRepository(db : AppDatabase) : PostRepository{
        return PostRepositoryImpl(db.postDao(),db.reactionDao(),db.commentDao(),db.commentReactionDao(),db.savedPostDao(),db.notificationDao())
    }

    @Provides
    @Singleton
    fun provideSampleRepository(db : AppDatabase) : SampleRepository{
        return SampleRepositoryImpl(db.sampleDao(),db.accountDao())
    }

    @Provides
    @Singleton
    fun provideUserId(app : Application) : Int{
        val sharedPref = app.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE)
        return sharedPref.getInt("USER_ID",-1)
    }
}