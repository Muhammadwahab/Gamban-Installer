package com.example.a4slashdevelopers.gambaninstaller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.support.v7.app.AlertDialog;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    private Button keyButton,Cancel;
    private EditText keyText;
    Drawable drawable;
    ProgressDialog progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamban_start_screen);


        // Edit text field for getting licnece Key
        keyText= (EditText) findViewById(R.id.key);
        drawable=keyText.getBackground();
        // Submit Button
        keyButton = (Button) findViewById(R.id.EnterKeyButton);
        Cancel= (Button) findViewById(R.id.cancelid);
        // Action listner on buttons

        Cancel.setOnClickListener(this);
        keyButton.setOnClickListener(this);
        keyText.setOnTouchListener(this);

//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
    }

    @Override
    public void onClick(View view) {

        if (view.getId()==Cancel.getId())
            postDailog();
        else if (view.getId()== keyButton.getId())
        {
            new downloadFile().execute();
            // validationProcess();
        }


        // startActivity(new Intent(MainActivity.this,GambanConfiguration.class));
        // startActivity(new Intent(MainActivity.this,ProtectedScreen.class));
    }

    private void validationProcess() {
        String Key=keyText.getText().toString().trim();
        if (Key.length()==6)
        {
            if (isConnectivity())
                getResponse(Key);
            else
            {
                Log.d("Connection","No Connection Avaiable");
                Toast.makeText(this, "No Connection Avaiable", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            keyText.setText(R.string.invalid_key);
            keyText.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Please Enter Correct Licence Key", Toast.LENGTH_SHORT).show();
        }
    }

    private void getResponse(String key)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://gamban.com/sales/api/v1/checkLicenseKey?api_token=k4recZVdYzdYRkCUJrMajmZHLRq3GJf9bQ5KdwZEvXH2s1xczKuGhsn9eSuw&license_key="+key+"&os=android";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            getData(response);
                        } catch (JSONException e) {
                            Log.d("Error",e+"");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the Re
        queue.add(stringRequest);
    }
    private void getData(String response) throws JSONException, IOException {
        JSONTokener jsonTokener = new JSONTokener(response);
        JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
        String status=jsonObject.getString(UTILITY.STATUS);
        verifyStatus(status);
    }
    private void verifyStatus(String status) throws IOException {
        switch (status)
        {
            case UTILITY.ACTIVE:
                // this
                break;
            case UTILITY.UNUSED:
                // this
                // store key in file
                // storeInFile();
                new downloadFile().execute();
                break;
            case UTILITY.EXPIRED:
                // this
                break;
            case UTILITY.SCHEDULED_EXPIRED:
                // this
                break;
            default:
                // this
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //   Toast.makeText(this, "On Touch", Toast.LENGTH_SHORT).show();
        if (keyText.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.invalid_key)))
        {
            keyText.setText("");
            keyText.setBackground(drawable);
        }
        return false;
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
            if(progress != null)
                progress.dismiss();
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

    boolean isConnectivity()
    {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    public void storeInFile() throws IOException {
        String Key=keyText.getText().toString().trim();
        File dir=getFilesDir();
        File file=new File(dir+"/debugKey.txt");
        if (file.exists())
        {
            Toast.makeText(this, "File already Store in Systemss", Toast.LENGTH_SHORT).show();
        }
        FileOutputStream fout = openFileOutput("debugKey.txt", Context.MODE_WORLD_READABLE);
        fout.write(Key.getBytes());
        fout.close();
    }
    private void postDailog() {
        Button yesEvent,noEvent;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // get layout inflator for setting layout in dailog
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.cancel_layout, null);
        yesEvent=dialogView.findViewById(R.id.yesId);
        noEvent=dialogView.findViewById(R.id.noID);
        builder.setView(dialogView);
        final AlertDialog dialogUpdate = builder.create();
        yesEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Yes Click", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        noEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "no Click", Toast.LENGTH_SHORT).show();
                dialogUpdate.dismiss();

            }
        });
        dialogUpdate.show();

    }
    private void loadFilemarshmallow(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            // writing file Start
//            String PATH = Environment.getExternalStorageDirectory() + "/download/";
//            File file = new File(PATH);
//            file.mkdirs();
//            File outputFile = new File(file, "wahab.apk");
//            FileOutputStream fos = new FileOutputStream(outputFile);

            //  FileOutputStream fout = openFileOutput("wahab.apk", Context.MODE_WORLD_READABLE);

            File file = new File(getFilesDir(),"dns.apk");
            FileOutputStream fileOutputStream=new FileOutputStream(file);



            InputStream is = conn.getInputStream();

            byte[] buffer = new byte[contentLength];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len1);
            }
            fileOutputStream.close();

            // writing file Complete

            // Read Apk file from file directory
            File myDir = getFilesDir();

            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
            File file1=new File(Environment.getExternalStorageDirectory() + "/download/" + "wahab.apk");
            File secondFile = new File(myDir, "dns.apk");
            if (file1.exists())
            {
                Log.d("Apk","read ");

            }
            promptInstall.setDataAndType(Uri.fromFile(secondFile), "application/vnd.android.package-archive");
            // promptInstall.putExtra()
            promptInstall.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);

            promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(promptInstall);

            // Complete Read Apk from file Dir


            //till here, it works fine - .apk is download to my sdcard in download file


//
        } catch(FileNotFoundException e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
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


        //till here, it works fine - .apk is download to my sdcard in download file


    }
    private void loadFile(String url) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();

//            // writing file Start
//            String PATH = Environment.getExternalStorageDirectory() + "/download/";
//            File file = new File(PATH);
//            file.mkdirs();
//            File outputFile = new File(file, "wahab.apk");
            // FileOutputStream fos = new FileOutputStream(outputFile);

            //    FileOutputStream fout = openFileOutput("wahab.apk", Context.MODE_WORLD_READABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                naogatRead(conn);
            }
            else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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




}
