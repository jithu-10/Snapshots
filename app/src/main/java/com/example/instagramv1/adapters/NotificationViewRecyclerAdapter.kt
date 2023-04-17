package com.example.instagramv1.adapters

import android.content.res.Configuration
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.data.entities.Notification
import com.example.instagramv1.databinding.NotificationViewBinding
import com.example.instagramv1.model.NotificationData
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel


class NotificationViewRecyclerAdapter(
    val viewModel : NotificationViewModel
) : RecyclerView.Adapter<NotificationViewRecyclerAdapter.NotificationViewViewHolder>() {

    private var notifications : List<NotificationData> = mutableListOf()

    fun setNotifications(list : List<NotificationData>){
        notifications = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewViewHolder {
        val notificationViewBinding : NotificationViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.notification_view, parent, false)
        return NotificationViewViewHolder(notificationViewBinding)
    }

    override fun onBindViewHolder(holder: NotificationViewViewHolder, position: Int) {
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

    inner class NotificationViewViewHolder(private val notificationViewBinding : NotificationViewBinding) : RecyclerView.ViewHolder(notificationViewBinding.root){

        fun bind(notificationData : NotificationData){
            notificationViewBinding.model = notificationData
            val notificationText = notificationData.broadcaster_user_name+" "+notificationData.message
            val message = SpannableStringBuilder(notificationText)
            message.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                notificationData.broadcaster_user_name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            notificationViewBinding.tvNotification.text = message


        }
    }
}