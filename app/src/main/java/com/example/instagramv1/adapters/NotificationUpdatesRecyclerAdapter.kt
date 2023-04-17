package com.example.instagramv1.adapters

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.instagramv1.R
import com.example.instagramv1.data.entities.Notification
import com.example.instagramv1.data.entities.NotificationUpdate
import com.example.instagramv1.databinding.NotificationUpdatesViewBinding
import com.example.instagramv1.databinding.NotificationViewBinding
import com.example.instagramv1.model.NotificationData
import com.example.instagramv1.model.NotificationType
import com.example.instagramv1.model.NotificationViewData
import com.example.instagramv1.ui.authscreen.commentscreen.PostWithCommentActivity
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationUpdatesRecyclerAdapter(
    val viewModel : NotificationViewModel
) : RecyclerView.Adapter<NotificationUpdatesRecyclerAdapter.NotificationUpdatesViewHolder>() {

    private var notifications : List<NotificationViewData> = mutableListOf()

    fun setNotifications(list : List<NotificationViewData>){
        notifications = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationUpdatesViewHolder {
        val notificationUpdatesViewBinding : NotificationUpdatesViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.notification_updates_view,parent,false)
        return NotificationUpdatesViewHolder(notificationUpdatesViewBinding)
    }

    override fun onBindViewHolder(holder: NotificationUpdatesViewHolder, position: Int) {

        val notification = notifications[position]
        holder.bind(notification)
        if(holder.itemView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(position == notifications.size -1){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topDpAsPixels = (10 * scale + 0.5f)
                val bottomDpAsPixels = (10 * scale + 0.5f)
                val leftDpAsPixels = (10 * scale + 0.5f)
                val rightDpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.notificationViewLayout).setPadding(leftDpAsPixels.toInt(),topDpAsPixels.toInt(),rightDpAsPixels.toInt(),bottomDpAsPixels.toInt())

            }
        }
        else{
            if(position == notifications.size-1 || position == notifications.size -2){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topDpAsPixels = (10 * scale + 0.5f)
                val bottomDpAsPixels = (10 * scale + 0.5f)
                val leftDpAsPixels = (10 * scale + 0.5f)
                val rightDpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.notificationViewLayout).setPadding(leftDpAsPixels.toInt(),topDpAsPixels.toInt(),rightDpAsPixels.toInt(),bottomDpAsPixels.toInt())

            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class NotificationUpdatesViewHolder(private val notificationUpdatesViewBinding: NotificationUpdatesViewBinding) : ViewHolder(notificationUpdatesViewBinding.root){

        fun bind( notificationViewData: NotificationViewData ){
            notificationUpdatesViewBinding.model = notificationViewData
            val notificationText = notificationViewData.broadcaster_user_name+" "+notificationViewData.notification_type.message
            val message = SpannableStringBuilder(notificationText)
            message.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                notificationViewData.broadcaster_user_name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            notificationUpdatesViewBinding.tvNotification.text = message

            val notificationViewType = notificationViewData.notification_type
            if(notificationViewType == NotificationType.FOLLOWS_YOU || notificationViewType == NotificationType.UNFOLLOWS_YOU){
                notificationUpdatesViewBinding.contentCardView.visibility = View.GONE
            }
            else{
                notificationUpdatesViewBinding.contentCardView.visibility = View.VISIBLE
            }
            var postId = -1
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                notificationUpdatesViewBinding.contentImageView.setImageBitmap(viewModel.getContentImage(notificationViewData.content_id,notificationViewType))
                postId = viewModel.getPostId(notificationViewData.content_id,notificationViewType)
            }

            notificationUpdatesViewBinding.notificationViewLayout.setOnClickListener {
                openProfile(notificationViewData.broadcaster_user_id)
            }
//            notificationUpdatesViewBinding.contentCardView.setOnClickListener {
//                val intent = Intent(it.context,PostWithCommentActivity::class.java).apply {
//                    putExtra("POST_ID",postId)
//                }
//                it.context.startActivity(intent)
//            }




        }

        private fun openProfile(post_owner_id : Int){

            val userId = viewModel.userId

            if(userId == post_owner_id){

                (FragmentComponentManager.findActivity(notificationUpdatesViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page, ProfileFragment())
                    addToBackStack(null)
                    commit()
                }
            }

            else{
                val othersProfileFragment = OthersProfileFragment().apply {
                    arguments = Bundle().apply {
                        putInt("OTHER_USER_ID",post_owner_id)
                    }
                }
                //othersProfileFragment.arguments?.putInt("OTHER_USER_ID",post_owner_id)

                (FragmentComponentManager.findActivity(notificationUpdatesViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,othersProfileFragment)
                    addToBackStack(null)
                    commit()
                }
            }

        }

    }
}