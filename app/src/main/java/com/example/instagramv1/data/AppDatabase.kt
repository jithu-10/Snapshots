package com.example.instagramv1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.instagramv1.data.dao.*
import com.example.instagramv1.data.dataconvertors.BitmapConvertor
import com.example.instagramv1.data.dataconvertors.DateConvertor
import com.example.instagramv1.data.entities.*

@Database(
    entities = [
        Account::class,
        User::class,
        Post::class,
        Connection::class,
        ConnectionRequest::class,
        Reaction::class,
        Comment::class,
        CommentReaction::class,
        SavedPost::class,
        Notification::class,
        SearchHistory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BitmapConvertor::class, DateConvertor::class)
abstract class AppDatabase : RoomDatabase(){


    abstract fun accountDao() : AccountDao
    abstract fun userDao() : UserDao
    abstract fun connectionDao() : ConnectionDao
    abstract fun postDao() : PostDao
    abstract fun commentDao() : CommentDao
    abstract fun reactionDao() : ReactionDao
    abstract fun commentReactionDao(): CommentReactionDao
    abstract fun sampleDao(): SampleDao
    abstract fun savedPostDao(): SavedPostDao
    abstract fun notificationDao() : NotificationDao


}