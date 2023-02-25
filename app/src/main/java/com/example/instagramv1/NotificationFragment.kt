package com.example.instagramv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNotificationPageViewPager(view)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.notification_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }


    }

    private fun setNotificationPageViewPager(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.notification_view_pager)
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)
        val suggestedAccountsFragment = SuggestedAccountsFragment()
        val followRequestsFragment = FollowRequestsFragment()
        val notificationViewFragment = NotificationViewFragment()

        viewPagerAdapter.add(followRequestsFragment)
        viewPagerAdapter.add(notificationViewFragment)
        viewPagerAdapter.add(suggestedAccountsFragment)
        viewPager2.adapter = viewPagerAdapter

        val notificationTabLayout = view.findViewById<TabLayout>(R.id.notification_tabLayout)
        TabLayoutMediator(notificationTabLayout, viewPager2) { tab, position ->
            viewPager2.setCurrentItem(tab.position,true)
            if(position==0){
                tab.text = "Requests"
            }
            else if (position == 1){
                tab.text = "Updates"
            }
            else{
                tab.text = "Suggestions"
            }
        }.attach()

    }


}