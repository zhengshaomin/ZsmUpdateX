package cn.zsmupdatex.library;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 作者：daboluo on 2024/3/29 17:28
 * Email:daboluo719@gmail.com
 */
public class ZsmUpdateX {

    private String title;//下载标题
    private String content;//下载内容
    private String url;//下载连接
    private String path;//下载路径
    private Boolean autoInstall=true;//默认自动安装

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
     * 更新路径
     */
    public ZsmUpdateX setPath(String path){
        this.path = path;
        return this;
    }

    /**
     * 下载完成自动安装
     */
    public ZsmUpdateX setAutoInstall(Boolean autoInstall){
        this.autoInstall=autoInstall;
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
        serviceIntent.putExtra("path",path);
        serviceIntent.putExtra("autoinstall",autoInstall);
        context.startService(serviceIntent);
    }



}
