package com.example.instagramv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        val window = this.window
        window.statusBarColor = getColor(R.color.dimBlue)
        val fragment = LoginFragment()
        val enterPasswordFragment = EnterPasswordFragment()
        val registerFragment = RegisterFragment()
//        supportFragmentManager.beginTransaction().apply {
//            add(R.id.mainFrame,registerFragment)
//            commit()
//        }
    }
}