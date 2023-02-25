package com.example.instagramv1

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EnterPasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_enter_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passwordTextInputLayout = view.findViewById<TextInputLayout>(R.id.etPassword)
        val passwordTextInputEditText = view.findViewById<TextInputEditText>(R.id.tiPassword)
        passwordTextInputLayout.setEndIconOnClickListener {

            if(view.findViewById<TextInputEditText>(R.id.tiPassword).transformationMethod is HideReturnsTransformationMethod){

                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_closed_eye)
                passwordTextInputEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            else{

                passwordTextInputLayout.setEndIconDrawable(R.drawable.ic_baseline_open_eye_24)
                passwordTextInputEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }
        }
    }

}