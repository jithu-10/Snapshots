package com.example.instagramv1


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.adapters.ProfileViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ProfileFragment : Fragment() {


    private var shouldScroll = false

    companion object{
        const val FOLLOWERS_COUNT = 0
        const val FOLLOWING_COUNT = 1

        /***better to be placed in the shared view model***/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfilePageViewPager(view)

        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        view.findViewById<ImageView>(R.id.imgViewSettingsBtn).setOnClickListener {
            showBottomSheetDialog()
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




    }

    private fun openConnectionsFragment(focusedType : Int){
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frame_page,UserConnectionsFragment().apply {
                arguments = Bundle().apply { putInt("focusedType",focusedType) }
            })
            addToBackStack(null)
            commit()
        }
    }

    private fun showBottomSheetDialog() {
        val settingsBottomSheetFragment = SettingsBottomSheetFragment()
        settingsBottomSheetFragment.show(childFragmentManager,tag)
    }

    private fun setProfilePageViewPager(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.profile_viewpager2)
        val profileViewPagerAdapter = ProfileViewPagerAdapter(childFragmentManager,lifecycle)
        val userPostsFragment = UserPostsFragment()
        val savedPostsFragment = SavedPostsFragment()
        profileViewPagerAdapter.add(userPostsFragment)
        profileViewPagerAdapter.add(savedPostsFragment)
        viewPager2.adapter = profileViewPagerAdapter

        val profileTabLayout = view.findViewById<TabLayout>(R.id.profile_tab_layout)
        TabLayoutMediator(profileTabLayout, viewPager2) { tab, position ->
            viewPager2.setCurrentItem(tab.position,true)
            if(position==0){
                tab.icon = AppCompatResources.getDrawable(requireActivity(),R.drawable.ic_baseline_grid_on_24)
            }
            else{
                tab.icon = AppCompatResources.getDrawable(requireActivity(),R.drawable.ic_outline_bookmark_border_24)
            }
        }.attach()




        val appBarLayout = view.findViewById(R.id.appBarLayout) as AppBarLayout
        appBarLayout.addOnOffsetChangedListener { appBar, verticalOffset ->
            if(verticalOffset==0){
                shouldScroll = true
            }
        }




        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(shouldScroll){

                    val fragment = childFragmentManager.findFragmentByTag("f${viewPager2.currentItem}")
                    if(fragment is SavedPostsFragment){
                        fragment.postsRecyclerView?.scrollToPosition(0)
                    }
                    if(fragment is UserPostsFragment){
                        fragment.postsRecyclerView?.scrollToPosition(0)

                    }

                }

            }

        })


    }



}