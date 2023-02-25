package com.example.instagramv1.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.databinding.OtherProfileSimpleViewBinding
import com.example.instagramv1.model.SimpleProfileType
import com.example.instagramv1.model.UserConnectionWithOtherUser
import com.example.instagramv1.model.UserMiniProfileData
import com.example.instagramv1.model.UserProfileData
import com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen.ConnectionViewModel
import com.example.instagramv1.ui.mainscreen.notificationscreen.NotificationViewModel
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen.RemoveFollowerDialogFragment
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProfileSimpleRecyclerAdapter(
    val viewModel : ConnectionViewModel,
    val type : SimpleProfileType
) : RecyclerView.Adapter<ProfileSimpleRecyclerAdapter.ProfileViewHolder>() {

    private var users : List<UserMiniProfileData> = mutableListOf()
    val profileFragment = ProfileFragment()

    fun setUsers(list : List<UserMiniProfileData>){
        users = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val otherProfileSimpleViewBinding : OtherProfileSimpleViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.other_profile_simple_view, parent, false)
        return ProfileViewHolder(otherProfileSimpleViewBinding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }


    inner class ProfileViewHolder(private val otherProfileSimpleViewBinding: OtherProfileSimpleViewBinding) : RecyclerView.ViewHolder(otherProfileSimpleViewBinding.root) {

        fun bind(user : UserMiniProfileData){
            otherProfileSimpleViewBinding.model = user
            if(type == SimpleProfileType.FOLLOWING_VIEW){
                bindFollowing(user)
            }
            else if(type == SimpleProfileType.FOLLOWERS_VIEW){
                bindFollowers(user)
            }
            else if(type == SimpleProfileType.OTHERS_FOLLOWERS_FOLLOWING_VIEW){
                bindOtherFollowersFollowing(user)
            }
            else if(type == SimpleProfileType.SEARCH_VIEW){
                bindSearchUsers(user)
            }

            otherProfileSimpleViewBinding.suggestedUserImageCardView.setOnClickListener {
                openProfile(user.user_id)
            }

            otherProfileSimpleViewBinding.tvSuggestedUserName.setOnClickListener {
                openProfile(user.user_id)
            }

            otherProfileSimpleViewBinding.tvRequested.setOnClickListener {
                openProfile(user.user_id)
            }


            otherProfileSimpleViewBinding.btnFollowing.setOnClickListener {
                //viewModel.unfollowUser(user.user_id)
                viewModel.unfollowUsers.add(user.user_id)
                viewModel.followUsers.remove(user.user_id)
                otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
                otherProfileSimpleViewBinding.btnFollow.visibility = View.VISIBLE
            }

            otherProfileSimpleViewBinding.btnRemove.setOnClickListener {
                val removeFollowerDialogFragment = RemoveFollowerDialogFragment().apply {
                    arguments = Bundle().apply {
                        putInt("OTHER_USER_ID",user.user_id)
                    }
                }
                removeFollowerDialogFragment.show((FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager,"removedialogfragment")
                //viewModel.removeFollower(user.user_id)
            }

            otherProfileSimpleViewBinding.btnFollow.setOnClickListener {
                //viewModel.followUser(user.user_id)
                viewModel.followUsers.add(user.user_id)

                otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
                if(user.private_account){
                    otherProfileSimpleViewBinding.btnRequested.visibility = View.VISIBLE
                }
                else{
                    otherProfileSimpleViewBinding.btnFollowing.visibility = View.VISIBLE
                    viewModel.unfollowUsers.remove(user.user_id)
                }

            }

            otherProfileSimpleViewBinding.btnRequested.setOnClickListener {
                viewModel.followUsers.remove(user.user_id)
                otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
                otherProfileSimpleViewBinding.btnFollow.visibility = View.VISIBLE

                //viewModel.cancelRequestUser(user.user_id)
            }

        }

        private fun openProfile(post_owner_id : Int){

            val userId = viewModel.userId

            if(userId == post_owner_id){

                (FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,profileFragment,"PROFILE_FRAGMENT")
                    addToBackStack("PROFILE_FRAGMENT")
                    commit()
                }



//                if(isFragmentInBackstack((FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager,"PROFILE_FRAGMENT2")){
//
////                    (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
////                        replace(R.id.frame_page,profileFragment,"PROFILE_FRAGMENT")
////                        //addToBackStack("PROFILE_FRAGMENT")
////                        commit()
////                    }
//
//                    val frg = (FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager.findFragmentByTag("PROFILE_FRAGMENT2")
//                    val ft: FragmentTransaction = (FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction()
//                    ft.detach(frg!!)
//                    ft.attach(frg!!)
//                    ft.commit()
//                }
//                else{
//
//                }
            }

            else{
                val othersProfileFragment = OthersProfileFragment().apply {
                    arguments = Bundle().apply {
                        putInt("OTHER_USER_ID",post_owner_id)
                    }
                }
                //othersProfileFragment.arguments?.putInt("OTHER_USER_ID",post_owner_id)

                (FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,othersProfileFragment)
                    addToBackStack(null)
                    commit()
                }
            }

        }

        private fun isFragmentInBackstack(fragmentManager: FragmentManager, fragmentTagName: String): Boolean {
            for (entry in 0 until fragmentManager.backStackEntryCount) {
                if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).name) {
                    return true
                }
            }
            return false
        }


        private fun bindFollowing(user : UserMiniProfileData){
            otherProfileSimpleViewBinding.btnFollowing.visibility = View.VISIBLE
            otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
            otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
            otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE


        }

        private fun bindFollowers(user : UserMiniProfileData){
            otherProfileSimpleViewBinding.btnRemove.visibility = View.VISIBLE
            otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
            otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
            otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
        }

        private fun bindOtherFollowersFollowing(user : UserMiniProfileData){
            if(user.user_id == viewModel.userId){
                otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
                otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
                otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
                otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
                return
            }
            viewModel.getUserConnectionWithOtherUser(user.user_id).observe(FragmentComponentManager.findActivity(otherProfileSimpleViewBinding.root.context) as AppCompatActivity){
                when (it) {
                    UserConnectionWithOtherUser.FOLLOWING -> {
                        otherProfileSimpleViewBinding.btnFollowing.visibility = View.VISIBLE
                        otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
                    }
                    UserConnectionWithOtherUser.REQUESTED_TO_FOLLOW -> {
                        otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnRequested.visibility = View.VISIBLE
                        otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
                    }
                    else -> {
                        otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
                        otherProfileSimpleViewBinding.btnFollow.visibility = View.VISIBLE
                        otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
                    }
                }
            }
        }

        private fun bindSearchUsers(user : UserMiniProfileData){
            otherProfileSimpleViewBinding.btnFollowing.visibility = View.GONE
            otherProfileSimpleViewBinding.btnRequested.visibility = View.GONE
            otherProfileSimpleViewBinding.btnFollow.visibility = View.GONE
            otherProfileSimpleViewBinding.btnRemove.visibility = View.GONE
        }
    }
}