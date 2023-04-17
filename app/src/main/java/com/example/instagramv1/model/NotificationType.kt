package com.example.instagramv1.model

enum class NotificationType(val value : Int,val message : String) {
    FOLLOWS_YOU (1,"started following you"),
    UNFOLLOWS_YOU(2,"unfollows you"),
    LIKED_YOUR_POST(3,"liked your post"),
    COMMENTED_ON_YOUR_POST(4,"commented on your post"),
    SAVED_YOUR_POST(5,"saved your post"),
    LIKED_YOUR_COMMENT(6,"liked your comment")
}