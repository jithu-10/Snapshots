package com.example.instagramv1.ui.mainscreen.notificationscreen

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.FollowRequestsRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowRequestsFragment : Fragment() {

    private val viewModel by activityViewModels<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_follow_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID",-1)
        viewModel.userId = userId

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val followRequestsRecyclerAdapter = FollowRequestsRecyclerAdapter(viewModel)
        followRequestsRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        recyclerView.adapter = followRequestsRecyclerAdapter

        val configuration = resources.configuration
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
        }
        else{
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        }
        //recyclerView.layoutManager = LinearLayoutManager(requireActivity())



//        val swipeToDeleteCallBack = object : SwipeToDeleteCallBack(){
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                 val position = viewHolder.adapterPosition
//                //business logic
//                recyclerView.adapter?.notifyItemRemoved(position)
//
//            }
//
//        }
//        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
//        itemTouchHelper.attachToRecyclerView(recyclerView)

        lifecycleScope.launch{
            viewModel.getFollowRequests().observe(requireActivity()){
                followRequestsRecyclerAdapter.setRequestedUsers(it)
                view.findViewById<View>(R.id.pbLoading).visibility = View.GONE
                if(it.isEmpty()){
                    recyclerView.visibility = View.GONE
                    view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.VISIBLE
                }
                else{
                    recyclerView.visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.GONE
                }
            }
        }
    }

}