package com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentEditProfileBinding
import com.example.instagramv1.ui.addpostscreen.GetPhotoDialogFragment
import com.example.instagramv1.ui.addpostscreen.PermissionNeededDialogFragment
import com.example.instagramv1.ui.mainscreen.MainActivity
import com.example.instagramv1.ui.mainscreen.profilescreen.editprofilescreen.EditProfileViewModel
import com.example.instagramv1.utils.ValidationErrorMessage
import com.example.instagramv1.utils.Validators
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel by activityViewModels<EditProfileViewModel>()
    private lateinit var editProfileBinding : FragmentEditProfileBinding
    private var updated= false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        editProfileBinding =  DataBindingUtil.inflate(inflater,
            R.layout.fragment_edit_profile, container, false)

        editProfileBinding.editProfileViewModel = viewModel
        fetchData()

        return editProfileBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        editProfileBinding.tvChangePicture.setOnClickListener {
            //startCropFragment()
            getPermission()
            if(checkForPermission()){
                startGetPhotoDialogFragment()
            }

        }

        editProfileBinding.editProfilePicCardView.setOnClickListener {
            //startCropFragment()
            getPermission()
            if(checkForPermission()){
                startGetPhotoDialogFragment()
            }
        }

        editProfileBinding.backBtn.setOnClickListener {
            requireActivity().finish()
        }

        setUpListeners()


    }



    private fun getPermission(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE_READ_PERMISSION
                );

            }
        } else{
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_PERMISSION
                );

            }
        }




    }

    private fun checkForPermission() : Boolean{
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            return ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else{
            return ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {


        if(requestCode == REQUEST_CODE_READ_PERMISSION){
            if(grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startGetPhotoDialogFragment()

                //return
            }
            else{
                openPermissionNeededDialog()
            }
        }
    }

    fun openPermissionNeededDialog(){
        val permissionNeededDialogFragment = PermissionNeededDialogFragment()
        permissionNeededDialogFragment.show(parentFragmentManager,"permissiondialog")
    }

    private fun startGetPhotoDialogFragment(){
        val metrics = resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        viewModel.removePictureOption = false
        val getPhotoDialogFragment = GetProfilePhotoDialogFragment()
        getPhotoDialogFragment.show(parentFragmentManager,"dialog")
        getPhotoDialogFragment.dialog?.window?.setLayout((6 * width)/7, (4 * height)/5);
    }

    private fun addTextWatcher(){
        editProfileBinding.userNameEt.addTextChangedListener(textWatcher)
        editProfileBinding.fullNameEt.addTextChangedListener(textWatcher)
        editProfileBinding.phoneEt.addTextChangedListener(textWatcher)
        editProfileBinding.emailEt.addTextChangedListener(textWatcher)

    }

    private val textWatcher : TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            editProfileBinding.btnUpdate.isEnabled = !(editProfileBinding.userNameEt.text!!.isBlank()  ||
                    editProfileBinding.fullNameEt.text!!.isBlank() ||
                    editProfileBinding.emailEt.text!!.isBlank()  ||
                    editProfileBinding.phoneEt.text!!.isBlank())
        }

    }

    private fun setUpListeners(){
        userNameInputTextOnFocusListener()
        fullNameInputTextOnFocusListener()
        emailInputTextOnFocusListener()
        phoneInputTextOnFocusListener()
        addTextWatcher()

        editProfileBinding.btnUpdate.setOnClickListener {
            editProfileBinding.fullNameEt.clearFocus()
            editProfileBinding.userNameEt.clearFocus()
            editProfileBinding.phoneEt.clearFocus()
            editProfileBinding.emailEt.clearFocus()
            updateData()

        }
    }

    private fun updateData(){

        lifecycleScope.launch{
            if(completeValidation()){
                updated = true
                viewModel.updateUserEditableData()
                requireActivity().finish()
            }

        }

    }

    private suspend fun completeValidation() : Boolean{
        var filled = true
        val fullNameValidation = Validators.validateFullName(editProfileBinding.fullNameEt.text.toString())
        if(fullNameValidation != null){
            filled = false
            editProfileBinding.etFullName.isErrorEnabled = true
            editProfileBinding.etFullName.error = fullNameValidation.message
        }
        val userNameValidation = Validators.validateUserName(editProfileBinding.userNameEt.text.toString())
        if(userNameValidation != null){
            filled = false
            editProfileBinding.etUserName.isErrorEnabled = true
            editProfileBinding.etUserName.error = userNameValidation.message
        }
        else if(viewModel.isUserNameAlreadyExist()){
            filled = false
            editProfileBinding.etUserName.isErrorEnabled = true
            editProfileBinding.etUserName.error = ValidationErrorMessage.USER_NAME_ALREADY_EXIST.message
        }

        val phoneValidation = Validators.validatePhone(editProfileBinding.phoneEt.text.toString())
        if(phoneValidation != null){
            filled = false
            editProfileBinding.etPhone.isErrorEnabled = true
            editProfileBinding.etPhone.error = phoneValidation.message
        }
        else if(viewModel.isPhoneAlreadyExist()){
            filled = false
            editProfileBinding.etPhone.isErrorEnabled = true
            editProfileBinding.etPhone.error = ValidationErrorMessage.PHONE_ALREADY_EXIST.message
        }

        val emailValidation = Validators.validateEmail(editProfileBinding.emailEt.text.toString())
        if(emailValidation != null){
            filled = false
            editProfileBinding.etEmail.isErrorEnabled = true
            editProfileBinding.etEmail.error = emailValidation.message
        }
        else if(viewModel.isEmailAlreadyExist()){
            filled = false
            editProfileBinding.etEmail.isErrorEnabled = true
            editProfileBinding.etEmail.error = ValidationErrorMessage.EMAIL_ALREADY_EXIST.message
        }


        return filled
    }


    override fun onResume() {
        super.onResume()
        editProfileBinding.invalidateAll()
    }

    private fun fetchData(){

        lifecycleScope.launch{
            val userData = viewModel.getUserEditableData()
            userData.observe(requireActivity()){

                if(!updated){
                    if(viewModel.profilePicture==null){
                        viewModel.profilePicture = userData.value?.profile_picture
                    }
                    if(viewModel.email==null){
                        viewModel.email  = userData.value?.email
                        viewModel.previousEmail = userData.value?.email
                    }
                    if(viewModel.phone==null){
                        viewModel.phone = userData.value?.phone
                        viewModel.previousPhone = userData.value?.phone
                    }
                    if(viewModel.userName==null){
                        viewModel.userName = userData.value?.user_name
                        viewModel.previousUserName = userData.value?.user_name
                    }
                    if(viewModel.fullName==null){
                        viewModel.fullName = userData.value?.full_name
                    }
                    editProfileBinding.invalidateAll()
                }


            }




        }

        viewModel.profilePictureChangedLiveData.observe(requireActivity()){

            if(it>0){
                editProfileBinding.invalidateAll()
            }
        }


    }

    private fun startCropFragment(){
        parentFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            replace(R.id.edit_profile_frame,ProfilePictureCropFragment())
            commit()
        }
    }


    private fun validateName(): String? {
        val error = Validators.validateFullName(editProfileBinding.fullNameEt.text.toString())
        if(error == ValidationErrorMessage.NAME_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateUserName(): String? {
        val error = Validators.validateUserName(editProfileBinding.userNameEt.text.toString())
        if(error == ValidationErrorMessage.USER_NAME_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validateEmail(): String?{
        val error = Validators.validateEmail(editProfileBinding.emailEt.text.toString())
        if(error == ValidationErrorMessage.EMAIL_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun validatePhone(): String?{
        val error =  Validators.validatePhone(editProfileBinding.phoneEt.text.toString())
        if(error == ValidationErrorMessage.PHONE_NOT_GIVEN){
            return null
        }
        return error?.message
    }

    private fun userNameInputTextOnFocusListener() {
        editProfileBinding.userNameEt.setOnFocusChangeListener { _ , focused ->
            if (!focused) {
                val validation = validateUserName()
                if(validation!=null){
                    editProfileBinding.etUserName.isErrorEnabled = true
                    editProfileBinding.etUserName.error = validation
                }
                else{
                    editProfileBinding.etUserName.isErrorEnabled  = false
                }
            }
            else{
                editProfileBinding.etUserName.isErrorEnabled = false
            }
        }
    }

    private fun fullNameInputTextOnFocusListener() {
        editProfileBinding.fullNameEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validateName()
                if(validation!=null){
                    editProfileBinding.etFullName.isErrorEnabled = true
                    editProfileBinding.etFullName.error = validation
                }
                else{
                    editProfileBinding.etFullName.isErrorEnabled  = false
                }
            }
            else{
                editProfileBinding.etFullName.isErrorEnabled = false
            }
        }
    }

    private fun emailInputTextOnFocusListener() {
        editProfileBinding.emailEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validateEmail()
                if(validation!=null){
                    editProfileBinding.etEmail.isErrorEnabled = true
                    editProfileBinding.etEmail.error = validation
                }
                else{
                    editProfileBinding.etEmail.isErrorEnabled  = false
                }
            }
            else{
                editProfileBinding.etEmail.isErrorEnabled = false
            }
        }
    }

    private fun phoneInputTextOnFocusListener() {
        editProfileBinding.phoneEt.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val validation = validatePhone()
                if(validation!=null){
                    editProfileBinding.etPhone.isErrorEnabled = true
                    editProfileBinding.etPhone.error = validation
                }
                else{
                    editProfileBinding.etPhone.isErrorEnabled  = false
                    editProfileBinding.etPhone.error = null
                }
            }
            else{
                editProfileBinding.etPhone.isErrorEnabled = false
            }
        }
    }

    companion object{
        const val REQUEST_CODE = 1
        private const val REQUEST_CODE_READ_PERMISSION = 22
    }


}