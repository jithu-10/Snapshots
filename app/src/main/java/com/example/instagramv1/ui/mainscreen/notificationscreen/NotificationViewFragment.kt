package com.example.instagramv1.ui.mainscreen.notificationscreen

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.NotificationUpdatesRecyclerAdapter
import com.example.instagramv1.adapters.NotificationViewRecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationViewFragment : Fragment() {

    private val viewModel by activityViewModels<NotificationViewModel>()
    private var fragmentView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            val view =  inflater.inflate(R.layout.fragment_notification_view, container, false)
            fragmentView = view

            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            viewModel.userId = userId

            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
//            val notificationAdapter = NotificationViewRecyclerAdapter(viewModel).apply {
//                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//            }
//            recyclerView.adapter = notificationAdapter
//            val configuration = resources.configuration
//            if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
//                recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
//            }
//            else{
//                recyclerView.layoutManager = LinearLayoutManager(requireActivity())
//            }

            val notificationAdapter = NotificationUpdatesRecyclerAdapter(viewModel).apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
            recyclerView.adapter = notificationAdapter
            val configuration = resources.configuration
            if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                recyclerView.layoutManager = GridLayoutManager(requireActivity(),2)
            }
            else{
                recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            }

//            lifecycleScope.launch {
//                viewModel.getNotifications().observe(requireActivity()){
//                    notificationAdapter.setNotifications(it)
//                    view.findViewById<View>(R.id.pbLoading).visibility = View.GONE
//                    if(it.isEmpty()){
//                        recyclerView.visibility = View.GONE
//                        view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.VISIBLE
//                    }
//                    else{
//                        recyclerView.visibility = View.VISIBLE
//                        view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.GONE
//                    }
//                }
//            }

            lifecycleScope.launch{
                viewModel.getNotificationUpdates().observe(requireActivity()){
                    notificationAdapter.setNotifications(it)
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

            return fragmentView
        }
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}