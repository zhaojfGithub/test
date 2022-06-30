package com.zhao.myapplication

import android.app.IntentService
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService
import androidx.recyclerview.widget.RecyclerView
import com.zhao.myapplication.databinding.ActivityTestBinding
import com.zhao.myapplication.databinding.FragmentViewBinding

class TestActivity : AppCompatActivity() {

    private lateinit var viewBinder: ActivityTestBinding

    private val conn:ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(this@TestActivity.javaClass.simpleName,"绑定成功")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(this@TestActivity.javaClass.simpleName,"内存不足被销毁")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityTestBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        //viewBinder.viewPage.adapter = TestAdapter()
        viewBinder.button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        viewBinder.button1.setOnClickListener {
            unbindService(conn)
        }
        val intent = Intent(this,TestService::class.java)
        bindService(intent,conn, Service.BIND_AUTO_CREATE)
        mLog("onCreate")

        Singleton.getInstance()
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