package com.example.instagramv1.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.CommentActivity
import com.example.instagramv1.R


class PostsRecyclerAdapter : RecyclerView.Adapter<PostsRecyclerAdapter.PostsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_view,parent,false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class PostsViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val likeBtn = view.findViewById<ImageView>(R.id.imgViewLikeBtn).apply {

        }

        val saveBtn = view.findViewById<ImageView>(R.id.imgViewSaveBtn).apply {

        }

        val commentBtn = view.findViewById<ImageView>(R.id.imgViewCommentBtn).apply {
            setOnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                context.startActivity(intent)
            }

        }

        val viewAllCommentsBtn = view.findViewById<TextView>(R.id.tvViewComments).apply {
            setOnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                context.startActivity(intent)
            }
        }

        val descriptionTextView = view.findViewById<TextView>(R.id.post_description)


    }
}