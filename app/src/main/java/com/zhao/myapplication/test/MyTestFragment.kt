package com.zhao.myapplication.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zhao.myapplication.databinding.FragmentViewBinding

/**
 *创建时间： 2022/8/24
 *编   写：  zjf
 *页面功能:
 */
class MyTestFragment : Fragment() {

    companion object {

        private const val DIY_NAME = "diy_name"

        fun newInstance(name: String): Fragment {

            return MyTestFragment().apply {
                arguments = Bundle().apply {
                    putString(DIY_NAME, name)
                }
            }
        }
    }

    private lateinit var viewBinding: FragmentViewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentViewBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val name = arguments?.getString(DIY_NAME,"ERROR1") ?: "ERROR2"
        viewBinding.text.text = name
    }
}