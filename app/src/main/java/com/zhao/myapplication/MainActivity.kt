package com.zhao.myapplication


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bumptech.glide.Glide
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.zhao.myapplication.databinding.ActivityMainBinding
import com.zhao.myapplication.databinding.ActivityTestBinding
import com.zhao.mylibrary.MyStringUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {


    //val viewModel = ViewModelProvider(this)[MainViewModel::class.java_basics]

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

    private val viewModel : MainViewModel by viewModels()

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

        var handlerThread = HandlerThread("handlerThread")
        handlerThread.start()
        var handler1 = object : Handler(handlerThread.looper){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
            }
        }
        handler.sendMessage(Message.obtain())
        handler.post {

        }
        /* viewBinder.root.transitionName = "Toolbar"
         window.sharedElementEnterTransition = MaterialContainerTransform().apply {
             aaddTarget(viewBinder.root)
             duration = 300L
             containerColor = ContextCompat.getColor(this@MainActivity,R.color.white)
         }
         window.sharedElementReturnTransition = MaterialContainerTransform().apply {
             addTarget(viewBinder.root)
             duration = 250L
             containerColor = ContextCompat.getColor(this@MainActivity,R.color.white)
         }
         window.sharedElementsUseOverlay = false


         val name = "Toolbar"
         viewBinder.button1.text = name

 */
        /*val intent = Intent(this,TestService::class.java_basics)
        bindService(intent,conn, Service.BIND_AUTO_CREATE)*/
        initLauncher()
        /*viewBinder.button.setOnClickListener {
            viewModel.testLaunch()
        }*/
        //val file = File(this.filesDir,"aaa.txt")
       // Glide.with(this).load("").into()
       // Log.e("MainActivity",file.absolutePath)
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



        /*val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = 10
            leftMargin = 20
            rightMargin = 20
            bottomMargin = 10
        }
        viewBinder.flowLayout.removeAllViews()
        for (i in 0..20){
            val textView = TextView(this)
            textView.text = "这是第${i}个"
            textView.gravity = Gravity.CENTER
            textView.layoutParams = layoutParams
            textView.setBackgroundColor(Color.RED)
            viewBinder.flowLayout.addView(textView)
        }

        viewBinder.button.setOnClickListener {
            Thread {

                    EventBus.getDefault().post("hello eventbus")


            }.start()

        }*/
    }

    private lateinit var permissionLauncher : ActivityResultLauncher<String>

    private fun initLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it){
                Log.e("MainActivity","请求成功")
            }else{
                Log.e("MainActivity","请求失败")
            }
        }
    }

    private fun obtainPermission() {
        permissionLauncher.launch(android.Manifest.permission.SYSTEM_ALERT_WINDOW)
    }

/*    fun go(){
        val startY = viewBinder.ivLogo.bottom.toFloat()
        val endY = viewBinder.ivLogo.bottom - viewBinder.ivLogo.height.toFloat()
        val objectAnimatorY = ObjectAnimator.ofFloat(viewBinder.ivLogo, "translationY", viewBinder.ivLogo.height * -1F)
        val objectAnimatorAlpha = ObjectAnimator.ofFloat(viewBinder.ivLogo, "alpha",1F,0F)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 3000
        animatorSet.play(objectAnimatorY).with(objectAnimatorAlpha)
        animatorSet.start()
    }*/

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


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    /**
     * 订阅者
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(msg:String){
        Log.e("MainActivity","收到了订阅事件${msg}")
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }


}

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}