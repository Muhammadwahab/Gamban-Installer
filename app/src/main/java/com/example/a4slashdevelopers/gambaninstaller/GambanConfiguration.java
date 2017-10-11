package com.example.a4slashdevelopers.gambaninstaller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GambanConfiguration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration_wait);

        new downloadFile().execute();

    }
    public class downloadFile extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects) {
            loadFile("http://110.37.231.10:8080/projects/wahab_download_test/dns.apk");



            // loadFile("http://sirius.androidapks.com/sdata/bf77952865b725cd4b1fa2c54b057bc9/com.facebook.lite_v60.0.0.5.140-72097660_Android-2.3.apk");
            return objects;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress = new ProgressDialog(MainActivity.this);
//            progress.setTitle("Loading");
//            progress.setMessage("Forget Password Please Wait...");
//            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//            progress.show();
        }
    }
    private void marshmallow(URLConnection conn) throws IOException {

        int contentLength = conn.getContentLength();

        // writing file Start
        String PATH = Environment.getExternalStorageDirectory() + "/download/";
        File file = new File(PATH);
        file.mkdirs();
        File outputFile = new File(file, "wahab.apk");
        FileOutputStream fos = new FileOutputStream(outputFile);

        FileOutputStream fout = openFileOutput("wahab.apk", Context.MODE_WORLD_READABLE);


        InputStream is = conn.getInputStream();

        byte[] buffer = new byte[contentLength];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1) {
            fout.write(buffer, 0, len1);
        }
        fout.close();
        fout.close();

        // writing file Complete

        // Read Apk file from file directory
        File myDir = getFilesDir();

        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        File file1=new File(Environment.getExternalStorageDirectory() + "/download/" + "wahab.apk");
        File secondFile = new File(myDir, "wahab.apk");
        if (file1.exists())
        {
            Log.d("Apk","read ");

            //  siliently install app
//                  String command = "adb install -r "+Environment.getExternalStorageDirectory() + "/download/wahab.apk";
//                java.lang.Process install= Runtime.getRuntime().exec((command));

//                 if (isSuccess==1)
//                 {
//
//                 }

            //  command = "adb install " + file1.getAbsolutePath();
            // Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
            //  proc.waitFor();
            // Runtime.getRuntime().exec("adb install wahab.apk");

        }
        promptInstall.setDataAndType(Uri.fromFile(secondFile), "application/vnd.android.package-archive");
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(promptInstall);

        Intent KEY_INTENT=getIntent();
        storeInFile(KEY_INTENT.getStringExtra("KEY"));


        //till here, it works fine - .apk is download to my sdcard in download file


    }
    private void loadFile(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                naogatRead(conn);
            }
            else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                marshmallow(conn);


            }
            else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // kit kat and marshmallow are same
                marshmallow(conn);

            }
            else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // kit kat and marshmallow and lollipop are same
                marshmallow(conn);

            }


            // Complete Read Apk from file Directory


            //till here, it works fine - .apk is download to my sdcard in download file

//
        } catch(FileNotFoundException e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
    private void naogatRead(URLConnection conn) throws IOException {
        File file = new File(getFilesDir(),"wahab.apk");
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        InputStream is = conn.getInputStream();
        int contentLength = conn.getContentLength();

        byte[] buffer = new byte[contentLength];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len1);
        }
        fileOutputStream.close();
        is.close();


        // writing file Complete

        // Read Apk file from file directory
        File myDir = getFilesDir();
//        progress.dismiss();



        Log.d("Naogat","Hello Naugat");
        File secondFile = new File(myDir, "wahab.apk");
        Uri apkUri = FileProvider.getUriForFile(getApplicationContext(),
                BuildConfig.APPLICATION_ID,secondFile);
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setDataAndType(apkUri,"application/vnd.android.package-archive");
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }
    public void storeInFile(String key) throws IOException {

        File dir=getFilesDir();
        File file=new File(dir+"/debugKey.txt");
        if (file.exists())
        {
            Toast.makeText(this, "File already Store in Systemss", Toast.LENGTH_SHORT).show();
        }
        FileOutputStream fout = openFileOutput("debugKey.txt", Context.MODE_WORLD_READABLE);
        fout.write(key.getBytes());
        fout.close();
    }
}
