package com.example.instagramv1.ui.mainscreen.othersprofilescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.R
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen.ConnectionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OthersProfileConnectionsFragment : Fragment() {

    private val viewModel by activityViewModels<ConnectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_others_connections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserConnectionsPageViewPager(view)



        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setUserConnectionsPageViewPager(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.userConnectionsViewPager)
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        val otherUserId = arguments?.getInt("OTHER_USER_ID")
        val followersFragment = OtherUsersFollowersFragment().apply {
            arguments = Bundle().apply {
                putInt("OTHER_USER_ID",otherUserId!!)}
        }
        val followingFragment = OtherUsersFollowingFragment().apply {
            arguments = Bundle().apply {
                putInt("OTHER_USER_ID",otherUserId!!)}
        }


        viewPagerAdapter.add(followersFragment)
        viewPagerAdapter.add(followingFragment)
        viewPager2.adapter = viewPagerAdapter



        val connectionsTabLayout = view.findViewById<TabLayout>(R.id.connectionsTabLayout)
        TabLayoutMediator(connectionsTabLayout, viewPager2) { tab, position ->
            val focusedType = arguments?.getInt("focusedType")
            if(focusedType != null) {
                viewPager2.setCurrentItem(focusedType,false)
            }
            else{
                viewPager2.setCurrentItem(tab.position,true)
            }

            if(position==0){
                lifecycleScope.launch{
                    viewModel.getNoOfFollowers(otherUserId!!).observe(requireActivity()){
                        if(it ==1){
                            tab.text="$it follower"
                        }
                        else{
                            tab.text = "$it followers"
                        }


                    }
                }
            }
            else if (position == 1){
                lifecycleScope.launch{
                    viewModel.getNoOfFollowings(otherUserId!!).observe(requireActivity()){
                        tab.text = "$it following"
                    }

                }
            }

        }.attach()






    }



}