package com.zhao.myapplication

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun textCoroutines() {
        runBlocking {
            //启动一个新协程，并阻塞当前线程，直到内存所有逻辑及子协程逻辑全部执行完成
        }
        GlobalScope.launch {
            //再应用范围内启动一个新协程，协程的生命周期与应用程序一致。这样启动的协程并不能使线程保活，就像是守护线程

        }
        CoroutineScope(Dispatchers.Default).launch {
            //在应用中推荐使用的方式，为自己的组件实现CoroutieScope接口，在需要的地方使用launch方法启动协程
            //使得协程和该组件生命周期绑定，组件销毁时，协程一并销毁
        }

        //launch{} 异步启动一个子协程
        //async{} 移动启动一个子协程，并返回Deffer对象，可通过Deffer.await方法等待子协程执行完成并获取结果，常用于并发执行-同步等待的情况

    }



}
