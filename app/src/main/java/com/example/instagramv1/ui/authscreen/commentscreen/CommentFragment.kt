package com.example.instagramv1.ui.authscreen.commentscreen

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramv1.R
import com.example.instagramv1.adapters.CommentsRecyclerAdapter
import com.example.instagramv1.adapters.GalleryViewRecyclerAdapter
import com.example.instagramv1.databinding.ActivityCommentBinding
import com.example.instagramv1.databinding.FragmentCommentBinding
import com.example.instagramv1.model.CommentViewData
import com.example.instagramv1.utils.loadImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CommentFragment : Fragment() ,CommentsRecyclerAdapter.onEventListener{

    private val viewModel by viewModels<CommentViewModel>()
    private lateinit var commentBinding: FragmentCommentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        commentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_comment,container,false)

        val sharedPref = requireActivity().getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return null
        val userId = sharedPref.getInt("USER_ID", -1)
        viewModel.userId = userId

        val postId = arguments?.getInt("POST_ID")
        viewModel.postId = postId!!

        commentBinding.imgViewSendIcon.visibility = View.GONE
        commentBinding.imgViewSendIconDisabled.visibility = View.VISIBLE

        commentBinding.addComment.addTextChangedListener {
            if (it.toString().isNotBlank()) {
                commentBinding.imgViewSendIconDisabled.visibility = View.GONE
                commentBinding.imgViewSendIcon.visibility = View.VISIBLE
            } else {
                commentBinding.imgViewSendIcon.visibility = View.GONE
                commentBinding.imgViewSendIconDisabled.visibility = View.VISIBLE
            }
        }




        lifecycleScope.launch {
            val profilePic =  viewModel.getUserProfilePicture()
            //commentBinding.userDp.setImageBitmap(profilePic)
            loadImage(commentBinding.userDp,profilePic)
            Log.d("Special",profilePic.toString())
            viewModel.userImage = profilePic
            commentBinding.executePendingBindings()
            val userName = viewModel.getUserName()
            viewModel.userName = userName
        }

        lifecycleScope.launch {
            viewModel.getPost().observe(requireActivity()) {
                viewModel.postDescription = it.post_description
                viewModel.postImage = it.post_image
                viewModel.postOwnerUserName = it.post_owner_user_name
                viewModel.postOwnerUserId = it.post_owner_id
                viewModel.postOwnerProfilePicture = it.post_owner_profile_picture
                viewModel.postLocation = it.post_location
                viewModel.postCreatedTime = it.post_created_time
                viewModel.postReactionCount = it.post_reaction_count
                viewModel.postCommentsCount = it.post_comments_count
                viewModel.userReacted = it.user_reacted

                if(it.post_description==null || it.post_description.isBlank()){
                    commentBinding.postDescription.visibility = View.GONE
                    commentBinding.commentSectionDivider.visibility = View.GONE
                }

                //commentBinding.postOwnerDp.setImageBitmap(it.post_owner_profile_picture)
                loadImage(commentBinding.postOwnerDp,viewModel.postOwnerProfilePicture)
                commentBinding.commentOwnerName.text = it.post_owner_user_name
                commentBinding.comment.text = it.post_description

                commentBinding.executePendingBindings()

            }
        }

        val commentsRecyclerView = commentBinding.root.findViewById<RecyclerView>(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = object : LinearLayoutManager(requireActivity()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val commentsRecyclerAdapter = CommentsRecyclerAdapter(viewModel)
        commentsRecyclerView.adapter = commentsRecyclerAdapter
        commentsRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        commentsRecyclerAdapter.eventListener = this

        lifecycleScope.launch {
            viewModel.getComments().observe(requireActivity()) {
                commentsRecyclerAdapter.setComments(it)
                commentBinding.pbLoading.visibility = View.GONE
                commentBinding.nestedScrollView.visibility = View.VISIBLE
                commentBinding.addComment.isEnabled = true
            }
        }
        var normalColor = ""
        val nightModeFlags: Int =resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                normalColor = "#000000"
            }
            Configuration.UI_MODE_NIGHT_NO ->{

                normalColor = "#FFFFFF"
            }
        }

        commentBinding.imgViewCloseBtn.setOnClickListener {
            for(view in commentsRecyclerAdapter.deleteCommentView){
                view.setBackgroundColor(Color.parseColor(normalColor))
            }
            commentsRecyclerAdapter.deleteCommentView.clear()
            closeSelectedItems()

        }

        commentBinding.imgViewDeleteBtn.setOnClickListener {
            for(comment in viewModel.selectedComments){
                viewModel.deleteComments.add(comment)
                commentsRecyclerAdapter.removeComment(comment)
            }
            for(view in commentsRecyclerAdapter.deleteCommentView){
                view.setBackgroundColor(Color.parseColor(normalColor))
            }
            commentsRecyclerAdapter.deleteCommentView.clear()
            closeSelectedItems()
            commentsRecyclerAdapter.notifyDataSetChanged()
        }



        commentBinding.imgViewSendIcon.setOnClickListener {
            val commentDesc = commentBinding.addComment.text.toString().trim()
            val commentViewData = CommentViewData(-1,-1, viewModel.userId,viewModel.userName,viewModel.userImage,commentDesc,
                Date(),0,false)
            commentsRecyclerAdapter.commentsList.add(0,commentViewData)
            commentsRecyclerAdapter.notifyDataSetChanged()

            Log.d("Comment Text",commentDesc)
            lifecycleScope.launch{

                viewModel.addAllCommentReaction()
                viewModel.removeAllCommentReaction()
                viewModel.deleteAllComments()
                viewModel.postComment(commentDesc)

            }
            commentBinding.addComment.setText("")





            val dm = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(dm)
            val topOffset: Int = dm.heightPixels - commentBinding.nestedScrollView.measuredHeight

            val loc = IntArray(2)
            commentBinding.commentsRecyclerView.getLocationOnScreen(loc)

            val y = loc[1] - topOffset
            Log.d("Scroll Set","y ="+loc[1])
            commentBinding.nestedScrollView.scrollTo(0,y)
//            if(y>0){
//                commentBinding.nestedScrollView.scrollTo(0,y)
//            }


            hideSoftKeyboard(requireActivity())

        }

        commentBinding.root.findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return commentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun closeSelectedItems(){
        commentBinding.deleteCommentBar.visibility = View.GONE
        commentBinding.editCommentSection.visibility = View.VISIBLE
        commentBinding.imgViewBackBtn.visibility = View.VISIBLE
        viewModel.selectedComments.clear()
    }

    fun hideSoftKeyboard(activity: Activity) {
        Log.d("Comment Section",activity.toString())
        val inputMethodManager = activity.getSystemService(
            AppCompatActivity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            if(activity.currentFocus!=null){
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    0
                )
            }

        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
        viewModel.addAllCommentReaction()
        viewModel.removeAllCommentReaction()
        viewModel.deleteAllComments()
        viewModel.selectedComments.clear()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
    }

    override fun onCommentSelected() {
        commentBinding.deleteCommentBar.visibility = View.VISIBLE
        commentBinding.editCommentSection.visibility = View.GONE
        commentBinding.imgViewBackBtn.visibility = View.GONE
        commentBinding.tvDelete.text = "${viewModel.selectedComments.size} selected"
    }

    override fun onCommentDeselected() {
        commentBinding.tvDelete.text = "${viewModel.selectedComments.size} selected"
        if(viewModel.selectedComments.isEmpty()){
            commentBinding.deleteCommentBar.visibility = View.GONE
            commentBinding.editCommentSection.visibility = View.VISIBLE
            commentBinding.imgViewBackBtn.visibility = View.VISIBLE
        }
    }
}