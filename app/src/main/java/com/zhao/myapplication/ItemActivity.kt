package com.zhao.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhao.myapplication.databinding.ActivityItemBinding

class ItemActivity : AppCompatActivity() {


    private lateinit var binding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.button1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        binding.button3.setOnClickListener {
            val intent = Intent(this,CapToActivity::class.java)
            startActivity(intent)
        }
        binding.button4.setOnClickListener {
            val intent = Intent(this,NoticeActivity::class.java)
            startActivity(intent)
        }
        binding.button5.setOnClickListener {
            val intent = Intent(this,ScrollActivity::class.java)
            startActivity(intent)
        }
    }
}