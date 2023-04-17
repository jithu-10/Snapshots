package com.example.instagramv1.adapters

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FollowRequestViewBinding
import com.example.instagramv1.model.UserMiniProfileData
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import dagger.hilt.android.internal.managers.FragmentComponentManager

class FollowRequestsRecyclerAdapter(
    val viewModel : NotificationViewModel
) : RecyclerView.Adapter<FollowRequestsRecyclerAdapter.FollowRequestsViewHolder>() {

    private var requestedUsers : List<UserMiniProfileData> = mutableListOf()

    fun setRequestedUsers(list : List<UserMiniProfileData>){
        requestedUsers = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowRequestsViewHolder {
        val followRequestViewBinding : FollowRequestViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.follow_request_view, parent, false)
        return FollowRequestsViewHolder(followRequestViewBinding)
    }

    override fun onBindViewHolder(holder: FollowRequestsViewHolder, position: Int) {
        val requestedUser = requestedUsers[position]
        holder.bind(requestedUser)
        if(holder.itemView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(position == requestedUsers.size -1){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topdpAsPixels = (10 * scale + 0.5f)
                val bottomdpAsPixels = (10 * scale + 0.5f)
                val leftdpAsPixels = (10 * scale + 0.5f)
                val rightdpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.followRequestsLayout).setPadding(leftdpAsPixels.toInt(),topdpAsPixels.toInt(),rightdpAsPixels.toInt(),bottomdpAsPixels.toInt())

            }
        }
        else{
            if(position == requestedUsers.size-1 || position == requestedUsers.size -2){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topdpAsPixels = (10 * scale + 0.5f)
                val bottomdpAsPixels = (10 * scale + 0.5f)
                val leftdpAsPixels = (10 * scale + 0.5f)
                val rightdpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.followRequestsLayout).setPadding(leftdpAsPixels.toInt(),topdpAsPixels.toInt(),rightdpAsPixels.toInt(),bottomdpAsPixels.toInt())

            }
        }
    }

    override fun getItemCount(): Int {
        return requestedUsers.size
    }


    inner class FollowRequestsViewHolder(private val followRequestViewBinding: FollowRequestViewBinding) : RecyclerView.ViewHolder(followRequestViewBinding.root){

        private val confirmBtn = followRequestViewBinding.btnConfirm
        private val rejectBtn = followRequestViewBinding.btnDelete
        fun bind(requestedUser : UserMiniProfileData){
            followRequestViewBinding.model = requestedUser

            confirmBtn.setOnClickListener {
                viewModel.acceptFollowRequest(requestedUser.user_id)
            }

            rejectBtn.setOnClickListener {
                viewModel.deleteFollowRequest(requestedUser.user_id)
            }


            followRequestViewBinding.followRequestsLayout.setOnClickListener {
                openProfile(requestedUser.user_id)
            }


            followRequestViewBinding.requestedUserImageCardView.setOnClickListener {
                openProfile(requestedUser.user_id)
            }

            followRequestViewBinding.tvRequestedUserName.setOnClickListener {
                openProfile(requestedUser.user_id)
            }


        }

        private fun openProfile(userId : Int){
            val othersProfileFragment = OthersProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt("OTHER_USER_ID",userId)
                }
            }
            //othersProfileFragment.arguments?.putInt("OTHER_USER_ID",post_owner_id)

            (FragmentComponentManager.findActivity(followRequestViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_page,othersProfileFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}