package com.zjj.daggerdatabinding.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import com.zjj.daggerdatabinding.App.App


import com.zjj.daggerdatabinding.bean.AppInfo

import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Collections
import java.util.Locale

/**
 * Created by zjj on 17/10/17.
 * Description: 系统工具类
 */

object SystemUtils {

    private val TAG = SystemUtils::class.java.simpleName

    /**
     * 获取系统安装的APP应用
     *
     * @param context
     */
    fun getAllApp(context: Context): ArrayList<AppInfo> {
        val manager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = manager.queryIntentActivities(mainIntent, 0)
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, ResolveInfo.DisplayNameComparator(manager))
        val appList = ArrayList<AppInfo>()
        for (info in apps) {
            val appInfo = AppInfo(info.activityInfo.name,
                    info.loadIcon(manager),
                    info.loadLabel(manager).toString() + "",
                    info.activityInfo.packageName)
            appList.add(appInfo)
            println("info.activityInfo.packageName=" + info.activityInfo.packageName)
            println("info.activityInfo.name=" + info.activityInfo.name)
        }
        return appList
    }

    /**
     * 获取用户安装的APP应用
     *
     * @param context
     */
    fun getUserApp(context: Context): ArrayList<AppInfo> {
        val manager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = manager.queryIntentActivities(mainIntent, 0)
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, ResolveInfo.DisplayNameComparator(manager))
        val appList = ArrayList<AppInfo>()
        for (info in apps) {
            val ainfo = info.activityInfo.applicationInfo
            if (ainfo.flags and ApplicationInfo.FLAG_SYSTEM <= 0) {
                val appInfo = AppInfo(info.activityInfo.name,
                        info.loadIcon(manager),
                        info.loadLabel(manager).toString() + "",
                        info.activityInfo.packageName)
                appList.add(appInfo)
            }
        }
        return appList
    }

    /**
     * 根据包名和Activity启动类查询应用信息
     *
     * @param cls
     * @param pkg
     * @return
     */
    fun getAppByClsPkg(context: Context, pkg: String, cls: String): AppInfo {

        val pm = context.packageManager
        var icon: Drawable
        var label: CharSequence = ""
        val comp = ComponentName(pkg, cls)
        try {
            val info = pm.getActivityInfo(comp, 0)
            icon = pm.getApplicationIcon(info.applicationInfo)
            label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0))
        } catch (e: PackageManager.NameNotFoundException) {
            icon = pm.defaultActivityIcon
        }
        val appInfo = AppInfo(cls,
                icon,
                label.toString() + "",
                pkg)
        return appInfo
    }

    /**
     * 跳转到WIFI设置
     *
     * @param context
     */
    fun intentWifiSetting(context: Context) {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
            context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
        } else {
            context.startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS))
        }
    }

    /**
     * WIFI网络开关
     */
    fun toggleWiFi(context: Context, enabled: Boolean) {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wm.isWifiEnabled = enabled
    }

    /**
     * 移动网络开关
     */
    fun toggleMobileData(context: Context, enabled: Boolean) {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var conMgrClass: Class<*>? = null // ConnectivityManager类
        var iConMgrField: Field? = null // ConnectivityManager类中的字段
        var iConMgr: Any? = null // IConnectivityManager类的引用
        var iConMgrClass: Class<*>? = null // IConnectivityManager类
        var setMobileDataEnabledMethod: Method? = null // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.javaClass.name)
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass!!.getDeclaredField("mService")
            // 设置mService可访问
            iConMgrField!!.isAccessible = true
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr)
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr!!.javaClass.name)
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass!!.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod!!.isAccessible = true
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }

    /**
     * GPS开关 当前若关则打，当前若开则关
     */
    fun toggleGPS(context: Context) {
        val gpsIntent = Intent()
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE")
        gpsIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }

    }

    /**
     * 调节系统音量
     *
     * @param context
     */
    fun holdSystemAudio(context: Context) {
        val audiomanage = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        // 获取系统最大音量
        // int maxVolume =
        // audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量
        val currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_RING)
        // 设置音量
        audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, currentVolume, AudioManager.FLAG_PLAY_SOUND)

        // 调节音量
        // ADJUST_RAISE 增大音量，与音量键功能相同
        // ADJUST_LOWER 降低音量
        audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)

    }

    /**
     * 设置亮度（每30递增）
     *
     * @param activity
     */
    fun setBrightness(activity: Activity) {
        val resolver = activity.contentResolver
        val uri = android.provider.Settings.System.getUriFor("screen_brightness")
        var nowScreenBri = getScreenBrightness(activity)
        nowScreenBri = if (nowScreenBri <= 225) nowScreenBri + 30 else 30
        println("nowScreenBri==" + nowScreenBri)
        android.provider.Settings.System.putInt(resolver, "screen_brightness", nowScreenBri)
        resolver.notifyChange(uri, null)
    }

    /**
     * 获取屏幕的亮度
     *
     * @param activity
     * @return
     */
    fun getScreenBrightness(activity: Activity): Int {
        var nowBrightnessValue = 0
        val resolver = activity.contentResolver
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return nowBrightnessValue
    }

    /**
     * 跳转到系统设置
     *
     * @param context
     */
    fun intentSetting(context: Context) {
        val pkg = "com.android.settings"
        val cls = "com.android.settings.Settings"

        val component = ComponentName(pkg, cls)
        val intent = Intent()
        intent.component = component

        context.startActivity(intent)
    }

    /**
     * 获取文件夹下所以的文件
     *
     * @param path
     * @return
     */
    fun getFilesArray(path: String): ArrayList<File> {
        val file = File(path)
        val files = file.listFiles()
        val listFile = ArrayList<File>()
        if (files != null) {
            for (i in files.indices) {
                if (files[i].isFile) {
                    listFile.add(files[i])
                }
                if (files[i].isDirectory) {
                    listFile.addAll(getFilesArray(files[i].toString()))
                }
            }
        }
        return listFile
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的，这样会节省内存
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND
     * 其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    fun getVideoThumbnail(videoPath: String, width: Int, height: Int, kind: Int): Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind)
        // System.out.println("w"+bitmap.getWidth());
        // System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        return bitmap
    }

    /**
     * 打开视频文件
     *
     * @param context
     * @param file    视频文件
     */
    fun intentVideo(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "video/*"
        val uri = Uri.fromFile(file)
        intent.setDataAndType(uri, type)
        context.startActivity(intent)
    }

    /**
     * 判断网络是否可用
     *
     *
     * This method requires the caller to hold the permission
     * [android.Manifest.permission.ACCESS_NETWORK_STATE].
     *
     * @param context
     * @return
     */
    fun checkNet(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return if (info != null && info.isAvailable) {
            true
        } else false
    }

    /**
     * 检查是否有可用网络
     */
    val isNetworkConnected: Boolean
        get() {
            val connectivityManager = App.instance.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }

    /**
     * 网络连接类型
     *
     * @param context
     * @return
     */
    @SuppressLint("DefaultLocale")
    fun getAPN(context: Context): String {
        var apn: String = ""
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo

        if (info != null) {
            if (ConnectivityManager.TYPE_WIFI == info.type) {
                apn = info.typeName
                if (apn == null) {
                    apn = "wifi"
                }
            } else {
                apn = info.extraInfo.toLowerCase()
                if (apn == null) {
                    apn = "mobile"
                }
            }
        }
        return apn
    }

    fun getModel(context: Context): String {
        return Build.MODEL
    }

    /**
     * 获取制造商
     *
     * @param context
     * @return
     */
    fun getManufacturer(context: Context): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取固件版本
     *
     * @param context
     * @return
     */
    fun getFirmware(context: Context): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取SDK版本
     *
     * @return
     */
    val sdkVer: String
        get() = Integer.valueOf(Build.VERSION.SDK_INT)!!.toString()

    /**
     * 获取当前语言
     *
     * @return
     */
    val language: String
        get() {
            val locale = Locale.getDefault()
            var languageCode = locale.language
            if (TextUtils.isEmpty(languageCode)) {
                languageCode = ""
            }
            return languageCode
        }

    /**
     * 获取国家代码
     *
     * @return
     */
    val country: String
        get() {
            val locale = Locale.getDefault()
            var countryCode = locale.country
            if (TextUtils.isEmpty(countryCode)) {
                countryCode = ""
            }
            return countryCode
        }

    /**
     * 获取imei
     *
     * @param context
     * @return
     */
    fun getIMEI(context: Context): String {
        val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var imei = mTelephonyMgr.deviceId
        if (TextUtils.isEmpty(imei) || imei == "000000000000000") {
            imei = "0"
        }
        return imei
    }

    /**
     * getIMSI Requires Permission:
     * [READ_PHONE_STATE][android.Manifest.permission.READ_PHONE_STATE]
     *
     * @param context
     * @return
     */
    fun getIMSI(context: Context): String {
        val mTelephonyMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imsi = mTelephonyMgr.subscriberId
        return if (TextUtils.isEmpty(imsi)) {
            "0"
        } else {
            imsi
        }
    }

    /**
     * 获取当前网络类型
     *
     * @param context
     * @return
     */
    fun getMcnc(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val mcnc = tm.networkOperator
        return if (TextUtils.isEmpty(mcnc)) {
            "0"
        } else {
            mcnc
        }
    }


    /**
     * 获取<meta-data>元素的数据
     *
     * @param context
     * @param keyName
     * @return
    </meta-data> */
    fun getMetaData(context: Context, keyName: String): Any? {
        try {
            val info = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val bundle = info.metaData
            return bundle.get(keyName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 获取App版本
     *
     * @param context
     * @return
     */
    fun getAppVersion(context: Context): String {
        val pm = context.packageManager
        val pi: PackageInfo
        try {
            pi = pm.getPackageInfo(context.packageName, 0)
            return pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 获取注册号码
     *
     * @param context
     * @return
     */
    fun getSerialNumber(context: Context): String? {
        var serial: String? = null
        try {
            val c = Class.forName("android.os.SystemProperties")
            val get = c.getMethod("get", String::class.java)
            serial = get.invoke(c, "ro.serialno") as String
            if (serial == null || serial.trim { it <= ' ' }.length <= 0) {
                val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                serial = tManager.deviceId
            }
            Log.d(SystemUtils::class.java.toString(), "Serial:" + serial!!)
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }

        return serial
    }

    /**
     * 判断SD卡是否存
     *
     * @return
     */
    val sDcard: Boolean
        get() = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            true
        } else false

    /**
     * 判断当前应用程序处于前台还是后台
     */
    fun isApplicationBroughtToBackground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != context.packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 获取屏幕像素
     *
     * @param activity
     * @return
     */
    fun screenSize(activity: Activity): String {
        val mDisplayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(mDisplayMetrics)
        val W = mDisplayMetrics.widthPixels
        val H = mDisplayMetrics.heightPixels
        return H.toString() + "x" + W
    }

    /**
     * 获取当前网络连接类型
     *
     * @param mContext
     * @return
     */
    fun GetNetworkType(mContext: Context): String {
        var strNetworkType = ""
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI"
            } else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                val _strSubTypeName = networkInfo.subtypeName

                // TD-SCDMA networkType is 17
                val networkType = networkInfo.subtype
                when (networkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN // api<8 : replace by
                    ->
                        // 11
                        strNetworkType = "2G"
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B // api<9 : replace by
                        ,
                        // 14
                    TelephonyManager.NETWORK_TYPE_EHRPD // api<11 : replace by
                        ,
                        // 12
                    TelephonyManager.NETWORK_TYPE_HSPAP // api<13 : replace by
                    ->
                        // 15
                        strNetworkType = "3G"
                    TelephonyManager.NETWORK_TYPE_LTE // api<11 : replace by
                    ->
                        // 13
                        strNetworkType = "4G"
                    else ->
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equals("TD-SCDMA", ignoreCase = true) || _strSubTypeName.equals("WCDMA", ignoreCase = true)
                                || _strSubTypeName.equals("CDMA2000", ignoreCase = true)) {
                            strNetworkType = "3G"
                        } else {
                            strNetworkType = _strSubTypeName
                        }
                }
            }
        }
        return strNetworkType
    }

    /**
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    fun getStatusHeight(activity: Activity): Int {
        var statusHeight = 0
        val localRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        statusHeight = localRect.top
        if (0 == statusHeight) {
            val localClass: Class<*>
            try {
                localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString())
                statusHeight = activity.resources.getDimensionPixelSize(i5)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

        }
        return statusHeight
    }
}
