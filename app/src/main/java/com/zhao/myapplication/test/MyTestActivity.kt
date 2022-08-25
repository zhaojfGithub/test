package com.zhao.myapplication.test

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zhao.myapplication.R
import com.zhao.myapplication.databinding.ActivityMyTestBinding
import com.zhao.myapplication.databinding.ViewTextBinding
import java.util.*

class MyTestActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMyTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMyTestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {
        Appsigning.getSignInfo(this,Appsigning.SHA1).forEach {
            mLog(it)
        }

        val fragments: Array<Pair<Fragment, ViewTextBinding>> = arrayOf(
            Pair(MyTestFragment.newInstance("全部"), ViewTextBinding.inflate(LayoutInflater.from(this), viewBinding.tabLayout, false)),
            Pair(MyTestFragment.newInstance("最新"), ViewTextBinding.inflate(LayoutInflater.from(this), viewBinding.tabLayout, false)),
            Pair(MyTestFragment.newInstance("Test"), ViewTextBinding.inflate(LayoutInflater.from(this), viewBinding.tabLayout, false))
        )
        viewBinding.wiewPage.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position].first
            }

        }
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.wiewPage) { tab, position ->
            tab.customView = fragments[position].second.root
            when (position) {
                0 -> {
                    fragments[position].second.lean.text = "全部"
                }
                1 -> {
                    fragments[position].second.lean.text = "最新"
                }
                else -> {

                }
            }
        }.attach()
        viewBinding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        viewBinding.tabLayout.setSelectedTabIndicator(null)
        viewBinding.wiewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                mLog("onPageScrollStateChanged${state}")
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                mLog("onPageScrolled,position = ${position},positionOffset = $positionOffset ,positionOffsetPixels = $positionOffsetPixels")

            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                fragments.forEach { pair: Pair<Fragment, ViewTextBinding> ->
                    pair.second.lean.apply {
                        if (pair == fragments[position]) {
                            paint.style = Paint.Style.FILL_AND_STROKE
                            paint.strokeWidth = 1F
                            textSize = 16F
                        } else {
                            paint.style = Paint.Style.FILL
                            paint.strokeWidth = 0.7F
                            textSize = 12F
                        }
                    }

                }

            }
        })
    }


    private fun mLog(msg: String) {
        Log.e("MyTestActivity", msg)
    }
}