package com.hrodriguesdev.supervisorioandroid

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hrodriguesdev.supervisorioandroid.databinding.ActivityGlendonBinding

class ActivityGlendon : AppCompatActivity() {

    private lateinit var binding: ActivityGlendonBinding
    private lateinit var fullscreenContent: ImageView
    private val hideHandler = Handler(Looper.myLooper()!!)
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideRunnable = Runnable { hide() }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlendonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullscreenContent = binding.glendonfullscreenContent
        fullscreenContentControls = binding.fullscreenContentControls
        fullscreenContent.setOnClickListener { hide() }


    }

    private fun hide() {
        supportActionBar?.hide()
        fullscreenContentControls.visibility = View.GONE
        hideHandler.postDelayed(hidePart2Runnable, ActivityGlendon.UI_ANIMATION_DELAY.toLong())

    }

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())

        } else {
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        }
    }

    companion object {
        private const val UI_ANIMATION_DELAY = 300

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)

    }

    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())

    }
}