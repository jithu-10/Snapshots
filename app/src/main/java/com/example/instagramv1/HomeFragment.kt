package com.example.instagramv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profileFragment = ProfileFragment()
        view.findViewById<CardView>(R.id.cardViewUserProfile).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_page,profileFragment)
                addToBackStack(null)
                commit()
            }
        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.home_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

        val postsRecyclerView = view.findViewById<RecyclerView>(R.id.home_page_posts_recycler_view)
        postsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        postsRecyclerView.adapter = PostsRecyclerAdapter()
    }


}