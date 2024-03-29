package cn.zsmupdatex.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.zsmupdatex.library.ZsmUpdateX;

public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivity";
    private EditText ed1,ed2,ed3,ed4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1=findViewById(R.id.editTextText);
        ed2=findViewById(R.id.editTextText2);
        ed3=findViewById(R.id.editTextText3);
        ed4=findViewById(R.id.editTextText4);

        //初始化
        ZsmUpdateX.initialize(MainActivity.this,ed1.getText().toString(),
                new ZsmUpdateX.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //初始化成功
                    }
                },
                new ZsmUpdateX.ErrorCallback() {
                    @Override
                    public void onError(String errorMessage) {
                        Log.d(TAG,errorMessage);
                    }
                }
        );

        Button button =findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ZsmUpdateX zsmUpdateX=new ZsmUpdateX();
                zsmUpdateX.setTitle(ed2.getText().toString())
                        .setContent(ed3.getText().toString())
                        .setUrl(ed4.getText().toString())
                        .start(MainActivity.this);
            }
        });


    }
}