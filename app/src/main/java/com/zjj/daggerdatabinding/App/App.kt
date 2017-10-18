package com.zjj.daggerdatabinding.App

import android.app.Application
import com.zjj.daggerdatabinding.component.ApiComponent
import com.zjj.daggerdatabinding.component.DaggerApiComponent
import com.zjj.daggerdatabinding.module.ApiModule
import com.zjj.daggerdatabinding.module.AppModule
import javax.inject.Inject
import android.app.Activity



/**
 * Created by zjj on 17/10/17.
 */
class App : Application() {
    init {
        instance = this
    }

    private lateinit var allActivities: MutableSet<Activity>
    @Inject lateinit var apiComponent: ApiComponent
    override fun onCreate() {
        super.onCreate()
        //蒲公英crash上报
//        PgyCrashManager.register(this);
        //初始化内存泄漏检测
//        LeakCanary.install(this);
        //初始化过度绘制检测
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //初始化realm
        DaggerApiComponent.builder().apiModule(ApiModule()).appModule(AppModule(this)).build().inject(this)
    }


    companion object {
        lateinit var instance: App

    }

    fun registerActivity(act: Activity) {
        if (allActivities == null) {
            allActivities = HashSet()
        }
        allActivities.add(act)
    }

    fun unregisterActivity(act: Activity) {
        allActivities.remove(act)
    }

    fun exitApp(){
        if(allActivities != null){
            synchronized(allActivities){
                allActivities.forEach { act ->
                    if (!act.isFinishing())
                        act.finish()
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }



}