package com.example.instagramv1.ui.searchscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.instagramv1.R
import com.example.instagramv1.adapters.SearchHistoryRecyclerAdapter
import com.example.instagramv1.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(),SearchHistoryRecyclerAdapter.EventListener {

    private val viewModel by activityViewModels<SearchViewModel>()

    private var fragmentView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(fragmentView == null){
            val view = inflater.inflate(R.layout.fragment_search, container, false)
            fragmentView = view

            val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
            val userId = sharedPref.getInt("USER_ID",-1)
            viewModel.userId = userId


            return fragmentView
        }
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgr: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val searchEt = view.findViewById<EditText>(R.id.etSearch)
        imgr.showSoftInput(searchEt, 0)

        val searchHistoryRecyclerView = view.findViewById<RecyclerView>(R.id.history_recycler_view)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val searchHistoryRecyclerAdapter = SearchHistoryRecyclerAdapter(viewModel,this)
        searchHistoryRecyclerView.adapter = searchHistoryRecyclerAdapter
        searchHistoryRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY


        lifecycleScope.launch{
            viewModel.getSearchHistory().observe(requireActivity()){
                if(it.isEmpty()){
                    view.findViewById<TextView>(R.id.clearAllBtn).visibility = View.GONE
                    view.findViewById<TextView>(R.id.recentTv).visibility = View.GONE
                }
                else{
                    view.findViewById<TextView>(R.id.clearAllBtn).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.recentTv).visibility = View.VISIBLE
                }
                searchHistoryRecyclerAdapter.setHistory(it)
            }
        }

        view.findViewById<TextView>(R.id.clearAllBtn).setOnClickListener {
            viewModel.deleteAllSearchHistory()
        }

        view.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
            imgr.hideSoftInputFromWindow(searchEt.windowToken,0)
            searchEt.setText("")
            fragmentView = null
        }

        searchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(searchEt.text.isNotBlank()){
                    searchEt.clearFocus()
                    Log.d("Search History",searchEt.text.toString())
                    viewModel.addSearchHistory(searchEt.text.toString())
                    imgr.hideSoftInputFromWindow(searchEt.windowToken,0)
                    loadSearchViewPager(view)
                    view.findViewById<FrameLayout>(R.id.history_layout).visibility = View.GONE
                }
                else{
                    imgr.hideSoftInputFromWindow(searchEt.windowToken,0)
                }

            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
//        val callback: OnBackPressedCallback =
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    val imgr: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    val searchEt = view?.findViewById<EditText>(R.id.etSearch)
//
//                    imgr.hideSoftInputFromWindow(searchEt?.windowToken,0)
//                    searchEt?.setText("")
//                    fragmentView = null
//                }
//            }
//        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onPause() {
        super.onPause()
        fragmentView = null
    }

    private fun loadSearchViewPager(view : View){
        val viewPager2 = view.findViewById<ViewPager2>(R.id.search_view_pager)
        viewPager2.isSaveEnabled = false
        viewPager2.visibility = View.VISIBLE

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        val accountsSearchFragment = AccountsSearchFragment().apply {
            arguments = Bundle().apply {
                putString("SEARCH_STRING",view.findViewById<EditText>(R.id.etSearch).text.toString())
            }
        }
        val placesSearchFragment = PlacesSearchFragment().apply {
            arguments = Bundle().apply {
                putString("SEARCH_STRING",view.findViewById<EditText>(R.id.etSearch).text.toString())
            }
        }

        viewPagerAdapter.add(accountsSearchFragment)
        viewPagerAdapter.add(placesSearchFragment)

        viewPager2.adapter = viewPagerAdapter


        val searchTabLayout = view.findViewById<TabLayout>(R.id.search_tab_layout)
        searchTabLayout.visibility = View.VISIBLE
        TabLayoutMediator(searchTabLayout,viewPager2){ tab, position ->
            viewPager2.setCurrentItem(tab.position,true)

            if(position == 0 ){
                tab.text = "Accounts"
            }
            else{
                tab.text = "Places"
            }

        }.attach()
    }

    override fun onSearchItemClickEvent(historyText: String) {
        val imgr: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val searchEt = view?.findViewById<EditText>(R.id.etSearch)
        searchEt?.clearFocus()
        imgr.hideSoftInputFromWindow(searchEt?.windowToken,0)
        searchEt?.setText(historyText)
        loadSearchViewPager(requireView())
        view?.findViewById<FrameLayout>(R.id.history_layout)?.visibility = View.GONE
        viewModel.deleteSearchHistory(historyText)
        viewModel.addSearchHistory(historyText)
    }


}