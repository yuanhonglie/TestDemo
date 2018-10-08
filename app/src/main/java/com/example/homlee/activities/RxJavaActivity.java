package com.example.homlee.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.homlee.R;
import com.example.homlee.rxjava.ActivityEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by homlee on 2018/9/6.
 */

public class RxJavaActivity extends BaseActivity implements View.OnClickListener {

    private volatile boolean isFinished = false;
    private volatile Disposable mDisposable1 = null;
    private volatile Disposable mDisposable2 = null;
    private volatile Disposable mDisposable3 = null;
    private volatile Disposable mDisposable5 = null;

    private Executor mExecutor1;
    private Executor mExecutor2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        findViewById(R.id.btn_rx_sample1).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample2).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample3).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample4).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample5).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample6).setOnClickListener(this);
        findViewById(R.id.btn_rx_sample7).setOnClickListener(this);
        initExecutors();
    }

    private void initExecutors() {
        mExecutor1 = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread t = new Thread(r);
                t.setName("rx-t1");
                return t;
            }
        });
        mExecutor2 = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread t = new Thread(r);
                t.setName("rx-t2");
                return t;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_sample1:
                rxJavaSample1();
                break;
            case R.id.btn_rx_sample2:
                rxJavaSample2();
                break;
            case R.id.btn_rx_sample3:
                rxJavaSample3();
                break;
            case R.id.btn_rx_sample4:
                rxJavaSample4();
                break;
            case R.id.btn_rx_sample5:
                rxJavaSample5();
                break;
            case R.id.btn_rx_sample6:
                rxJavaSample6();
                break;
            case R.id.btn_rx_sample7:
                rxJavaSample7();
                break;
            default:
                break;
        }
    }

    private void rxJavaSample1() {
        Log.i(TAG, "rxJavaSample1: start");
        Observable.just(1, 2, 3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "string:" + integer;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.i(TAG, "onSubscribe: ");
                        if (isFinished) {
                            dispose(disposable);
                        } else {
                            mDisposable1 = disposable;
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
        Log.i(TAG, "rxJavaSample1: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample1: start
    I/RxJavaActivity: onSubscribe:
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: onNext: string:2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
    I/RxJavaActivity: rxJavaSample1: end
     */

    private void rxJavaSample2() {
        Log.i(TAG, "rxJavaSample2: start");
        Observable<Integer> observableJust = Observable.just(1,2,3);
        Observable<String> observableMap = observableJust.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "string:" + integer;
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                Log.i(TAG, "onSubscribe: ");
                if (isFinished) {
                    dispose(disposable);
                } else {
                    mDisposable2 = disposable;
                }
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };
        observableMap.subscribe(observer);
        Log.i(TAG, "rxJavaSample2: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample2: start
    I/RxJavaActivity: onSubscribe:
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: onNext: string:2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
    I/RxJavaActivity: rxJavaSample2: end
     */

    private void rxJavaSample3() {
        Log.i(TAG, "rxJavaSample3: start");
        Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "string:" + integer;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.i(TAG, "onSubscribe:Thread = " + Thread.currentThread().getName());
                        if (isFinished) {
                            dispose(disposable);
                        } else {
                            mDisposable3 = disposable;
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
        Log.i(TAG, "rxJavaSample3: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample3: start
    I/RxJavaActivity: onSubscribe:Thread = main
    I/RxJavaActivity: rxJavaSample3: end
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: onNext: string:2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
     */

    private void rxJavaSample4() {
        Log.i(TAG, "rxJavaSample4: start");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                rxJavaSample3();
            }
        });
        thread.setName("rx-sample4");
        thread.start();
        Log.i(TAG, "rxJavaSample4: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample4: start
    I/RxJavaActivity: rxJavaSample4: end
    I/RxJavaActivity: rxJavaSample3: start
    I/RxJavaActivity: onSubscribe:Thread = rx-sample4
    I/RxJavaActivity: rxJavaSample3: end
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: onNext: string:2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
     */



    private void rxJavaSample5() {
        Log.i(TAG, "rxJavaSample5: start");
        Observable.just(1, 2, 3)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        Log.i(TAG, "filter:test: integer  = " + integer);
                        return (integer % 2) == 1;
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.i(TAG, "map:apply: integer  = " + integer);
                        return "string:" + integer;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.i(TAG, "onSubscribe: ");
                        if (isFinished) {
                            dispose(disposable);
                        } else {
                            mDisposable5 = disposable;
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
        Log.i(TAG, "rxJavaSample5: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample5: start
    I/RxJavaActivity: onSubscribe:
    I/RxJavaActivity: filter:test: integer  = 1
    I/RxJavaActivity: map:apply: integer  = 1
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: filter:test: integer  = 2
    I/RxJavaActivity: filter:test: integer  = 3
    I/RxJavaActivity: map:apply: integer  = 3
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
    I/RxJavaActivity: rxJavaSample5: end
     */


    private void rxJavaSample6() {
        Log.i(TAG, "rxJavaSample6: start");
        Observable.just(1, 2, 3)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        Log.i(TAG, "filter:test: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "filter:test: integer  = " + integer);
                        return (integer % 2) == 1;
                    }
                })
                .subscribeOn(Schedulers.from(mExecutor1))
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Log.i(TAG, "map:apply: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "map:apply: integer  = " + integer);
                        return "string:" + integer;
                    }
                })
                .observeOn(Schedulers.from(mExecutor2))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.i(TAG, "onSubscribe: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "onSubscribe: ");
                        if (isFinished) {
                            dispose(disposable);
                        } else {
                            mDisposable5 = disposable;
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
        Log.i(TAG, "rxJavaSample6: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample6: start
    I/RxJavaActivity: onSubscribe: Thread = main
    I/RxJavaActivity: onSubscribe:
    I/RxJavaActivity: rxJavaSample6: end
    I/RxJavaActivity: filter:test: Thread = rx-t1
    I/RxJavaActivity: filter:test: integer  = 1
    I/RxJavaActivity: map:apply: Thread = rx-t1
    I/RxJavaActivity: map:apply: integer  = 1
    I/RxJavaActivity: filter:test: Thread = rx-t1
    I/RxJavaActivity: filter:test: integer  = 2
    I/RxJavaActivity: filter:test: Thread = rx-t1
    I/RxJavaActivity: filter:test: integer  = 3
    I/RxJavaActivity: map:apply: Thread = rx-t1
    I/RxJavaActivity: map:apply: integer  = 3
    I/RxJavaActivity: onNext: Thread = rx-t2
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: onNext: Thread = rx-t2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: onComplete:
     */

    private void rxJavaSample7() {
        Log.i(TAG, "rxJavaSample7: start");
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTORY))
                .filter(new Predicate<Long>() {
                    @Override
                    public boolean test(Long num) throws Exception {
                        Log.i(TAG, "filter:test: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "filter:test: num  = " + num);
                        return (num % 2) == 1;
                    }
                })
                .subscribeOn(Schedulers.from(mExecutor1))
                .map(new Function<Long, String>() {
                    @Override
                    public String apply(Long num) throws Exception {
                        Log.i(TAG, "map:apply: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "map:apply: num  = " + num);
                        return "string:" + num;
                    }
                })
                .observeOn(Schedulers.from(mExecutor2))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        Log.i(TAG, "onSubscribe: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "onNext: Thread = " + Thread.currentThread().getName());
                        Log.i(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");
                    }
                });
        Log.i(TAG, "rxJavaSample7: end");
    }

    /*
    I/RxJavaActivity: rxJavaSample7: start
    I/RxJavaActivity: onSubscribe: Thread = main
    I/RxJavaActivity: onSubscribe:
    I/RxJavaActivity: rxJavaSample7: end
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 0
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 1
    I/RxJavaActivity: map:apply: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: map:apply: num  = 1
    I/RxJavaActivity: onNext: Thread = rx-t2
    I/RxJavaActivity: onNext: string:1
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 2
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 3
    I/RxJavaActivity: map:apply: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: map:apply: num  = 3
    I/RxJavaActivity: onNext: Thread = rx-t2
    I/RxJavaActivity: onNext: string:3
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 4
    I/RxJavaActivity: filter:test: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: filter:test: num  = 5
    I/RxJavaActivity: map:apply: Thread = RxComputationThreadPool-1
    I/RxJavaActivity: map:apply: num  = 5
    I/RxJavaActivity: onNext: Thread = rx-t2
    I/RxJavaActivity: onNext: string:5
    I/RxJavaActivity: onComplete:
     */

    @Override
    public void finish() {
        isFinished = true;
        disposeAllObservables();
        super.finish();
    }

    private void disposeAllObservables() {
        dispose(mDisposable1);
        dispose(mDisposable2);
        dispose(mDisposable3);
        dispose(mDisposable5);
    }

    private void dispose(Disposable d) {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}
