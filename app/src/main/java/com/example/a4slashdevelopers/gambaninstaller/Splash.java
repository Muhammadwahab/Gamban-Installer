package com.example.a4slashdevelopers.gambaninstaller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.d("Splash Screen","Exception is "+e);
                }
                if (isFile())
                    startActivity(new Intent(Splash.this,ProtectedScreen.class));
                else
                {
                    Intent intent=new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
        Thread thread=new Thread(runnable);
        thread.start();
    }
    boolean isFile()
    {
        File dir=getFilesDir();
        File file=new File(dir+"/debugKey.txt");
        return file.exists();

    }
}
