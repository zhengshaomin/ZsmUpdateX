package cn.zsmupdatex.library;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;

/**
 * 作者：daboluo on 2024/3/29 17:28
 * Email:daboluo719@gmail.com
 */
public class ZsmUpdateX {

    private String title;//下载标题
    private String content;//下载内容
    private String url;//下载连接

    public static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    public interface SuccessCallback {
        void onSuccess();
    }

    public interface ErrorCallback {
        void onError(String errorMessage);
    }

    //初始化
    public static void initialize(Context context, String applicationId,SuccessCallback onSuccess, ErrorCallback onError) {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            // 通知权限尚未被授予，向用户请求授予通知权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 如果是 Android 8.0 及以上版本，跳转到应用通知设置页面
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);
            } else {
                // 如果是 Android 7.1 及以下版本，跳转到应用详细设置页面
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", context.getPackageName(), null));
                context.startActivity(intent);
            }
        }
        try {
            // 获取当前应用程序的 ApplicationInfo
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);

            // 获取 AndroidManifest.xml 中的 FileProvider 的 authorities 属性的资源 ID
            int resourceId = context.getResources().getIdentifier("cn.zsmupdatex.app.fileProvider", "string", applicationInfo.packageName);

            // 如果资源 ID 存在，则设置 authorities 属性为指定的值
            if (resourceId != 0) {
                ProviderInfo providerInfo = new ProviderInfo();
                providerInfo.authority = applicationId+".fileProvider";
                providerInfo.packageName = applicationInfo.packageName;
                providerInfo.name = "androidx.core.content.FileProvider";

                // 通过反射设置 FileProvider 的 authorities 属性
                try {
                    Field authoritiesField = providerInfo.getClass().getField("authority");
                    authoritiesField.setAccessible(true);
                    authoritiesField.set(providerInfo, applicationId);
                    onSuccess.onSuccess();//成功
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            onError.onError(e.getMessage());//出现错误
        }
    }
    /**
     * 更新标题
     */
    public ZsmUpdateX setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 更新内容
     */
    public ZsmUpdateX setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     *更新链接
     */
    public ZsmUpdateX setUrl(String url) {
        this.url = url;
        return this;
    }


    /**
     * 启动Service更新
     */
    public void start(Context context) {
            Intent serviceIntent = new Intent(context, ZsmUpdateXService.class);
            serviceIntent.putExtra("title", title);
            serviceIntent.putExtra("content", content);
            serviceIntent.putExtra("url", url);
            context.startService(serviceIntent);
    }

}
