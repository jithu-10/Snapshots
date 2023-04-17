package com.example.instagramv1.adapters

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.databinding.CommentViewBinding
import com.example.instagramv1.model.CommentViewData
import com.example.instagramv1.ui.authscreen.commentscreen.CommentActivity
import com.example.instagramv1.ui.authscreen.commentscreen.CommentViewModel

class CommentsRecyclerAdapter(val commentViewModel: CommentViewModel) : RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder>() {

    var eventListener : CommentsRecyclerAdapter.onEventListener? = null

    var commentsList : MutableList<CommentViewData> = mutableListOf()
    var deleteCommentView : MutableList<View> = mutableListOf()

    var itemViewColor = "#e0f2ff"
    var normalColor = ""

    var longPressed = false

    fun removeComment(commentId : Int){
        for(comment in commentsList){
            if(comment.comment_id == commentId){
                commentsList.remove(comment)
                return
            }
        }
    }




    fun setComments(list : List<CommentViewData>){
        commentsList.clear()
        commentsList = list.toMutableList()
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val commentViewBinding : CommentViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.comment_view, parent, false)
        return CommentsViewHolder(commentViewBinding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.bind(comment)


    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class CommentsViewHolder(private val commentViewBinding: CommentViewBinding) : RecyclerView.ViewHolder(commentViewBinding.root) {


        var commentViewData : CommentViewData? = null
        var selected = false

        private val commentOnClickListener2 = OnClickListener{
            if(!selected){
                selected = true
                commentViewBinding.commentView.setBackgroundColor(Color.parseColor(itemViewColor))
                deleteCommentView.add(commentViewBinding.commentView)
                commentViewModel.selectedComments.add(commentViewData!!.comment_id)
                eventListener?.onCommentSelected()
            }
            else{
                selected = false

                commentViewBinding.commentView.setBackgroundColor(Color.parseColor(normalColor))
                deleteCommentView.remove(commentViewBinding.commentView)
                commentViewModel.selectedComments.remove(commentViewData!!.comment_id)
                eventListener?.onCommentDeselected()
            }

        }

        private val commentOnClickListener = OnClickListener{
            if(!selected){
                selected = true
                commentViewBinding.commentView.setBackgroundColor(Color.parseColor(itemViewColor))
                (commentViewBinding.root.context as CommentActivity).findViewById<ConstraintLayout>(R.id.delete_comment_bar).visibility = View.VISIBLE
                (commentViewBinding.root.context as CommentActivity).findViewById<ConstraintLayout>(R.id.edit_comment_section).visibility = View.GONE
                (commentViewBinding.root.context as CommentActivity).findViewById<ImageView>(R.id.imgViewBackBtn).visibility = View.GONE
                deleteCommentView.add(commentViewBinding.commentView)
                commentViewModel.selectedComments.add(commentViewData!!.comment_id)
                (commentViewBinding.root.context as CommentActivity).findViewById<TextView>(R.id.tvDelete).text = "${commentViewModel.selectedComments.size} selected"
            }
            else{
                selected = false
                commentViewBinding.commentView.setBackgroundColor(Color.parseColor(normalColor))
                deleteCommentView.remove(commentViewBinding.commentView)
                commentViewModel.selectedComments.remove(commentViewData!!.comment_id)
                (commentViewBinding.root.context as CommentActivity).findViewById<TextView>(R.id.tvDelete).text = "${commentViewModel.selectedComments.size} selected"
                if(commentViewModel.selectedComments.isEmpty()){
                    (commentViewBinding.root.context as CommentActivity).findViewById<ConstraintLayout>(R.id.delete_comment_bar).visibility = View.GONE
                    (commentViewBinding.root.context as CommentActivity).findViewById<ConstraintLayout>(R.id.edit_comment_section).visibility = View.VISIBLE
                    (commentViewBinding.root.context as CommentActivity).findViewById<ImageView>(R.id.imgViewBackBtn).visibility = View.VISIBLE
                }
            }

        }



        fun bind(comment : CommentViewData){
            commentViewBinding.model = comment
            commentViewData = comment

            val nightModeFlags: Int = commentViewBinding.root.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    itemViewColor = "#1a1a1a"
                    normalColor = "#000000"

                }
                Configuration.UI_MODE_NIGHT_NO ->{
                    itemViewColor = "#e0f2ff"
                    normalColor = "#FFFFFF"

                }
            }

            if(comment.comment_id == -1){
                commentViewBinding.posting.visibility = View.VISIBLE
                commentViewBinding.imgViewCommentLikeBtn.visibility = View.GONE
                commentViewBinding.commentLikeCount.visibility = View.GONE
            }
            else{
                commentViewBinding.posting.visibility = View.GONE
                commentViewBinding.imgViewCommentLikeBtn.visibility = View.VISIBLE
                commentViewBinding.commentLikeCount.visibility = View.VISIBLE
            }






            if(commentViewModel.userId == comment.comment_owner_user_id || commentViewModel.userId == commentViewModel.postOwnerUserId){
                commentViewBinding.commentView.setOnClickListener(commentOnClickListener)
            }



            if(comment.user_reacted){
                commentViewBinding.imgViewCommentLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentViewBinding.imgViewCommentLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
            }
            else{
                commentViewBinding.imgViewCommentLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentViewBinding.imgViewCommentLikeBtn.resources,R.drawable.ic_baseline_favorite_border_24,null))
            }
            commentViewBinding.imgViewCommentLikeBtn.setOnClickListener {
                if(comment.user_reacted){
                    comment.user_reacted = false
                    commentViewBinding.imgViewCommentLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentViewBinding.imgViewCommentLikeBtn.resources,R.drawable.ic_baseline_favorite_border_24,null))
                    comment.comment_reaction_count--
                    commentViewBinding.commentLikeCount.text = comment.comment_reaction_count.toString()
                    commentViewModel.removeCommentReactions.add(comment.comment_id)
                    commentViewModel.addCommentReactions.remove(comment.comment_id)
                }
                else{
                    comment.user_reacted = true
                    commentViewBinding.imgViewCommentLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentViewBinding.imgViewCommentLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                    comment.comment_reaction_count++
                    commentViewBinding.commentLikeCount.text = comment.comment_reaction_count.toString()
                    commentViewModel.addCommentReactions.add(comment.comment_id)
                    commentViewModel.removeCommentReactions.remove(comment.comment_id)
                }

            }

        }


    }

    interface onEventListener{
        fun onCommentSelected()
        fun onCommentDeselected()
    }
}