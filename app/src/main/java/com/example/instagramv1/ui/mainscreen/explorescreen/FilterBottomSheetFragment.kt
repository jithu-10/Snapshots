package com.example.instagramv1.ui.mainscreen.explorescreen


import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.instagramv1.R
import com.example.instagramv1.databinding.BottomSheetFilterBinding
import com.example.instagramv1.model.FilterOptions
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class FilterBottomSheetFragment  : BottomSheetDialogFragment(){

    private val viewModel by activityViewModels<PostViewModel>()
    private var changed = false
    override fun onStart() {
        super.onStart()
        changed = false
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private lateinit var bottomSheetFilterBinding : BottomSheetFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bottomSheetFilterBinding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_filter,container,false)

        return bottomSheetFilterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup = bottomSheetFilterBinding.filterRadioGroup


        bottomSheetFilterBinding.sortByDateDescRadioBtn.setOnClickListener {
            if(viewModel.sort!=FilterOptions.SORT_BY_DATE_DESC){
                viewModel.sortedOps.value = FilterOptions.SORT_BY_DATE_DESC
                viewModel.sort = FilterOptions.SORT_BY_DATE_DESC
                viewModel.changed.value = true
            }
            dismiss()

        }

        bottomSheetFilterBinding.sortByDateAscRadioBtn.setOnClickListener {
            if(viewModel.sort!=FilterOptions.SORT_BY_DATE_ASC){
                viewModel.sortedOps.value = FilterOptions.SORT_BY_DATE_ASC
                viewModel.sort = FilterOptions.SORT_BY_DATE_ASC
                viewModel.changed.value = true
            }
            dismiss()

        }

        bottomSheetFilterBinding.sortByMostLikesRadioBtn.setOnClickListener {
            if(viewModel.sort!=FilterOptions.MOST_LIKED){
                viewModel.sortedOps.value = FilterOptions.MOST_LIKED
                viewModel.sort = FilterOptions.MOST_LIKED
                viewModel.changed.value = true
            }
            dismiss()

        }

        bottomSheetFilterBinding.sortByMostCommentedRadioBtn.setOnClickListener {
            if(viewModel.sort!=FilterOptions.MOST_COMMENTED){
                viewModel.sortedOps.value = FilterOptions.MOST_COMMENTED
                viewModel.sort = FilterOptions.MOST_COMMENTED
                viewModel.changed.value = true
            }
            dismiss()

        }
//        radioGroup.setOnCheckedChangeListener { _, checkedId ->
//
//            if(checkedId == R.id.sortByDateDescRadioBtn){
//                if(viewModel.sort!=FilterOptions.SORT_BY_DATE_DESC){
//                    viewModel.changed.value = true
//                }
//                viewModel.sortedOps.value = FilterOptions.SORT_BY_DATE_DESC
//                viewModel.sort = FilterOptions.SORT_BY_DATE_DESC
//
//            }
//            else if(checkedId == R.id.sortByDateAscRadioBtn){
//                if(viewModel.sort!=FilterOptions.SORT_BY_DATE_ASC){
//                    viewModel.changed.value = true
//                }
//                viewModel.sortedOps.value = FilterOptions.SORT_BY_DATE_ASC
//                viewModel.sort = FilterOptions.SORT_BY_DATE_ASC
//
//            }
//            else if(checkedId == R.id.sortByMostLikesRadioBtn){
//                if(viewModel.sort!=FilterOptions.MOST_LIKED){
//                    viewModel.changed.value = true
//                }
//                viewModel.sortedOps.value = FilterOptions.MOST_LIKED
//                viewModel.sort = FilterOptions.MOST_LIKED
//
//            }
//            else if(checkedId == R.id.sortByMostCommentedRadioBtn){
//                if(viewModel.sort!=FilterOptions.MOST_COMMENTED){
//                    viewModel.changed.value = true
//                }
//                viewModel.sortedOps.value = FilterOptions.MOST_COMMENTED
//                viewModel.sort = FilterOptions.MOST_COMMENTED
//
//            }


        if(viewModel.sort == FilterOptions.SORT_BY_DATE_DESC){
            radioGroup.check(R.id.sortByDateDescRadioBtn)
        }
        else if(viewModel.sort == FilterOptions.SORT_BY_DATE_ASC){
            radioGroup.check(R.id.sortByDateAscRadioBtn)
        }
        else if(viewModel.sort == FilterOptions.MOST_LIKED){
            radioGroup.check(R.id.sortByMostLikesRadioBtn)
        }
        else if(viewModel.sort == FilterOptions.MOST_COMMENTED){
            radioGroup.check(R.id.sortByMostCommentedRadioBtn)
        }

        }








//        viewModel.sortedOps.observe(viewLifecycleOwner){
//            if(it == FilterOptions.SORT_BY_DATE_DESC){
//                radioGroup.check(R.id.sortByDateDescRadioBtn)
//            }
//            else if(it == FilterOptions.SORT_BY_DATE_ASC){
//                radioGroup.check(R.id.sortByDateAscRadioBtn)
//            }
//            else if(it == FilterOptions.MOST_LIKED){
//                radioGroup.check(R.id.sortByMostLikesRadioBtn)
//            }
//            else if(it == FilterOptions.MOST_COMMENTED){
//                radioGroup.check(R.id.sortByMostCommentedRadioBtn)
//            }
//
//        }


    private fun refreshFragment() {
        parentFragmentManager.beginTransaction().replace(R.id.frame_page, ExploreFragment()).commit()
    }



    override fun onPause() {
        super.onPause()
        if(changed){
            //refreshFragment()
        }
        viewModel.addAllReaction()
        viewModel.removeAllReaction()

    }



    }



//    override fun onDismiss(dialog: DialogInterface) {
//        super.onDismiss(dialog)
//        if(changed){
//            refreshFragment()
//        }
//
//    }

