package com.example.instagramv1.ui.authscreen.commentscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.instagramv1.R
import com.example.instagramv1.adapters.CommentsRecyclerAdapter
import com.example.instagramv1.databinding.ActivityCommentBinding
import com.example.instagramv1.databinding.ActivityPostWithCommentBinding
import com.example.instagramv1.model.CommentViewData
import com.example.instagramv1.model.PostViewData
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.MoreOptionsBottomSheetFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import com.example.instagramv1.utils.DateUtils
import com.example.instagramv1.utils.DoubleClickListener
import com.example.instagramv1.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostWithCommentActivity : AppCompatActivity() {

    private val viewModel by viewModels<CommentViewModel>()
    private lateinit var commentBinding: ActivityPostWithCommentBinding
    private var userSavedState : Boolean = false
    private var isReadMoreAvail : Boolean = false
    private var descReadMoreOn : Boolean = false
    private var readMoreColor : String= "808080"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_with_comment)

        val sharedPref = getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val userId = sharedPref.getInt("USER_ID", -1)
        viewModel.userId = userId

        val postId = intent.getIntExtra("POST_ID", -1)
        viewModel.postId = postId

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
            viewModel.getPost().observe(this@PostWithCommentActivity) {
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

//                if(it.post_description==null || it.post_description.isBlank()){
//                    commentBinding.postDescription.visibility = View.GONE
//                    commentBinding.commentSectionDivider.visibility = View.GONE
//                }

                //commentBinding.postOwnerDp.setImageBitmap(it.post_owner_profile_picture)
                loadImage(commentBinding.postOwnerImage,viewModel.postOwnerProfilePicture)
                loadImage(commentBinding.postImage,viewModel.postImage)
                commentBinding.tvPostOwnerUserName.text = it.post_owner_user_name
                commentBinding.postOwnerNameDesc.text = it.post_owner_user_name
                commentBinding.likesCount.text = it.post_reaction_count.toString()
                commentBinding.commentsCount.text = it.post_comments_count.toString()



                
                val nightModeFlags: Int = resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK
                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> readMoreColor = "D3D3D3"
                    Configuration.UI_MODE_NIGHT_NO -> readMoreColor = "808080"
                }

                commentBinding.createdTime.text = DateUtils.setCreatedTime(it.post_created_time)

                if(it.post_owner_id == userId){
                    commentBinding.moreOptionsBtn.visibility = View.VISIBLE
                }
                else{
                    commentBinding.moreOptionsBtn.visibility = View.GONE
                }

                commentBinding.moreOptionsBtn.setOnClickListener {
                    openMoreOptionDialogFragment(postId)
                }

                commentBinding.descLayout.setOnClickListener {
                    if(isReadMoreAvail){
                        if(descReadMoreOn){
                            commentBinding.postDescriptionRmTv.visibility = View.GONE
                            commentBinding.postDescriptionTv.visibility = View.VISIBLE
                            descReadMoreOn = false

                        }
                        else{
                            commentBinding.postDescriptionRmTv.visibility = View.VISIBLE
                            commentBinding.postDescriptionTv.visibility = View.GONE
                            descReadMoreOn = true
                        }
                    }
                }

                getSavedStatus(postId)
                commentBinding.postDescriptionRmTv.visibility = View.GONE
                commentBinding.postDescriptionTv.visibility = View.GONE

                if(it.post_description == null){
                    //commentBinding.postDescriptionSection.visibility = View.GONE
                    commentBinding.descLayout.visibility = View.GONE
                    
                }
                else{
                    commentBinding.descLayout.visibility = View.VISIBLE
                    
                    val desc = it.post_description
                    commentBinding.postDescriptionTv.text = desc
                    var stringNew = ""
                    if(desc.length>40 && desc.contains("\n")){
                        isReadMoreAvail = true
                        var count = 0
                        Log.d("ReadMore",desc)
                        for (s in desc) {
                            Log.d("ReadMore",s.toString())
                            if (s == '\n') {
                                break
                            }
                            count += 1
                        }
                        if(count > 40){
                            stringNew = desc.substring(0,40).plus("<font color='#$readMoreColor'>...more</font>")

                            commentBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,40)+"<font color='#$readMoreColor'>...more</font>")
                        }
                        else{
                            stringNew = desc.substring(0,count).plus("<font color='#$readMoreColor'>...more</font>")
                            commentBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,count)+"<font color='#$readMoreColor'>...more</font>")
                        }
                        commentBinding.postDescriptionRmTv.visibility = View.VISIBLE
                        commentBinding.postDescriptionTv.visibility = View.GONE
                        // commentBinding.postDescriptionRmTv.text = stringNew
                        descReadMoreOn = true
                    }
                    else if(desc.length<40 && desc.contains("\n")){
                        isReadMoreAvail = true
                        var count = 0
                        for (s in desc) {
                            if (s == '\n') {
                                break
                            }
                            count += 1
                        }

                        stringNew = desc.substring(0,count).plus("<font color='#$readMoreColor'>...more</font>")
                        commentBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,count)+"<font color='#$readMoreColor'>...more</font>")
                        commentBinding.postDescriptionRmTv.visibility = View.VISIBLE
                        commentBinding.postDescriptionTv.visibility = View.GONE
                        //commentBinding.postDescriptionRmTv.text = stringNew
                        descReadMoreOn = true
                    }

                    else if(desc.length>40 && !desc.contains("\n")){
                        isReadMoreAvail = true
                        stringNew = desc.substring(0,40).plus("<font color='#$readMoreColor'>...more</font>")
                        commentBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,40)+"<font color='#$readMoreColor'>...more</font>")
                        commentBinding.postDescriptionRmTv.visibility = View.VISIBLE
                        commentBinding.postDescriptionTv.visibility = View.GONE
                        ///commentBinding.postDescriptionRmTv.text = stringNew
                        descReadMoreOn = true

                    }

                    else{
                        commentBinding.postDescriptionRmTv.visibility = View.GONE
                        commentBinding.postDescriptionTv.visibility = View.VISIBLE
                        commentBinding.postDescriptionTv.text = it.post_description
                    }
                }

                if(it.post_location == null){
                    commentBinding.tvLocation.visibility = View.GONE
                }
                else{
                    commentBinding.tvLocation.text = viewModel.postLocation
                    commentBinding.tvLocation.visibility = View.VISIBLE
                }
                if(it.user_reacted){
                    commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                }
                else{
                    commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.favorite,null))
                }
                commentBinding.likeBtnCardView.setOnClickListener {view -> 
                    if(it.user_reacted){
                        it.user_reacted = false
                        commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.favorite,null))
                        it.post_reaction_count--
                        commentBinding.likesCount.text = it.post_reaction_count.toString()
                        viewModel.removeReactions.add(it.post_id)
                        viewModel.addReactions.remove(it.post_id)
                    }
                    else{
                        it.user_reacted = true
                        commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                        it.post_reaction_count++
                        commentBinding.likesCount.text = it.post_reaction_count.toString()
                        viewModel.addReactions.add(it.post_id)
                        viewModel.removeReactions.remove(it.post_id)
                    }

//                if(it.user_reacted){
//                   // commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.favorite,null))
//                    //viewModel.removeReaction(it.post_id)
//                    viewModel.removeReactions.add(it.post_id)
//                    viewModel.addReactions.remove(it.post_id)
//                }
//                else{
//                   // commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
//                    //viewModel.addReaction(it.post_id)
//                    viewModel.addReactions.add(it.post_id)
//                    viewModel.removeReactions.remove(it.post_id)
//                }

                }



                //commentBinding.comment.text = it.post_description

                val cardView = commentBinding.postCardView
                val heart= commentBinding.heart
                val drawable = heart?.drawable



                commentBinding.postCardView.setOnClickListener(object : DoubleClickListener(){

                    override fun onDoubleClick(v: View?) {
                        heart?.alpha = 0.70f
                        if(drawable is AnimatedVectorDrawableCompat){
                            val avd = drawable
                            avd.start()
                        }
                        else if(drawable is AnimatedVectorDrawable){
                            val avd = drawable
                            avd.start()
                        }
                        if(!it.user_reacted){
                            commentBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                            viewModel.addReactions.add(it.post_id)
                            viewModel.removeReactions.remove(it.post_id)
                            it.post_reaction_count++
                            commentBinding.likesCount.text = it.post_reaction_count.toString()
                            it.user_reacted = true
                        }


                    }
                })

                commentBinding.postOwnerDetails.setOnClickListener {
                   // openProfile(it.post_owner_id)
                }

                commentBinding.tvPostOwnerUserName.setOnClickListener {
                    //openProfile(it.post_owner_id)
                }

                commentBinding.cardViewPostOwnerImage.setOnClickListener {
                    //openProfile(it.post_owner_id)
                }

                commentBinding.commentBtnCardView.setOnClickListener {

                }

               

                commentBinding.executePendingBindings()

            }
        }





        val commentsRecyclerView = findViewById<RecyclerView>(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val commentsRecyclerAdapter = CommentsRecyclerAdapter(viewModel)
        commentsRecyclerView.adapter = commentsRecyclerAdapter
        commentsRecyclerAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        lifecycleScope.launch {
            viewModel.getComments().observe(this@PostWithCommentActivity) {
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
            val commentViewData = CommentViewData(-1,-1, viewModel.userId,viewModel.userName,viewModel.userImage,commentDesc,0,false)
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
            this.windowManager.defaultDisplay.getMetrics(dm)
            val topOffset: Int = dm.heightPixels - commentBinding.nestedScrollView.measuredHeight

            val loc = IntArray(2)
            commentBinding.commentsRecyclerView.getLocationOnScreen(loc)

            val y = loc[1] - topOffset
            Log.d("Scroll Set","y ="+loc[1])
            commentBinding.nestedScrollView.scrollTo(0,y)
//            if(y>0){
//                commentBinding.nestedScrollView.scrollTo(0,y)
//            }


            hideSoftKeyboard(this)

        }

        findViewById<ImageView>(R.id.imgViewBackBtn).setOnClickListener {
            finish()
        }
    }

    private fun getSavedStatus(postId : Int){
        
        lifecycleScope.launch {
            viewModel.getSavedStatus(postId).observe(this@PostWithCommentActivity){
                userSavedState = it
                if(it){
                    commentBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.bookmarkfilled,null))
                    commentBinding.saveTextView.text = "Saved"
                }
                else{
                    commentBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.bookmark,null))
                    commentBinding.saveTextView.text = "Save"
                }
            }
        }

        commentBinding.saveBtnCardView.setOnClickListener {
            if(userSavedState){
                userSavedState = false
                commentBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.bookmark,null))
                commentBinding.saveTextView.text = "Save"
                viewModel.unSavePost(postId)
            }
            else{
                userSavedState = true
                commentBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(commentBinding.imgViewLikeBtn.resources,R.drawable.bookmarkfilled,null))
                commentBinding.saveTextView.text = "Saved"
                viewModel.savePost(postId)
            }
        }


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
            INPUT_METHOD_SERVICE
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
        viewModel.addAllCommentReaction()
        viewModel.removeAllCommentReaction()
        viewModel.deleteAllComments()
        viewModel.addAllReaction()
        viewModel.removeAllReaction()
        viewModel.selectedComments.clear()
    }

    private fun openMoreOptionDialogFragment(postId : Int){
        viewModel.addAllReaction()
        viewModel.removeAllReaction()
        val moreOptionsBottomSheetFragment = MoreOptionsBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putInt("POST_ID",postId)
            }
        }
        moreOptionsBottomSheetFragment.show(supportFragmentManager,"bottomsheet")
    }



}