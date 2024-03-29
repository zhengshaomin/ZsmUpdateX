package cn.zsmupdatex.library;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;

public class ZsmUpdateXService extends Service {

    private static final String TAG = "ZsmUpdateXService";
    private static final int NOTIFICATIONID = 100001;

    private String title;//下载标题
    private String content;//下载内容
    private String url;//下载连接
    private String path;//下载路径
    private Boolean autoInstall=true;//默认自动安装
    private File apkfile;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            ifapk();//判断apk是否已经下载
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");


        if (intent != null) {
            title = intent.getStringExtra("title");
            url=intent.getStringExtra("url");
            content=intent.getStringExtra("content");
            autoInstall=intent.getBooleanExtra("autoInstall",true);
            // 根据传递的参数进行相应的处理
        }

        // 在示例中，仅模拟任务执行
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = new Message();
                handler.sendMessage(msg);
                stopSelf(); // 任务完成后停止Service
            }
        }.start();

        return START_STICKY; // 标记Service被系统关闭后自动重启
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        // 取消通知栏
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null; // 此处不需要绑定Service，因此返回null
    }

    /**
     * 判断是否已经下载
     */
    private void ifapk(){
        apkfile = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + title + ".apk");
        if(apkfile.exists()){
            //已经下载，直接安装
                installApk();

        }else {
            downloadapk();//下载
            //未下载
            sendNotification(title,content);//发送通知栏
        }
    }

    /**
     * 下载apk
     */
    private void downloadapk(){

        ZsmUpdateXDownload down = new ZsmUpdateXDownload();
        down.setondown(new ZsmUpdateXDownload.ondown() {
            @Override
            public void downing(int len, int oklen) {
                //下载ing
                updateNotificationProgress((int) (((double) oklen / (double) len) * 100));
            }
            @Override
            public void downok(String ruest) {
                //下载完成判断apk
                ifapk();
            }
        });

        down.dowmFile(url, getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + title + ".apk");///mnt/sdcard
    }
    /**
     * 更新通知栏
     */
    public void sendNotification(String title,String message) {
        //Intent intent = new Intent(this, MainActivity.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
//        // 1. 创建一个通知(必须设置channelId)
        Notification notification = new Notification.Builder(this, NOTIFICATIONID + "")
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.test)
                //.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.cjss)
                //.setContentIntent(pendingIntent) // 设置点击通知时的操作

                .setAutoCancel(true)
                .build();
        // 2. 获取系统的通知管理器
        android.app.NotificationManager notificationManager = (android.app.NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // 3. 创建NotificationChannel(这里传入的channelId要和创建的通知channelId一致，才能为指定通知建立通知渠道)
        NotificationChannel channel = new NotificationChannel(NOTIFICATIONID+"","测试渠道名称", android.app.NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
        // 4. 发送通知
        notificationManager.notify(NOTIFICATIONID, notification);
    }

    /**
     * 更新下载进度
     */
    private void updateNotificationProgress(int progress) {
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        Notification notification = new Notification.Builder(this, NOTIFICATIONID + "")
                .setContentTitle(title)
                .setContentText("下载进度：" + progress + "%")
                .setProgress(100, progress, false)
                .setSmallIcon(R.mipmap.test)
                //.setContentIntent(pendingIntent) // 设置点击通知时的操作
                .setAutoCancel(true)
                .build();
        // 2. 获取系统的通知管理器
        android.app.NotificationManager notificationManager = (android.app.NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // 3. 创建NotificationChannel(这里传入的channelId要和创建的通知channelId一致，才能为指定通知建立通知渠道)
        NotificationChannel channel = new NotificationChannel(NOTIFICATIONID+"","测试渠道名称", android.app.NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        // 发送更新后的通知
        notificationManager.notify(NOTIFICATIONID, notification);
    }

    /**
     * 安装apk
     */
    private void installApk(){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后打开新版本
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // 给目标应用一个临时授权
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
            //如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24，使用FileProvider兼容安装apk
            String packageName = getApplicationContext().getApplicationContext().getPackageName();
            String authority = new StringBuilder(packageName).append(".fileProvider").toString();
            Uri apkUri = FileProvider.getUriForFile(this, authority, apkfile);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        getApplicationContext().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());//安装完之后会提示”完成” “打开”。

    }
}