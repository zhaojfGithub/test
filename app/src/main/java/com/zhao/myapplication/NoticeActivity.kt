package com.zhao.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.zhao.myapplication.databinding.ActivityNoticeBinding

/**
 *创建时间： 2022/11/21
 *编   写：  zjf
 *页面功能:
 */
class NoticeActivity : AppCompatActivity() {


    /**
     *  NotificationManager 通知管理器，用来发起、更新、删除通知。
     *  NotificationChannel 通知渠道，8.0及以上配置渠道以及优先级。
     *  NotificationCompat.Builder 通知构造器，用来配置通知的布局显示以及操作相关。
     */
    private lateinit var binding: ActivityNoticeBinding
    private lateinit var mManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mManager = this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        initView()
    }


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
        }
        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    private fun initView() {
        binding.button1.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("1", "button1", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "描述"
                    setShowBadge(false)//是否在桌面显示角标
                }
                mManager.createNotificationChannel(channel)
            }
            val intent = Intent(this, this::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val uri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/raw/${R.raw.weekly_new}")
            val mBuilder = NotificationCompat.Builder(this, "1")
                .setContentTitle("普通标题")
                .setContentText("普通通知内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //7.0 设置优先级
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(false)
                .setSound(uri)

            mManager.notify(1, mBuilder.build())

        }

        binding.button2.setOnClickListener {
            val text = "有新的周刊需要查看！"
            val uri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/raw/${R.raw.weekly_new}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("2", "button2", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "描述"
                    setShowBadge(false)//是否在桌面显示角标
                    enableVibration(true)
                    setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
                }
                mManager.createNotificationChannel(channel)
            }
            val notificationBuild = NotificationCompat.Builder(applicationContext, "2")
                .setContentTitle(text)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setOngoing(false)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSound(uri)

            mManager.notify(2, notificationBuild.build())
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this, TestService::class.java)
            startService(intent)
        }
        binding.button4.setOnClickListener {
            val intent = Intent(this, TestService::class.java)
            bindService(intent,connection,Context.BIND_AUTO_CREATE)
        }
        binding.button5.setOnClickListener {
            val intent = Intent(this, TestService::class.java)
            stopService(intent)
        }
        binding.button6.setOnClickListener {
            unbindService(connection)
        }
    }
}