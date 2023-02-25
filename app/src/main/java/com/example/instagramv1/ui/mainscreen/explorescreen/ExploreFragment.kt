package com.example.instagramv1.ui.mainscreen.explorescreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R

import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.model.FilterOptions
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.searchscreen.SearchFragment
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private val viewModel by activityViewModels<ExplorePostsViewModel>()
    private val postViewModel by activityViewModels<PostViewModel>()


    private var fragmentView : View? = null
    private var filterOptions : FilterOptions = FilterOptions.SORT_BY_DATE_DESC

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView==null){
            val view = inflater.inflate(R.layout.fragment_explore, container, false)
            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            postViewModel.userId = userId
            viewModel.userId = userId

            val searchFragment = SearchFragment()

            view.findViewById<ConstraintLayout>(R.id.search_bar).setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,searchFragment)
                    addToBackStack(null)
                    commit()
                }
            }

            view.findViewById<ImageView>(R.id.imgViewSortBtn).setOnClickListener {
                showFilterBottomSheetFragment()
            }


            postsRecyclerView = view.findViewById<RecyclerView>(R.id.explore_page_posts_recycler_view)
            layoutManager = LinearLayoutManager(requireActivity())
            postsRecyclerView.layoutManager = layoutManager
            val postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,null)
            postsRecyclerView.adapter = postRecyclerAdapter
            postRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY






//            val observerExplorePosts = Observer<List<PostViewData2>> {
//                postRecyclerAdapter.setPosts(it,true)
//            }




//            lifecycleScope.launch {
//                if(filterOptions == FilterOptions.SORT_BY_DATE_DESC){
//                    viewModel.getExplorePosts().observe(viewLifecycleOwner){
//                        postRecyclerAdapter.setPosts(it,false)
//                    }
//                }
//                else if(filterOptions == FilterOptions.SORT_BY_DATE_ASC){
//                    viewModel.getAllPostsAscending().observe(viewLifecycleOwner){
//                        postRecyclerAdapter.setPosts(it,false)
//                    }
//                }
//                else if(filterOptions == FilterOptions.MOST_LIKED){
//                    viewModel.getAllPostsByMostLiked().observe(viewLifecycleOwner){
//                        postRecyclerAdapter.setPosts(it,false)
//                    }
//                }
//
//                else if(filterOptions == FilterOptions.MOST_COMMENTED){
//                    viewModel.getAllPostsByMostCommented().observe(viewLifecycleOwner){
//                        postRecyclerAdapter.setPosts(it,false)
//                    }
//                }
//
//            }

            lifecycleScope.launch{
                viewModel.getExplorePosts().observe(requireActivity()){
                    postRecyclerAdapter.setPosts(it,false,filterOptions)

                    view.findViewById<FrameLayout>(R.id.pbLoading).visibility = View.GONE
                    if(it.isEmpty()){
                        view.findViewById<View>(R.id.no_content_frame).visibility = View.VISIBLE
                        postsRecyclerView.visibility = View.GONE
                    }
                    else{
                        view.findViewById<View>(R.id.no_content_frame).visibility = View.GONE
                        postsRecyclerView.visibility = View.VISIBLE
                    }
                }
            }

            postViewModel.sortedOps.observe(requireActivity()){

                filterOptions = it
                postRecyclerAdapter.setPosts(false,filterOptions)
                postsRecyclerView.scrollToPosition(0)

            }







//            lifecycleScope.launch{
//                postViewModel.sortedOps.observe(viewLifecycleOwner){
//
//                }
//            }

//            lifecycleScope.launch {
//                viewModel.getExplorePosts2(postViewModel.sortedOps.value!!).observe(viewLifecycleOwner,observerExplorePosts)
//
//            }

            fragmentView = view


            return fragmentView
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onResume() {
        super.onResume()
        val bottomNavigationView = requireActivity().findViewById<NavigationBarView>(R.id.bottom_navigation)
        val menuItem = bottomNavigationView.menu.findItem(R.id.explore_page)
        if(!menuItem.isChecked){
            menuItem.isChecked = true
        }
        postViewModel.changed.value = true

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    bottomNavigationView.selectedItemId = R.id.home_page
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)



    }


    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()

    }

    private fun showFilterBottomSheetFragment(){
        val filterBottomSheetFragment = FilterBottomSheetFragment()
        filterBottomSheetFragment.show(parentFragmentManager,tag)


    }




}