package com.x0.newsapi.test;

import com.x0.newsapi.NewsApiApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.TrampolineScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public abstract class DefaultPluginTestSetup {

    @BeforeClass
    public static void beforeClass() {
        new NewsApiApplication(); // setup instance
    }

    @Before
    public void setupPlugins() {
        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return TrampolineScheduler.instance();
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void resetPlugins() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }
}
