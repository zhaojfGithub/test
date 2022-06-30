package com.zhao.myapplication


import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zhao.myapplication.databinding.ActivityMainBinding
import com.zhao.myapplication.databinding.ActivityTestBinding
import com.zhao.mylibrary.MyStringUtil
import java.io.File

class MainActivity : AppCompatActivity() {


    private val fragments: List<Fragment> = listOf(
        TestFragment.newInstance("testFragment1", ""),
        TestFragment.newInstance("testFragment2", "")
    )

    private val handler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            Log.e("MainActivity","ok")
            startSO()
        }
    }


    private fun startSO() {
        Log.e("MainActivity", MyStringUtil.ndkString())

    }

    private lateinit var viewBinder: ActivityMainBinding

    private var conn : ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(this@MainActivity.javaClass.simpleName,"绑定成功")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(this@MainActivity.javaClass.simpleName,"内存不足被销毁")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        val intent = Intent(this,TestService::class.java)
        bindService(intent,conn, Service.BIND_AUTO_CREATE)
        viewBinder.button.setOnClickListener {
            unbindService(conn)
            onBackPressed()
        }
        val file = File(this.filesDir,"aaa.txt")
        Log.e("MainActivity",file.absolutePath)
       /* Thread{
            if (!file.exists()){
                file.createNewFile()
            }
            try {
                val fileWriter = file.writer()
                fileWriter.appendLine("aaaa")
                fileWriter.close()
                val message = Message.obtain()
                message.arg1 = 1
                handler.sendMessage(message)
            }catch (e:Exception){
                e.printStackTrace()
            }finally {

            }
        }.start()*/


        //setContentView(SunView(this))
        //og.e("MainActivity",MyStringUtil.ndkString())
        //val frameLayout: FrameLayout = findViewById(R.id.frameLayout)
       /* val testFragment1: TestFragment = TestFragment.newInstance("testFragment1","")
        val testFragment2: TestFragment = TestFragment.newInstance("testFragment2","")
            Log.e("MainActivity", "testFragment1地址 = $testFragment1")
        Log.e("MainActivity", "testFragment2地址 = $testFragment2")
        val beginTransaction = supportFragmentManager.beginTransaction()
        Log.e("MainActivity", "未加入前,${beginTransaction.isEmpty}")
        beginTransaction.add(R.id.frameLayout, testFragment1)
        Log.e("MainActivity", "加入一个,${beginTransaction.isEmpty}")
        beginTransaction.add(R.id.frameLayout, testFragment2)
        Log.e("MainActivity", "加入二个,${beginTransaction.isEmpty}")
        beginTransaction.commit()
        if (supportFragmentManager.fragments.isNotEmpty()) {
            supportFragmentManager.fragments.forEach {
                Log.e("MainActivity", "for打印${it}")
            }
        }*/
        //cutFragment(0)
    }


    fun cutFragment(position: Int) {
        /*Log.e("ACT","position:$position")
        val fragment = fragments[position]
        val beginTransaction = supportFragmentManager.beginTransaction()
        val displayFragment = supportFragmentManager.fragments.find { it.isVisible }
        if (displayFragment == fragment) return
        beginTransaction.setCustomAnimations(R.anim.alide_left_in,R.anim.alide_right_out,R.anim.alide_right_in,R.anim.alide_left_out)
        if (displayFragment != null) {
            beginTransaction.hide(displayFragment)
        }
        if (supportFragmentManager.fragments.indexOf(fragment) == -1){
            beginTransaction.add(R.id.frameLayout,fragment)
        }else{
            beginTransaction.show(fragment)
        }
        //beginTransaction.replace(R.id.frameLayout,fragment)
        beginTransaction.commit()*/
    }


}