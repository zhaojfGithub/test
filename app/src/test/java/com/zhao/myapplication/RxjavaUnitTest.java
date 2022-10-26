package com.zhao.myapplication;

import org.junit.Test;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;


/**
 * 创建时间： 2022/10/26
 * 编   写：  zjf
 * 页面功能:
 */
public class RxjavaUnitTest {

    @Test
    public void test() {
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            //create 是 rxjava最基本的创建事件序列的方法
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        Function<Integer,String> function = new Function<Integer,String>(){

            @Override
            public String apply(Integer integer) {
                return integer.toString() + "变换";
            }
        };

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("开始采用subscribe连接");
            }

            @Override
            public void onNext(@NonNull String integer) {
                System.out.println("对Next事件做出响应" + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("对ERROR事件做出回应");
            }

            @Override
            public void onComplete() {
                System.out.println("对Complete事件做出响应");
            }
        };
        /**
         * 只对next做出响应
         */
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String integer) {
                System.out.println("对Next事件做出响应" + integer);
            }
        };
        observable.map(function).subscribe(observer);

        //observable.subscribe(observer);


    }
}
