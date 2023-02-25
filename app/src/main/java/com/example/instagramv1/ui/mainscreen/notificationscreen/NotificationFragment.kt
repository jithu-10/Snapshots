package com.example.instagramv1.ui.mainscreen.notificationscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.R
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.example.instagramv1.ui.mainscreen.UserProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val viewModel by activityViewModels<UserProfileViewModel>()
    private val notificationViewModel by activityViewModels<NotificationViewModel>()
    private val suggestedAccountsFragment = SuggestedAccountsFragment()
    private val followRequestsFragment = FollowRequestsFragment()
    private val notificationViewFragment = NotificationViewFragment()
    private var fragmentView : View? = null
    private var start : Boolean = true





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            val view =  inflater.inflate(R.layout.fragment_notification, container, false)
            setNotificationViewPager(view)
            fragmentView = view
            return view
        }
        return fragmentView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        lifecycleScope.launch {
//            viewModel.getUserPrivacy().observe(viewLifecycleOwner){
//
//                if(it){
//                    setNotificationPageViewPagerForPrivateAccount(view)
//                }
//                else{
//                    setNotificationPageViewPagerForPublicAccount(view)
//                }
//
//
//            }
//        }




    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.notification_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    bottomNavigationView.selectedItemId = R.id.home_page
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


    }

    private fun setNotificationViewPager(view : View){

        val viewPager2 = view.findViewById<ViewPager2>(R.id.notification_view_pager)
        viewPager2.isSaveEnabled = false

        val viewPagerAdapterPrivate  = ViewPagerAdapter(parentFragmentManager,lifecycle)
        val viewPagerAdapterPublic = ViewPagerAdapter(parentFragmentManager,lifecycle)


        viewPagerAdapterPrivate.add(followRequestsFragment)
        viewPagerAdapterPrivate.add(notificationViewFragment)
        viewPagerAdapterPrivate.add(suggestedAccountsFragment)


        viewPagerAdapterPublic.add(notificationViewFragment)
        viewPagerAdapterPublic.add(suggestedAccountsFragment)

        if(viewModel.privateAccount!!){
            setNotificationPageViewPagerForPrivateAccount(view)
        }
        else{
            setNotificationPageViewPagerForPublicAccount(view)
        }
        

        lifecycleScope.launch {
            viewModel.getUserPrivacy().observe(requireActivity()){
                if(start){
                    start = false

                }
                else{
                    fragmentView = null
                    start = true
                }

//                if(it){
//                    setNotificationPageViewPagerForPrivateAccount(view)
//                }
//                else{
//                    setNotificationPageViewPagerForPublicAccount(view)
//                }

            }
        }




    }

    private fun setNotificationPageViewPagerForPrivateAccount(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.notification_view_pager)
        viewPager2.isSaveEnabled = false
        val viewPagerAdapter = ViewPagerAdapter(parentFragmentManager,lifecycle)
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

    private fun setNotificationPageViewPagerForPublicAccount(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.notification_view_pager)
        viewPager2.isSaveEnabled = false
        val viewPagerAdapter = ViewPagerAdapter(parentFragmentManager,lifecycle)
        val notificationViewFragment = NotificationViewFragment()
        val suggestedAccountsFragment = SuggestedAccountsFragment()


        viewPagerAdapter.add(notificationViewFragment)
        viewPagerAdapter.add(suggestedAccountsFragment)
        viewPager2.adapter = viewPagerAdapter

        val notificationTabLayout = view.findViewById<TabLayout>(R.id.notification_tabLayout)
        TabLayoutMediator(notificationTabLayout, viewPager2) { tab, position ->
            viewPager2.setCurrentItem(tab.position,true)

            if (position == 0){
                tab.text = "Updates"
            }
            else{
                tab.text = "Suggestions"
            }
        }.attach()
    }

    override fun onPause() {
        super.onPause()
        notificationViewModel.followAllUsers()
    }

}