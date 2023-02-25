package com.example.instagramv1.adapters

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.instagramv1.R
import com.example.instagramv1.databinding.SuggestedAccountViewBinding
import com.example.instagramv1.model.UserMiniProfileData
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import dagger.hilt.android.internal.managers.FragmentComponentManager


class SuggestedAccountsRecyclerAdapter(
    val viewModel : NotificationViewModel
) : RecyclerView.Adapter<SuggestedAccountsRecyclerAdapter.SuggestedAccountsViewHolder>() {

    private var suggestedUsers : List<UserMiniProfileData> = mutableListOf()

    fun setSuggestedUsers(list : List<UserMiniProfileData>){
        suggestedUsers = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedAccountsViewHolder {
        val suggestedAccountViewBinding : SuggestedAccountViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.suggested_account_view, parent, false)
        return SuggestedAccountsViewHolder(suggestedAccountViewBinding)
    }

    override fun onBindViewHolder(holder: SuggestedAccountsViewHolder, position: Int) {
        val suggestedUser = suggestedUsers[position]
        holder.bind(suggestedUser)
        if(holder.itemView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(position == suggestedUsers.size -1){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topDpAsPixels = (10 * scale + 0.5f)
                val bottomDpAsPixels = (10 * scale + 0.5f)
                val leftDpAsPixels = (10 * scale + 0.5f)
                val rightDpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.suggestedAccountsLayout).setPadding(leftDpAsPixels.toInt(),topDpAsPixels.toInt(),rightDpAsPixels.toInt(),bottomDpAsPixels.toInt())

            }
        }
        else{
            if(position == suggestedUsers.size-1 || position == suggestedUsers.size -2){
                val scale: Float = holder.itemView.context.resources.displayMetrics.density

                val topDpAsPixels = (10 * scale + 0.5f)
                val bottomDpAsPixels = (10 * scale + 0.5f)
                val leftDpAsPixels = (10 * scale + 0.5f)
                val rightDpAsPixels = (10 * scale + 0.5f)
                holder.itemView.findViewById<ConstraintLayout>(R.id.suggestedAccountsLayout).setPadding(leftDpAsPixels.toInt(),topDpAsPixels.toInt(),rightDpAsPixels.toInt(),bottomDpAsPixels.toInt())

            }
        }

    }

    override fun getItemCount(): Int {
        return suggestedUsers.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class SuggestedAccountsViewHolder(private val suggestedAccountViewBinding: SuggestedAccountViewBinding) : ViewHolder(suggestedAccountViewBinding.root){

        private val followBtn = suggestedAccountViewBinding.btnFollow

        fun bind(suggestedUser : UserMiniProfileData){
            suggestedAccountViewBinding.model = suggestedUser
            suggestedAccountViewBinding.btnRequested.visibility = View.GONE
            suggestedAccountViewBinding.btnFollowing.visibility = View.GONE
            suggestedAccountViewBinding.btnFollow.visibility = View.VISIBLE
            followBtn.setOnClickListener {
                followBtn.visibility = View.GONE
                if(suggestedUser.private_account){
                    suggestedAccountViewBinding.btnRequested.visibility = View.VISIBLE
                }
                else{
                    suggestedAccountViewBinding.btnFollowing.visibility = View.VISIBLE
                }
                //viewModel.followUser(suggestedUser.user_id)
                viewModel.followUsers.add(suggestedUser.user_id)
                Log.d("Follow User","From View Holder "+suggestedUser.user_id)
            }

            suggestedAccountViewBinding.btnFollowing.setOnClickListener {
                suggestedAccountViewBinding.btnFollowing.visibility = View.GONE
                suggestedAccountViewBinding.btnFollow.visibility = View.VISIBLE
                viewModel.followUsers.remove(suggestedUser.user_id)
            }

            suggestedAccountViewBinding.btnRequested.setOnClickListener {
                suggestedAccountViewBinding.btnRequested.visibility = View.GONE
                suggestedAccountViewBinding.btnFollow.visibility = View.VISIBLE
                viewModel.followUsers.remove(suggestedUser.user_id)

            }


            suggestedAccountViewBinding.suggestedUserImageCardView.setOnClickListener {
                openProfile(suggestedUser.user_id)
            }

            suggestedAccountViewBinding.tvSuggestedUserName.setOnClickListener {
                openProfile(suggestedUser.user_id)
            }


        }

        private fun openProfile(post_owner_id : Int){

            val userId = viewModel.userId

            if(userId == post_owner_id){

                (FragmentComponentManager.findActivity(suggestedAccountViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
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

                (FragmentComponentManager.findActivity(suggestedAccountViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,othersProfileFragment)
                    addToBackStack(null)
                    commit()
                }
            }

        }


    }
}