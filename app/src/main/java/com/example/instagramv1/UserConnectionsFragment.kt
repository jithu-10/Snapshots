package com.example.instagramv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserConnectionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_connections, container, false)
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
        val followersFragment = FollowersFragment()
        val followingFragment = FollowingFragment()


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
                tab.text = "45 followers"
            }
            else if (position == 1){
                tab.text = "45 following"
            }

        }.attach()






    }
}