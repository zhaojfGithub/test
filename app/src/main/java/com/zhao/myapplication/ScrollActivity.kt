package com.zhao.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhao.myapplication.databinding.ActivityScrollBinding

/**
 *创建时间： 2022/11/25
 *编   写：  zjf
 *页面功能:
 */
class ScrollActivity : AppCompatActivity() {


    private lateinit var binding: ActivityScrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button1.setOnClickListener {
            binding.textView.startScrollBy(-1 * 100, -1 * 100)
        }

    }
}