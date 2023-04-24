package com.example.instagramv1.ui.authscreen.loginscreen

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.instagramv1.R
import com.example.instagramv1.databinding.FragmentOTPBinding
import com.example.instagramv1.ui.mainscreen.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class OTPFragment : Fragment() {

    private val viewModel by activityViewModels<LoginViewModel> ()
    private lateinit var binding : FragmentOTPBinding
    private var fragmentView : View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_o_t_p, container, false)
        binding.loginViewModel = viewModel
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        createNotificationChannel()
    }

    private fun setupListeners(){
        binding.btnSendOtp.setOnClickListener {
            viewModel.otp = ""
            sendOtp()
            hideSoftKeyboard(binding.tiPhoneOrEmailOTP);
        }

        binding.backImageBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tiPhoneOrEmailOTP.setOnFocusChangeListener{_,focused ->
            if(focused){
                binding.etEnterPhoneOrEmailOTP.isErrorEnabled = false
            }

        }

        binding.tiPhoneOrEmailOTP.addTextChangedListener(textWatcher)
    }

    private val textWatcher : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.btnSendOtp.isEnabled = binding.tiPhoneOrEmailOTP.text!!.isNotBlank()
        }

    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        view.clearFocus()
    }

    private fun buildNotification(otp : String){
        val builder = NotificationCompat.Builder(requireActivity(), "OTP CHANNEL")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("Snapshots OTP")
            .setContentText("OTP : $otp")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notification = builder.build()
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1,notification)

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "OTP CHANNEL"
            val descriptionText = "Channel for otp"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("OTP CHANNEL", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendOtp() {
        if (NotificationManagerCompat.from(requireActivity()).areNotificationsEnabled()) {
            lifecycleScope.launch {
                viewModel.otpUserInfo = binding.tiPhoneOrEmailOTP.text.toString()
                if (viewModel.checkOtpUserInfo()) {
                    viewModel.givenOtp = getRandomNumberString()
                    Log.d("Special", viewModel.givenOtp)

                    parentFragmentManager.beginTransaction().apply {
                        addToBackStack(null)
                        replace(R.id.frame_layout, EnterOTPFragment())
                        commit()
                    }
                    delay(1000)
                    buildNotification(viewModel.givenOtp)
                } else {
                    if(viewModel.otpUserInfo.isNotBlank()){
                        binding.etEnterPhoneOrEmailOTP.isErrorEnabled = true
                        binding.etEnterPhoneOrEmailOTP.error = "User not exist"
                    }

                }
            }
        } else {

            val otpNotificationDialogFragment = OtpNotificationDialogFragment()
            otpNotificationDialogFragment.show(parentFragmentManager,"dialog2")

        }
    }










    private fun getRandomNumberString(): String {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random
        val number: Int = rnd.nextInt(999999)

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number)
    }

    private fun saveLoginData(){
        val sharedPreferences = activity?.getSharedPreferences("COMMON_PREFERENCE", Context.MODE_PRIVATE) ?: return
        val edit = sharedPreferences.edit()
        val value : Int = viewModel.userId.value!!
        edit.putInt("USER_ID",viewModel.userId.value!!)
        edit.apply()
    }

    private fun startMainActivity(){
        Intent(requireActivity(), MainActivity::class.java).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }


}