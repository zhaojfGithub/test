package com.zhao.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zhao.myapplication.databinding.ActivityTestBinding
import com.zhao.myapplication.databinding.FragmentViewBinding

class TestActivity : AppCompatActivity() {

    private lateinit var viewBinder: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityTestBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        //viewBinder.viewPage.adapter = TestAdapter()
        viewBinder.button.setOnClickListener {
            val intent = Intent(this,TestActivity::class.java)
            startActivity(intent)
        }
        mLog("onCreate")
    }

    override fun onStart() {
        super.onStart()
        mLog("onStart")
    }

    override fun onResume() {
        super.onResume()
        mLog("onResume")
    }

    override fun onPause() {
        super.onPause()
        mLog("onPause")
    }

    override fun onStop() {
        super.onStop()
        mLog("onStop")
    }

    override fun onRestart() {
        super.onRestart()
        mLog("onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        mLog("onDestroy")
    }

    private fun mLog(msg:String){
        Log.e(this.javaClass.simpleName,msg)
    }

    private class TestAdapter() : RecyclerView.Adapter<TestAdapter.TextViewHolder>() {
        private class TextViewHolder(binding: FragmentViewBinding) : RecyclerView.ViewHolder(binding.root) {
             val textView: TextView = binding.text
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
            return TextViewHolder(FragmentViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
            holder.textView.text = position.toString()
        }

        override fun getItemCount(): Int {
            return 3
        }

    }
}