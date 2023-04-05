package com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.ProfileSimpleRecyclerAdapter
import com.example.instagramv1.model.SimpleProfileType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowersFragment : Fragment() {

    private val viewModel by viewModels<ConnectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        viewModel.userId = userId

        val recyclerView = view.findViewById<RecyclerView>(R.id.followers_recycler_view)
        val profileSimpleRecyclerAdapter = ProfileSimpleRecyclerAdapter(viewModel, SimpleProfileType.FOLLOWERS_VIEW)
        recyclerView.adapter = profileSimpleRecyclerAdapter
        val configuration = resources.configuration
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
        }
        else{
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        }
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch{
            viewModel.getFollowers().observe(viewLifecycleOwner){
                profileSimpleRecyclerAdapter.setUsers(it)

                if(it.isEmpty()){
                    view.findViewById<View>(R.id.no_content_frame).visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                else{
                    view.findViewById<View>(R.id.no_content_frame).visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

}