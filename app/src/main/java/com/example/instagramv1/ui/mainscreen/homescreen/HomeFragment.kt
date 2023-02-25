package com.example.instagramv1.ui.mainscreen.homescreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.databinding.FragmentHomeBinding
import com.example.instagramv1.ui.addpostscreen.AddPostActivity
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.UserProfileViewModel
import com.example.instagramv1.ui.mainscreen.explorescreen.ExplorePostsViewModel
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val profileViewModel by activityViewModels<UserProfileViewModel>()
    private val viewModel by activityViewModels<ExplorePostsViewModel>()
    private val postViewModel by activityViewModels<PostViewModel>()
    private lateinit var homeBinding: FragmentHomeBinding

    private var fragmentView : View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            homeBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_home, container, false)
            homeBinding.userProfileViewModel = profileViewModel
            val view = homeBinding.root
            fragmentView = view

            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            postViewModel.userId = userId
            viewModel.userId = userId

            fetchData()
            val profileFragment = ProfileFragment()
            view.findViewById<CardView>(R.id.cardViewUserProfile).setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,profileFragment,"PROFILE_FRAGMENT")
                    addToBackStack("PROFILE_FRAGMENT")
                    commit()
                }
            }

            view.findViewById<ImageView>(R.id.cameraLogo).setOnClickListener {
                val intent = Intent(requireActivity(),AddPostActivity::class.java).apply {
                    putExtra("OPEN_CAMERA",1)
                }
                startActivity(intent)
            }


            val postsRecyclerView = view.findViewById<RecyclerView>(R.id.home_page_posts_recycler_view)
            postsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,null)
            //postRecyclerAdapter.setHasStableIds(true)
            postsRecyclerView.adapter = postRecyclerAdapter
            postRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            lifecycleScope.launch {
                viewModel.getFollowingPosts().observe(requireActivity()){
                    postRecyclerAdapter.setPosts(it,false)
                    homeBinding.pbLoading.visibility = View.GONE
                    if(it.isEmpty()){
                        postsRecyclerView.visibility = View.GONE
                        view.findViewById<View>(R.id.no_content_frame).visibility = View.VISIBLE

                    }
                    else{
                        postsRecyclerView.visibility = View.VISIBLE
                        view.findViewById<View>(R.id.no_content_frame).visibility = View.GONE

                    }
                }
            }
            return fragmentView
        }
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun fetchData(){
        lifecycleScope.launch{
            profileViewModel.getUserProfileData().observe(requireActivity()){
                profileViewModel.profilePicture = it.profile_picture
                if(it.profile_picture == null){
                    homeBinding.userDp.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.default_profile_pic,null))
                }
                else{
                    homeBinding.userDp.setImageBitmap(it.profile_picture)
                }

            }

        }
    }


    override fun onResume() {
        super.onResume()
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.home_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val postsRecyclerView = view?.findViewById<RecyclerView>(R.id.home_page_posts_recycler_view)
                    if(postsRecyclerView?.computeVerticalScrollOffset()==0){
                        requireActivity().finish()
                    }
                    else{
                        postsRecyclerView?.smoothScrollToPosition(0)
                    }


                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)



    }

    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()

    }






}