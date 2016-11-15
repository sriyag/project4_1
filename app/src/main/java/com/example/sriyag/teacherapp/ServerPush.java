package com.example.sriyag.teacherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class ServerPush extends Activity {

    TextView messageText;
    EditText etIP;
    Button uploadButton, btnStoreIp;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    String IPaddress, ipAddr = "";
    Boolean IPValue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    /**********  File Path *************/
    final String uploadFilePath = Environment.getExternalStorageDirectory() + "/";
//    final String uploadFileName = "skeleton2.png";
    final String uploadFileName = "datastorage_t1_questionpaper_final.zip";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_push);

        uploadButton = (Button)findViewById(R.id.btnPushToServer);
        btnStoreIp = (Button)findViewById(R.id.btnStoreIP);
        messageText  = (TextView)findViewById(R.id.tvZippedFile);

        etIP  = (EditText) findViewById(R.id.etIP);

        sp = getSharedPreferences("mypref", 0);
        ipAddr = sp.getString("addr", "");
        etIP.setText(ipAddr);

//        detectWifiNetwork();

        //THE APP HAS TO GET THE IP ADDRESS OF THE MACHINE WHERE THE
        //XAMPP SERVER IS SET UP
        //IN THIS CASE, MAC BOOK PRO RUNNING ON WIFI DLK 80 HAS AN IP ADDRESS ...106
        //Wi-Fi is connected to dlk80 and has the IP address 192.168.0.106.
        //DLK 80 ON ANDROID PHONE WILL OBVIOUSLY SHOW A DIFFERENT IP ADDRESS
        //FOR THE SAME WIFI!!
        //DLK 80 IS JUST THE ACCESS POINT

        //Hence, get the IP address of the machine hosting the server via Settings
        //In Java, you can use InetAddress.getLocalHost() to get the Ip Address of
        //the current Server running the Java app.

        btnStoreIp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//               startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)); - settings
                    /*InetAddress address = InetAddress.getByName("Sriyas-MacBook-Pro.local");
                    Toast.makeText(getApplicationContext(), address.getHostAddress(), Toast
                            .LENGTH_LONG).show();*/ //null exception
//                    String ip =  InetAddress.getLocalHost().getHostAddress(); - null exception
                    //store in settings and retrieve next time!!!!!!
                    ipAddr = etIP.getText().toString();
                    editor = sp.edit();
                    editor.putString("addr", ipAddr);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), "ipaddr: "+ ipAddr, Toast.LENGTH_SHORT).show();

                    //Shared Preferences


                } catch (Exception e) {
                    Toast.makeText(ServerPush.this, "get ip error: "+e.getMessage(), Toast
                            .LENGTH_LONG).show();
                }
            }
        });



//        messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");
        messageText.setText("Uploading file path :- '/storage/emulated/0/"+uploadFileName+"'");

        /************* Php script path ****************/
//        upLoadServerUri = "http://172.16.72.23/uploadzip.php";
//        upLoadServerUri = "http://10.0.2.2/"; - got log exception : null
//        upLoadServerUri = "http://localhost/"; - got log exception : null
        upLoadServerUri = "http://" + ipAddr + "/uploadzip.php";

        uploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(ServerPush.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                messageText.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);

                    }
                }).start();
            }
        });
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                try {
                    dos = new DataOutputStream(conn.getOutputStream());
                } catch (Exception e) {
                    Log.e("whaa", "msg: " + e.getMessage());
                }
                //error:
                //Exception : failed to connect to /192.168.0.102 (port 80): connect failed: ENETUNREACH (Network is unreachable) java.net.ConnectException: failed to connect to /192.168.0.102 (port 80): connect failed: ENETUNREACH (Network is unreachable)
                //at libcore.io.IoBridge.connect(IoBridge.java:114)
                //at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:192)
                //at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:460)
                //error arose because Android Phone was not connected to WiFi!!!!!!

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\nSee uploaded file" + uploadFileName +
                                    "in uploads folder.";

                            messageText.setText(msg);
                            Toast.makeText(ServerPush.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(ServerPush.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (final Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception (log): " + e.getMessage());

                    }
                });
                Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    } //end of uploadFile method


    //Check the internet connection.
    private void detectWifiNetwork() {

        boolean WIFI = false;
        boolean MOBILE = false;

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                WIFI = true;
                // connected to wifi
                //tvWifiStatus.setText("WiFi detected");
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                MOBILE = true;
                // connected to the mobile provider's data plan
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
            //tvWifiStatus.setText("No WiFi detected");
            Toast.makeText(getApplicationContext(), "You are not connected to the Internet!", Toast
                    .LENGTH_SHORT).show();
        }

        if(WIFI)

        {
            IPaddress = GetDeviceipWiFiData();
           // tvIP.setText(IPaddress);


        }

        if(MOBILE)
        {

            IPaddress = GetDeviceipMobileData();
           // tvIP.setText(IPaddress);

        }

    }


    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData()
    {

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

}