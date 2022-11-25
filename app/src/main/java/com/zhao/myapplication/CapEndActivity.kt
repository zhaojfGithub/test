package com.zhao.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.zhao.myapplication.databinding.ActivityCapEndBinding

class CapEndActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCapEndBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        /*findViewById<View>(android.R.id.content).transitionName = "Toolbar"
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 3000L
            startContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
            endContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 2500L
            startContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
            endContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
        }*/
        super.onCreate(savedInstanceState)
        binding = ActivityCapEndBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.transitionName = "Toolbar"
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(binding.root)
            duration = 3000L
            startContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
            endContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(binding.root)
            duration = 2500L
            startContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
            endContainerColor = ContextCompat.getColor(this@CapEndActivity,R.color.white)
        }



        when (intent.getStringExtra("ToolbarId")) {
            "1" -> {
                //共享元素
            }
            "2" -> {
                //变形转换
            }
            "3" -> {
                //共享轴体过度 X,Y,Z
            }
            "4" -> {
                //淡入淡出
            }
            "5" -> {
                //选项转换
            }
        }
    }
}