package com.example.instagramv1.ui.searchscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.ui.mainscreen.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlacesSearchFragment : Fragment() {

    private val postViewModel by activityViewModels<PostViewModel>()
    private val searchViewModel by activityViewModels<SearchViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_places_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        postViewModel.userId = userId
        searchViewModel.userId = userId

        val searchString = arguments?.getString("SEARCH_STRING")
        Log.d("Search Places",searchString.toString());
        val postsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        postsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,null)
        postRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        postsRecyclerView.adapter = postRecyclerAdapter

        lifecycleScope.launch{
            searchViewModel.getPostByLocation(searchString!!).observe(requireActivity()){
                Log.d("Search Places",it.size.toString())
                postRecyclerAdapter.setPosts(it,false)
                view.findViewById<View>(R.id.pbLoading).visibility = View.GONE
                if(it.isEmpty()){
                    postsRecyclerView.visibility = View.GONE
                    view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.VISIBLE
                }
                else{
                    postsRecyclerView.visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.GONE
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()

    }

}