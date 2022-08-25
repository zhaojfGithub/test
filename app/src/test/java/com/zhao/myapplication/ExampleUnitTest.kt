package com.zhao.myapplication

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun textCoroutines() = runBlocking {
        val a = launch {
            testSleepA("test1")
            testSleepA("test2")
        }
        //val b = launch { testSleepA("test2") }
        a.join()
       // b.join()
        println("start")
        //val resultA = a.await()
        //val resultB = b.await()
        // c = resultA + resultB
        //println(c)

    }


    private suspend fun testSleepA(type:String) : String {
        println("start $type")
        delay(1000)
        println("end $type")
        return type
    }
}
