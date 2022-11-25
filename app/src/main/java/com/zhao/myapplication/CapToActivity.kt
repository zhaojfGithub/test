package com.zhao.myapplication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.zhao.myapplication.databinding.ActivityCapToBinding

class CapToActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCapToBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        binding = ActivityCapToBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.button1.transitionName = "Toolbar"
        /**
         * 共享元素
         */
        binding.button1.setOnClickListener {
            val intent = Intent(this, CapEndActivity::class.java)
            intent.putExtra("ToolbarId", "1")
            intent.putExtra("ToolbarName", binding.button1.transitionName)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, it, binding.button1.transitionName).toBundle())
        }
        /**
         *
         */


    }
}