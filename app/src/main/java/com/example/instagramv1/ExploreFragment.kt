package com.example.instagramv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class ExploreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.explore_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }

        val searchFragment = SearchFragment()

        view.findViewById<ConstraintLayout>(R.id.search_bar).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_page,searchFragment)
                addToBackStack(null)
                commit()
            }
        }

        val postsRecyclerView = view.findViewById<RecyclerView>(R.id.explore_page_posts_recycler_view)
        postsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        postsRecyclerView.adapter = PostsRecyclerAdapter()
    }

}