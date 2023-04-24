package com.example.instagramv1.ui.mainscreen.othersprofilescreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.databinding.FragmentOthersProfileBinding
import com.example.instagramv1.model.UserConnectionWithOtherUser
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.explorescreen.ExplorePostsViewModel
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class OthersProfileFragment : Fragment(),PostsRecyclerAdapter.EventListener {

    private val viewModel by activityViewModels<ExplorePostsViewModel>()
    private val otherProfileViewModel by viewModels<OthersProfileViewModel>()
    private val postViewModel by activityViewModels<PostViewModel>()
    private lateinit var othersProfileBinding: FragmentOthersProfileBinding
    private var recyclerView : RecyclerView? = null
    private var menuItemId : Int = -1
    private var postRecyclerAdapter : PostsRecyclerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        menuItemId = bottomNavigationView.selectedItemId
        othersProfileBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_others_profile, container, false)
        othersProfileBinding.otherUserProfileViewModel = otherProfileViewModel
        return othersProfileBinding.root
    }

    override fun onResume() {
        super.onResume()
        othersProfileBinding.invalidateAll()
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(menuItemId)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        otherProfileViewModel.userId = userId
        viewModel.userId = userId
        postViewModel.userId = userId

        val otherUserId = arguments?.getInt("OTHER_USER_ID")
        otherProfileViewModel.otherUserId = otherUserId!!

        fetchData()

        setUpListeners()




        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<ConstraintLayout>(R.id.followersCountLayout).setOnClickListener {
            openConnectionsFragment(ProfileFragment.FOLLOWERS_COUNT)
        }
        view.findViewById<ConstraintLayout>(R.id.followingCountLayout).setOnClickListener {
            openConnectionsFragment(ProfileFragment.FOLLOWING_COUNT)
        }
        view.findViewById<ConstraintLayout>(R.id.postCountLayout).setOnClickListener {
            view.findViewById<AppBarLayout>(R.id.appBarLayout).setExpanded(false,true)
        }
    }

    private fun openConnectionsFragment(focusedType : Int){
        if(otherProfileViewModel.userConnectionWithOtherUser != UserConnectionWithOtherUser.FOLLOWING){
            return
        }
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_page, OthersProfileConnectionsFragment().apply {
                arguments = Bundle().apply {
                    putInt("focusedType",focusedType)
                    putInt("OTHER_USER_ID",otherProfileViewModel.otherUserId)}
            })
            addToBackStack(null)
            commit()
        }
    }

    private fun fetchData(){

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_profile)
        postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,this)
        postRecyclerAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView?.adapter = postRecyclerAdapter
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())

        var loaded = false

        lifecycleScope.launch {
                val userData = otherProfileViewModel.getOtherUserProfileData()
                userData.observe(viewLifecycleOwner) {

                    otherProfileViewModel.profilePicture = userData.value?.profile_picture
                    otherProfileViewModel.fullName = userData.value?.full_name
                    otherProfileViewModel.userName = userData.value?.user_name
                    otherProfileViewModel.noOfPosts = userData.value?.num_posts
                    otherProfileViewModel.noOfFollowers = userData.value?.num_followers
                    otherProfileViewModel.noOfFollowing = userData.value?.num_following
                    otherProfileViewModel.privateAccount = userData.value?.private_account

                    othersProfileBinding.invalidateAll()

                    if(loaded){
                        view?.findViewById<FrameLayout>(R.id.profileLoading)?.visibility = View.GONE
                        view?.findViewById<CoordinatorLayout>(R.id.coordinator)?.visibility = View.VISIBLE
                    }
                    loaded = true
                }


        }




        lifecycleScope.launch {
            val userConnectionData = otherProfileViewModel.getUserConnectionWithOtherUser()
            userConnectionData.observe(viewLifecycleOwner){
                otherProfileViewModel.userConnectionWithOtherUser = it
                if(it==UserConnectionWithOtherUser.FOLLOWING){
                    othersProfileBinding.btnFollow.visibility = View.GONE
                    othersProfileBinding.btnRequested.visibility = View.GONE
                    othersProfileBinding.btnFollowing.visibility = View.VISIBLE

                }
                else if(it == UserConnectionWithOtherUser.NOT_FOLLOWED){
                    othersProfileBinding.btnFollow.visibility = View.VISIBLE
                    othersProfileBinding.btnRequested.visibility = View.GONE
                    othersProfileBinding.btnFollowing.visibility = View.GONE
                }

                else if(it == UserConnectionWithOtherUser.REQUESTED_TO_FOLLOW){
                    othersProfileBinding.btnFollow.visibility = View.GONE
                    othersProfileBinding.btnRequested.visibility = View.VISIBLE
                    othersProfileBinding.btnFollowing.visibility = View.GONE
                }

                if(loaded){
                    view?.findViewById<FrameLayout>(R.id.profileLoading)?.visibility = View.GONE
                    view?.findViewById<CoordinatorLayout>(R.id.coordinator)?.visibility = View.VISIBLE
                }

                loaded = true


            }

            val isPrivateAccount = otherProfileViewModel.isPrivateAccount()


            viewModel.getOtherUserPosts(otherProfileViewModel.otherUserId).observe(requireActivity()){
                Log.d("Special",it.size.toString())
                postRecyclerAdapter?.setPosts(it,false)
                view?.findViewById<FrameLayout>(R.id.pbLoading)?.visibility = View.GONE
                if(it.isEmpty()){
                    recyclerView?.visibility = View.GONE


                    if(isPrivateAccount){
                        if(otherProfileViewModel.userConnectionWithOtherUser != UserConnectionWithOtherUser.FOLLOWING){
                            view?.findViewById<View>(R.id.tvPrivateAccountFound)?.visibility = View.VISIBLE
                        }
                    }
                    else{
                        view?.findViewById<View>(R.id.tvNoResultsFound)?.visibility = View.VISIBLE
                    }

                }
                else{
                    recyclerView?.visibility = View.VISIBLE
                    view?.findViewById<View>(R.id.tvNoResultsFound)?.visibility = View.GONE
                    view?.findViewById<View>(R.id.tvPrivateAccountFound)?.visibility = View.GONE


                }

            }
        }






    }

    private fun setUpListeners(){
        othersProfileBinding.btnFollow.setOnClickListener {
            if(otherProfileViewModel.privateAccount!!){
                othersProfileBinding.btnFollow.visibility = View.GONE
                othersProfileBinding.btnRequested.visibility = View.VISIBLE
                othersProfileBinding.btnFollowing.visibility = View.GONE
            }
            else{
                othersProfileBinding.btnFollow.visibility = View.GONE
                othersProfileBinding.btnRequested.visibility = View.GONE
                othersProfileBinding.btnFollowing.visibility = View.VISIBLE
            }

            otherProfileViewModel.followUser()

            otherProfileViewModel.followUser = true
            otherProfileViewModel.unfollowUser = false
        }

        othersProfileBinding.btnFollowing.setOnClickListener {

            othersProfileBinding.btnFollow.visibility = View.VISIBLE
            othersProfileBinding.btnRequested.visibility = View.GONE
            othersProfileBinding.btnFollowing.visibility = View.GONE
            otherProfileViewModel.unFollowUser()

            otherProfileViewModel.followUser = false
            otherProfileViewModel.unfollowUser = true
        }

        othersProfileBinding.btnRequested.setOnClickListener {

            othersProfileBinding.btnFollow.visibility = View.VISIBLE
            othersProfileBinding.btnRequested.visibility = View.GONE
            othersProfileBinding.btnFollowing.visibility = View.GONE

            otherProfileViewModel.cancelRequestUser()

            otherProfileViewModel.followUser = false
            otherProfileViewModel.unfollowUser = true
        }
    }

    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()
//        if(otherProfileViewModel.followUser){
//            otherProfileViewModel.followUser()
//        }
//        if(otherProfileViewModel.unfollowUser){
//            otherProfileViewModel.followUser()
//        }

    }





    override fun onPostItemClickEvent() {
        recyclerView?.scrollToPosition(0)
        view?.findViewById<AppBarLayout>(R.id.appBarLayout)?.setExpanded(true,true)

    }

}