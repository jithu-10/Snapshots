package com.example.instagramv1.ui.searchscreen

import android.content.Context
import android.os.Bundle
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
import com.example.instagramv1.adapters.ProfileSimpleRecyclerAdapter
import com.example.instagramv1.model.SimpleProfileType
import com.example.instagramv1.ui.mainscreen.profilescreen.connectionscreen.ConnectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountsSearchFragment : Fragment() {

    private val connectionViewModel by activityViewModels<ConnectionViewModel>()
    private val searchViewModel by activityViewModels<SearchViewModel>()
    private var fragmentView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView==null){
            val view =  inflater.inflate(R.layout.fragment_accounts_search, container, false)
            fragmentView = view
            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            connectionViewModel.userId = userId
            searchViewModel.userId = userId

            val searchString = arguments?.getString("SEARCH_STRING")

            val accountsRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
            accountsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val accountsRecyclerAdapter = ProfileSimpleRecyclerAdapter(connectionViewModel,SimpleProfileType.SEARCH_VIEW)
            accountsRecyclerView.adapter = accountsRecyclerAdapter

            lifecycleScope.launch{
                searchViewModel.getUsersByName(searchString!!).observe(requireActivity()){
                    accountsRecyclerAdapter.setUsers(it)
                    view.findViewById<View>(R.id.pbLoading).visibility = View.GONE
                    if(it.isEmpty()){
                        accountsRecyclerView.visibility = View.GONE
                        view.findViewById<TextView>(R.id.tvNoResultsFound).visibility = View.VISIBLE
                    }
                    else{
                        accountsRecyclerView.visibility = View.VISIBLE
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