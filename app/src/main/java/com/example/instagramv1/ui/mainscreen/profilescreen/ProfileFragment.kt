package com.example.instagramv1.ui.mainscreen.profilescreen



import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.*
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.example.instagramv1.databinding.ActivitySampleBinding
import com.example.instagramv1.databinding.FragmentProfileBinding
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.UserProfileViewModel
import com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen.UserConnectionsFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen.EditProfileActivity
import com.example.instagramv1.utils.DelayClickListener
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment(), DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {


    private val viewModel by activityViewModels<UserProfileViewModel>()
    private val postViewModel by activityViewModels<PostViewModel>()
    private lateinit var profileBinding: FragmentProfileBinding
    private var fragmentView: View? = null
    private var settingsBottomSheetFragment : SettingsBottomSheetFragment? = null
    private var menuItemId : Int = -1
    private var shouldScroll = false

    companion object{
        const val FOLLOWERS_COUNT = 0
        const val FOLLOWING_COUNT = 1

        /***better to be placed in the shared view model***/
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View ?{

//        profileBinding = DataBindingUtil.inflate(inflater,
//            R.layout.fragment_profile, container, false)
//        profileBinding.userProfileViewModel = viewModel
//
        if(fragmentView == null){
            val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
            menuItemId = bottomNavigationView.selectedItemId
            profileBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false)
            profileBinding.userProfileViewModel = viewModel
            val view  = profileBinding.root
            fragmentView = view

            fetchData()
            setProfilePageViewPager(view)

            view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            view.findViewById<ImageView>(R.id.imgViewSettingsBtn).setOnClickListener(object : DelayClickListener(){
                override fun onDelayClick() {
                    showSettingsBottomSheetFragment()
                }

            })
//            view.findViewById<ImageView>(R.id.imgViewSettingsBtn).setOnClickListener {
//                showSettingsBottomSheetFragment()
//
//            }
            view.findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
                //showEditProfileBottomSheetFragment()
                startEditProfileActivity()
            }

            view.findViewById<ConstraintLayout>(R.id.followersCountLayout).setOnClickListener {
                openConnectionsFragment(FOLLOWERS_COUNT)
            }
            view.findViewById<ConstraintLayout>(R.id.followingCountLayout).setOnClickListener {
                openConnectionsFragment(FOLLOWING_COUNT)
            }
            view.findViewById<ConstraintLayout>(R.id.postCountLayout).setOnClickListener {
                view.findViewById<AppBarLayout>(R.id.appBarLayout).setExpanded(false,true)
            }




            return fragmentView

        }
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        profileBinding.invalidateAll()
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(menuItemId)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }





    private fun openConnectionsFragment(focusedType : Int){
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_page, UserConnectionsFragment().apply {
                arguments = Bundle().apply { putInt("focusedType",focusedType) }
            })
            addToBackStack(null)
            commit()
        }
    }

    private fun showSettingsBottomSheetFragment() {
        val settingsBottomSheetFragment = SettingsBottomSheetFragment()
        settingsBottomSheetFragment.show(parentFragmentManager,tag)


    }




    private fun startEditProfileActivity(){
        Intent(requireActivity(),EditProfileActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun setProfilePageViewPager(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.profile_viewpager2)
        val profileViewPagerAdapter = ViewPagerAdapter(parentFragmentManager,lifecycle)
        val userPostsFragment = UserPostsFragment()
        val savedPostsFragment = SavedPostsFragment()
        profileViewPagerAdapter.add(userPostsFragment)
        profileViewPagerAdapter.add(savedPostsFragment)
        viewPager2.adapter = profileViewPagerAdapter
        viewPager2.isSaveEnabled = false
        //\viewPager2.isSaveFromParentEnabled = false

        val profileTabLayout = view.findViewById<TabLayout>(R.id.profile_tab_layout)
        TabLayoutMediator(profileTabLayout, viewPager2) { tab, position ->
            viewPager2.setCurrentItem(tab.position,true)
            if(position==0){
                tab.icon = AppCompatResources.getDrawable(requireActivity(),
                    R.drawable.ic_outline_posts_icon_24
                )
            }
            else{
                tab.icon = AppCompatResources.getDrawable(requireActivity(),
                    R.drawable.ic_outline_bookmark_border_24
                )
            }
        }.attach()






        val appBarLayout = view.findViewById(R.id.appBarLayout) as AppBarLayout
        appBarLayout.addOnOffsetChangedListener { appBar, verticalOffset ->
            Log.d("Scroll Testing","Vertical Offset : $verticalOffset")
            Log.d("Scroll Testing","Scroll Range : ${appBarLayout.totalScrollRange}")
            val offset = Math.abs(verticalOffset)

            if(offset < appBarLayout.totalScrollRange){
                shouldScroll = true

            }
        }

        viewPager2.offscreenPageLimit = 2

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                Log.d("Scroll Testing","Should Scroll : $shouldScroll")
                if(shouldScroll){
//                    val fragmentManager: FragmentManager = parentFragmentManager
//                    val fragmentList: List<Fragment> = fragmentManager.fragments
//                    val currentFragments = mutableListOf<Fragment>()
//                    for (fragment in fragmentList) {
//                        val tag = fragment.tag
//                        if(tag == "f${viewPager2.currentItem}"){
//                            currentFragments.add(fragment)
//                        }
//                        Log.d("Fragment List",fragment.toString()+" tag : "+tag)
//                        // Do something with the fragment and its tag
//                    }
//



                    val fragment = parentFragmentManager.findFragmentByTag("f${viewPager2.currentItem}")

                    if(viewPager2.currentItem == 0){
                        userPostsFragment.postsRecyclerView?.scrollToPosition(0)
                    }
                    else{
                        savedPostsFragment.postsRecyclerView?.scrollToPosition(0)
                    }

//                    Log.d("Scroll Testing Fragment",fragment.toString())
//                    if(fragment is SavedPostsFragment){
//                        fragment.postsRecyclerView?.scrollToPosition(0)
//                        Log.d("Scroll Testing","Scrolled")
//                    }
//                    if(fragment is UserPostsFragment){
//                        fragment.postsRecyclerView?.scrollToPosition(0)
//                        Log.d("Scroll Testing","Scrolled")
//                    }
                    Log.d("Scroll Testing","Is Lifted : ${appBarLayout.isLifted}")
                    if(!appBarLayout.isLifted){


                    }
                    shouldScroll = false





                }



            }

        })


    }


    private fun fetchData(){

        lifecycleScope.launch{
            val userData = viewModel.getUserProfileData()
            userData.observe(requireActivity()){
                viewModel.profilePicture = userData.value?.profile_picture
                viewModel.fullName  = userData.value?.full_name
                viewModel.userName = userData.value?.user_name
                viewModel.noOfPosts = userData.value?.num_posts
                viewModel.noOfFollowers = userData.value?.num_followers
                viewModel.noOfFollowing = userData.value?.num_following

                profileBinding.invalidateAll()

                view?.findViewById<FrameLayout>(R.id.profileLoading)?.visibility = View.GONE
                view?.findViewById<CoordinatorLayout>(R.id.coordinator)?.visibility = View.VISIBLE
            }






        }


    }

    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()
    }

    override fun onCancel(dialog: DialogInterface?) {

    }

    override fun onDismiss(dialog: DialogInterface?) {

    }

}