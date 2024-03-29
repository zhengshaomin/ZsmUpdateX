package cn.zsmupdatex.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cn.zsmupdatex.library.ZsmUpdateX;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZsmUpdateX zsmUpdateX=new ZsmUpdateX();
        zsmUpdateX.setTitle("Apk2.0")
                .setContent("Update infomation")
                .setUrl("https://github.com/zhengshaomin/ZsmTime/blob/main/screenshot.png")
                .start(this);

    }
}