package com.example.instagramv1.adapters

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    private val fragments: MutableList<Fragment> = ArrayList()

    fun add(fragment: Fragment) {
        fragments.add(fragment)
    }

//    fun removeAndAdd(newFragment : Fragment): Boolean?{
//        for(fragment in fragments){
//            if(fragment is UserPostsFragment && newFragment is UserPostsFragment){
//                val index = fragments.indexOf(fragment)
//                fragments.set(index,newFragment)
//                return true
//            }
//
//            else if(fragment is SavedPostsFragment && newFragment is SavedPostsFragment){
//                val index = fragments.indexOf(fragment)
//                fragments.set(index,newFragment)
//                return false
//            }
//        }
//        return null
//
//    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position];
    }

//    override fun getItemViewType(position: Int): Int {
//        return PagerAdapter.POSITION_NONE
//    }
}