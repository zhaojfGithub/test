package com.zhao.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.zhao.myapplication.databinding.ActivityFragBinding
import com.zhao.myapplication.databinding.ActivityMainBinding

/**
 *创建时间： 2022/11/16
 *编   写：  zjf
 *页面功能:
 */
class FragActivity : AppCompatActivity() {

    private lateinit var viewBinder: ActivityFragBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityFragBinding.inflate(layoutInflater)
        
    }
}