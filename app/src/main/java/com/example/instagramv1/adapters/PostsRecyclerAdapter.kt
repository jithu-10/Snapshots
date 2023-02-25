package com.example.instagramv1.adapters

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.instagramv1.R
import com.example.instagramv1.databinding.PostViewBinding
import com.example.instagramv1.model.FilterOptions
import com.example.instagramv1.model.PostViewData
import com.example.instagramv1.ui.commentscreen.CommentActivity
import com.example.instagramv1.ui.mainscreen.PostViewModel
import com.example.instagramv1.ui.mainscreen.othersprofilescreen.OthersProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.MoreOptionsBottomSheetFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.ProfileFragment
import com.example.instagramv1.ui.mainscreen.profilescreen.SavedPostsFragment
import com.example.instagramv1.utils.DoubleClickListener
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PostsRecyclerAdapter(val postViewModel: PostViewModel,val eventListener: EventListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val profileFragment = ProfileFragment()
    private var posts : List<PostViewData> = mutableListOf()
    private var isEndPostViewNeeded = false

    fun setPosts(list : List<PostViewData>,endView : Boolean){
        posts = list
        isEndPostViewNeeded = endView

        notifyDataSetChanged()
    }

    fun setPosts(list : List<PostViewData>,endView: Boolean,filterOptions: FilterOptions){

        if(filterOptions == FilterOptions.SORT_BY_DATE_DESC){
            val posts = sortByDateDescending(list)
            setPosts(posts,endView)

        }

        else if(filterOptions == FilterOptions.SORT_BY_DATE_ASC){
            val posts = sortByDateAscending(list)
            setPosts(posts,endView)
        }

        else if(filterOptions == FilterOptions.MOST_LIKED){
            val posts = sortByLikes(list)
            setPosts(posts,endView)
        }

        else if(filterOptions == FilterOptions.MOST_COMMENTED){
            val posts = sortByMostComments(list)
            setPosts(posts,endView)
        }
    }

    fun setPosts(endView: Boolean,filterOptions: FilterOptions){
        val list = posts
        if(list.isEmpty()){
            return
        }
        Log.d("Adapter Check","Not Null : "+list.toString())
        if(filterOptions == FilterOptions.SORT_BY_DATE_DESC){
            val posts = sortByDateDescending(list)
            setPosts(posts,endView)

        }

        else if(filterOptions == FilterOptions.SORT_BY_DATE_ASC){
            val posts = sortByDateAscending(list)
            setPosts(posts,endView)
        }

        else if(filterOptions == FilterOptions.MOST_LIKED){
            val posts = sortByLikes(list)
            setPosts(posts,endView)
        }

        else if(filterOptions == FilterOptions.MOST_COMMENTED){
            val posts = sortByMostComments(list)
            setPosts(posts,endView)
        }
    }

    private fun sortByMostComments(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_comments_count
        }
        return newList
    }

    private fun sortByLikes(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_reaction_count
        }
        return newList
    }

    private fun sortByDateAscending(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedBy {
            it.post_id
        }
        return newList
    }

    private fun sortByDateDescending(list : List<PostViewData>) : List<PostViewData>{
        val newList = list.sortedByDescending {
            it.post_id
        }
        return newList
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val postViewBinding : PostViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.post_view, parent, false)
        return PostsViewHolder(postViewBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = posts[position]

        (holder as PostsViewHolder).bind(post)

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }




    inner class PostsViewHolder(private val postViewBinding: PostViewBinding) : RecyclerView.ViewHolder(postViewBinding.root) {

        private val likeBtn = postViewBinding.imgViewLikeBtn
        private val saveBtn = postViewBinding.imgViewSaveBtn
        private val commentBtn = postViewBinding.imgViewCommentBtn
        private val viewAllCommentsBtn = postViewBinding.tvViewComments


        private var userSavedState : Boolean = false
        private var isReadMoreAvail : Boolean = false
        private var descReadMoreOn : Boolean = false
        private var readMoreColor : String= "808080"



        //val descriptionTv = postViewBinding.postDescriptionView


        private fun openMoreOptionDialogFragment(postId : Int){
            postViewModel.addAllReaction()
            postViewModel.removeAllReaction()
            val moreOptionsBottomSheetFragment = MoreOptionsBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt("POST_ID",postId)
                }
            }
            moreOptionsBottomSheetFragment.show((FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager,"bottomsheet")
        }

        fun bind(post : PostViewData){
            val nightModeFlags: Int = postViewBinding.root.getResources().getConfiguration().uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> readMoreColor = "D3D3D3"
                Configuration.UI_MODE_NIGHT_NO -> readMoreColor = "808080"
            }
            postViewBinding.model = post

            if(post.post_owner_id == postViewModel.userId){
                postViewBinding.moreOptionsBtn.visibility = View.VISIBLE
            }
            else{
                postViewBinding.moreOptionsBtn.visibility = View.GONE
            }

            postViewBinding.moreOptionsBtn.setOnClickListener {
                openMoreOptionDialogFragment(post.post_id)
            }

            postViewBinding.descLayout.setOnClickListener {
                if(isReadMoreAvail){
                    if(descReadMoreOn){
                        postViewBinding.postDescriptionRmTv.visibility = View.GONE
                        postViewBinding.postDescriptionTv.visibility = View.VISIBLE
                        descReadMoreOn = false

                    }
                    else{
                        postViewBinding.postDescriptionRmTv.visibility = View.VISIBLE
                        postViewBinding.postDescriptionTv.visibility = View.GONE
                        descReadMoreOn = true
                    }
                }
            }

            getSavedStatus(post)
            postViewBinding.postDescriptionRmTv.visibility = View.GONE
            postViewBinding.postDescriptionTv.visibility = View.GONE



//            postViewBinding.postDescriptionRmTv?.setOnClickListener {
//                postViewBinding.postDescriptionRmTv.visibility = View.GONE
//                postViewBinding.postDescriptionTv?.visibility = View.VISIBLE
//
//            }
//
//            postViewBinding.postDescriptionTv?.setOnClickListener {
//                if(isReadMoreAvail){
//                    postViewBinding.postDescriptionRmTv?.visibility = View.VISIBLE
//                    postViewBinding.postDescriptionTv.visibility = View.GONE
//                }
//            }

            if(post.post_description == null){
                //postViewBinding.postDescriptionSection.visibility = View.GONE
                postViewBinding.descLayout.visibility = View.GONE
                postViewBinding.tvViewComments.visibility = View.GONE
            }
            else{
                val desc = post.post_description
                postViewBinding.postDescriptionTv.text = desc
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

                        postViewBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,40)+"<font color='#$readMoreColor'>...more</font>")
                    }
                    else{
                        stringNew = desc.substring(0,count).plus("<font color='#$readMoreColor'>...more</font>")
                        postViewBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,count)+"<font color='#$readMoreColor'>...more</font>")
                    }
                    postViewBinding.postDescriptionRmTv.visibility = View.VISIBLE
                    postViewBinding.postDescriptionTv.visibility = View.GONE
                    // postViewBinding.postDescriptionRmTv.text = stringNew
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
                    postViewBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,count)+"<font color='#$readMoreColor'>...more</font>")
                    postViewBinding.postDescriptionRmTv.visibility = View.VISIBLE
                    postViewBinding.postDescriptionTv.visibility = View.GONE
                    //postViewBinding.postDescriptionRmTv.text = stringNew
                    descReadMoreOn = true
                }

                else if(desc.length>40 && !desc.contains("\n")){
                    isReadMoreAvail = true
                    stringNew = desc.substring(0,40).plus("<font color='#$readMoreColor'>...more</font>")
                    postViewBinding.postDescriptionRmTv.text = Html.fromHtml(desc.substring(0,40)+"<font color='#$readMoreColor'>...more</font>")
                    postViewBinding.postDescriptionRmTv.visibility = View.VISIBLE
                    postViewBinding.postDescriptionTv.visibility = View.GONE
                    ///postViewBinding.postDescriptionRmTv.text = stringNew
                    descReadMoreOn = true

                }

                else{
                    postViewBinding.postDescriptionRmTv.visibility = View.GONE
                    postViewBinding.postDescriptionTv.visibility = View.VISIBLE
                    postViewBinding.postDescriptionTv.text = post.post_description
                }
            }

            if(post.post_location == null){
                postViewBinding.tvLocation.visibility = View.GONE
            }
            if(post.user_reacted){
                postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
            }
            else{
                postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_border_24,null))
            }
            likeBtn.setOnClickListener {
                if(post.user_reacted){
                    post.user_reacted = false
                    postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_border_24,null))
                    post.post_reaction_count--
                    postViewBinding.likesCount.text = post.post_reaction_count.toString()
                    postViewModel.removeReactions.add(post.post_id)
                    postViewModel.addReactions.remove(post.post_id)
                }
                else{
                    post.user_reacted = true
                    postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                    post.post_reaction_count++
                    postViewBinding.likesCount.text = post.post_reaction_count.toString()
                    postViewModel.addReactions.add(post.post_id)
                    postViewModel.removeReactions.remove(post.post_id)
                }

//                if(post.user_reacted){
//                   // postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_border_24,null))
//                    //postViewModel.removeReaction(post.post_id)
//                    postViewModel.removeReactions.add(post.post_id)
//                    postViewModel.addReactions.remove(post.post_id)
//                }
//                else{
//                   // postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
//                    //postViewModel.addReaction(post.post_id)
//                    postViewModel.addReactions.add(post.post_id)
//                    postViewModel.removeReactions.remove(post.post_id)
//                }

            }



            val cardView = postViewBinding.postCardView
            val heart= postViewBinding.heart
            val drawable = heart?.drawable



            postViewBinding.postCardView.setOnClickListener(object : DoubleClickListener(){

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
                    if(!post.user_reacted){
                        postViewBinding.imgViewLikeBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_favorite_24,null))
                        postViewModel.addReactions.add(post.post_id)
                        postViewModel.removeReactions.remove(post.post_id)
                        post.post_reaction_count++
                        postViewBinding.likesCount.text = post.post_reaction_count.toString()
                        post.user_reacted = true
                    }


                }
            })

            postViewBinding.tvPostOwnerUserName.setOnClickListener {
                openProfile(post.post_owner_id)
            }

            postViewBinding.cardViewPostOwnerImage.setOnClickListener {
                openProfile(post.post_owner_id)
            }

            commentBtn.setOnClickListener {
                val intent = Intent(it.context, CommentActivity::class.java).apply {
                    putExtra("POST_ID",post.post_id)
                }
                it.context.startActivity(intent)
            }

            viewAllCommentsBtn.setOnClickListener {
                val intent = Intent(it.context, CommentActivity::class.java).apply {
                    putExtra("POST_ID",post.post_id)
                }
                it.context.startActivity(intent)
            }

            postViewBinding.executePendingBindings()



        }



        private fun getSavedStatus(post : PostViewData){
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                postViewModel.getSavedStatus(post.post_id).observe((FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity)){
                    userSavedState = it
                    if(it){
                        postViewBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_bookmark_24,null))
                        postViewBinding.saveTextView.text = "Saved"
                    }
                    else{
                        postViewBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_outline_bookmark_border_24,null))
                        postViewBinding.saveTextView.text = "Save"
                    }
                }
            }

            saveBtn.setOnClickListener {
                if(userSavedState){
                    userSavedState = false
                    postViewBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_outline_bookmark_border_24,null))
                    postViewBinding.saveTextView.text = "Save"
                    postViewModel.unSavePost(post.post_id)
                }
                else{
                    userSavedState = true
                    postViewBinding.imgViewSaveBtn.setImageDrawable(ResourcesCompat.getDrawable(postViewBinding.imgViewLikeBtn.resources,R.drawable.ic_baseline_bookmark_24,null))
                    postViewBinding.saveTextView.text = "Saved"
                    postViewModel.savePost(post.post_id)
                }
            }


        }

        private fun openProfile(post_owner_id : Int){
            val userId = postViewModel.userId

            if(userId == post_owner_id){

                (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,ProfileFragment(),"PROFILE_FRAGMENT")
                    addToBackStack("PROFILE_FRAGMENT")
                    commit()
                }


//                if(isFragmentInBackstack((FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager,"PROFILE_FRAGMENT")){
//
//                    (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.frame_page,profileFragment,"PROFILE_FRAGMENT")
//                        //addToBackStack("PROFILE_FRAGMENT")
//                        commit()
//                    }
//
//                    val frg = (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.findFragmentByTag("PROFILE_FRAGMENT")
//                    val ft: FragmentTransaction = (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction()
//                    ft.detach(frg!!)
//                    ft.attach(frg!!)
//                    ft.commit()
//                }
//                else{
//                    (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
//                        replace(R.id.frame_page,profileFragment,"PROFILE_FRAGMENT")
//                        addToBackStack("PROFILE_FRAGMENT")
//                        commit()
//                    }
//                }

            }

            else{
                val othersProfileFragment = OthersProfileFragment().apply {
                    arguments = Bundle().apply {
                        putInt("OTHER_USER_ID",post_owner_id)
                    }
                }
                //othersProfileFragment.arguments?.putInt("OTHER_USER_ID",post_owner_id)

                (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_page,othersProfileFragment)
                    addToBackStack(null)
                    commit()
                }
            }

//            if(eventListener!=null){
//                if(eventListener is SavedPostsFragment){
//                    if(userId!=post_owner_id){
//                        val othersProfileFragment = OthersProfileFragment().apply {
//                            arguments = Bundle().apply {
//                                putInt("OTHER_USER_ID",post_owner_id)
//                            }
//                        }
//                        //othersProfileFragment.arguments?.putInt("OTHER_USER_ID",post_owner_id)
//
//                        (FragmentComponentManager.findActivity(postViewBinding.root.context) as AppCompatActivity).supportFragmentManager.beginTransaction().apply {
//                            replace(R.id.frame_page,othersProfileFragment)
//                            addToBackStack(null)
//                            commit()
//                        }
//                    }
//
//                }
//
//            }
//            else{
//
//            }




        }


        private fun isFragmentInBackstack(fragmentManager: FragmentManager, fragmentTagName: String): Boolean {
            for (entry in 0 until fragmentManager.backStackEntryCount) {
                if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).name) {
                    return true
                }
            }
            return false
        }



    }


    interface EventListener{
        fun onPostItemClickEvent()
    }


}