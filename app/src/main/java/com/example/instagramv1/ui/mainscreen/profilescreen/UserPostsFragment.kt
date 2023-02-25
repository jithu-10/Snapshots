package com.example.instagramv1.ui.mainscreen.profilescreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.explorescreen.ExplorePostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserPostsFragment : Fragment(),PostsRecyclerAdapter.EventListener {
    var postsRecyclerView : RecyclerView? = null

    private val viewModel by activityViewModels<ExplorePostsViewModel>()
    private val postViewModel by activityViewModels<PostViewModel>()
    private var fragmentView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            val view = inflater.inflate(R.layout.fragment_user_posts, container, false)
            fragmentView = view
            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            viewModel.userId = userId
            postViewModel.userId = userId

            postsRecyclerView = view.findViewById<RecyclerView>(R.id.profile_page_user_posts_recycler_view)
            postsRecyclerView?.layoutManager = LinearLayoutManager(requireActivity())
            val postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,this)
            postRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            postsRecyclerView?.adapter = postRecyclerAdapter

            lifecycleScope.launch {
                viewModel.getUserPosts().observe(requireActivity()){
                    postRecyclerAdapter.setPosts(it,false)
                    view.findViewById<FrameLayout>(R.id.pbLoading).visibility = View.GONE
                    Log.d("Special Logs",it.size.toString())
                    if(it.isEmpty()){
                        postsRecyclerView?.visibility = View.GONE
                        view.findViewById<View>(R.id.tvNoResultsFound).visibility = View.VISIBLE
                    }
                    else{
                        postsRecyclerView?.visibility = View.VISIBLE
                        view.findViewById<View>(R.id.tvNoResultsFound).visibility = View.GONE
                    }

                }
            }

        }
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        postViewModel.addAllReaction()
        postViewModel.removeAllReaction()

    }

    override fun onPostItemClickEvent() {

    }

}