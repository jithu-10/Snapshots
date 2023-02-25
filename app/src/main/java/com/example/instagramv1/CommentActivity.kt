package com.example.instagramv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.adapters.CommentsRecyclerAdapter

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = object : LinearLayoutManager(this){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        commentsRecyclerView.adapter = CommentsRecyclerAdapter()

        findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            finish()
        }
    }
}