package cn.zsmupdatex.library;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
/**
 * 作者：daboluo on 2024/3/29 17:25
 * Email:daboluo719@gmail.com
 */
public class ZsmUpdateXDownload {

    // 下载回调接口
    ondown dow;
    // 下载状态信息
    String ruest;
    // 文件总长度和当前已下载长度
    int length=0, len=0;

    // 下载回调接口定义
    public static interface ondown {
        // 下载中回调方法，参数为当前已下载长度和文件总长度
        public void downing(int len, int oklen);
        // 下载完成回调方法，参数为下载状态信息
        public void downok(String ruest);
    }

    // 设置下载回调接口
    public void setondown(ondown dow) {
        this.dow = dow;
    }

    // 下载文件方法
    public void dowmFile(final String URL, final String path) {
        new Thread() {
            public void run() {
                String url=URL;
                try {
                    // 创建目标文件路径
                    File ff=new File(path);
                    if (!ff.getParentFile().exists()) {
                        ff.getParentFile().mkdirs();
                    }
                    // 对URL进行编码处理
                    String rgx="(?<==)(.|\n)+?(?=&|$)";
                    Matcher su= Pattern.compile(rgx).matcher(url);
                    while (su.find()) {
                        String o=su.group();
                        url = url.replace(o, URLEncoder.encode(o));
                    }
                    String[] one={"{","}"};
                    for (String two:one) {
                        url = url.replace(two, URLEncoder.encode(two));
                    }
                    // 打开URL连接并获取输入流
                    URL UR=new URL(url);
                    URLConnection op=UR.openConnection();
                    InputStream input=op.getInputStream();
                    byte[] b=new byte[1024 * 1024];
                    int le=0;
                    // 获取文件总长度
                    length = op.getContentLength();
                    len = 0;
                    // 创建输出流，写入文件
                    FileOutputStream hj=new FileOutputStream(path);
                    while ((le = input.read(b)) != -1) {
                        len += le;
                        // 更新下载进度
                        if (dow != null) {
                            Message me=new Message();
                            me.what = 0;
                            hh.sendMessage(me);
                        }
                        hj.write(b, 0, le);
                    }
                    input.close();
                    hj.flush();
                    hj.close();
                    ruest = "下载完成";
                } catch (Exception e) {
                    ruest = e.toString();
                }
                // 发送下载完成消息
                Message me=new Message();
                me.what = 1;
                hh.sendMessage(me);
            }
        }.start();
    }

    // 处理下载进度和下载完成消息的Handler
    Handler hh=new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 调用回调接口，通知下载进度
                    dow.downing(length, len);
                    break;
                case 1:
                    // 调用回调接口，通知下载完成
                    dow.downok(ruest);
                    break;
            }
        }
    };
}
