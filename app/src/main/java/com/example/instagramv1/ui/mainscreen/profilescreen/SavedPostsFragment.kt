package com.example.instagramv1.ui.mainscreen.profilescreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.PostsRecyclerAdapter
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.explorescreen.ExplorePostsViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedPostsFragment : Fragment(),PostsRecyclerAdapter.EventListener {

    var postsRecyclerView : RecyclerView?= null

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
            val view = inflater.inflate(R.layout.fragment_saved_posts, container, false)
            fragmentView = view
            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            viewModel.userId = userId
            postViewModel.userId = userId

            postsRecyclerView = view.findViewById<RecyclerView>(R.id.profile_page_saved_posts_recycler_view)
            postsRecyclerView?.layoutManager = LinearLayoutManager(requireActivity())
            val postRecyclerAdapter = PostsRecyclerAdapter(postViewModel,this)
            postRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            postsRecyclerView?.adapter = postRecyclerAdapter

            lifecycleScope.launch{
                viewModel.getUserSavedPosts().observe(requireActivity()){
                    postRecyclerAdapter.setPosts(it,false)
                    view.findViewById<FrameLayout>(R.id.pbLoading).visibility = View.GONE

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
            return fragmentView
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
        val profileFragment: ProfileFragment =
            parentFragmentManager.findFragmentByTag("PROFILE_FRAGMENT") as ProfileFragment
        postsRecyclerView?.scrollToPosition(0)

        profileFragment.view?.findViewById<AppBarLayout>(R.id.appBarLayout)?.setExpanded(true,true)
    }

}