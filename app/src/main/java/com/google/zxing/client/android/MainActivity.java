package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends  Activity implements OnClickListener {

    private ImageView scanBtn;
    private TextView formatTxt, contentTxt, sampleText;
    File location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    File logFile = new File(location+"/DLScanner.log");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (ImageView)findViewById(R.id.imageView);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);

        //setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageView){

            Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");

            intent.putExtra("SCAN_MODE", "PDF417_MODE");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);

            /*Intent intent=new Intent(MainActivity.this,ScannedActivity.class);
            String result = "First Name: John\n" +
                    "Last Name: qwertyuasdasd asdsadsaa\n" +
                    "Address: 2685 Sand Rd, Arlington Heights, IL, 60070\n" +
                    "License Expiration Date: 02/2050";
            intent.putExtra("MyStudentObjectAsString", result);
            startActivityForResult(intent,2);*/

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        super.onActivityResult(requestCode, resultCode, intent);
        // check if the request code is same as what is passed  here it is 2
        /*if(requestCode==2 && intent != null)
        {
            setContentView(R.layout.content_scanned);
            intent.putExtra("MyStudentObjectAsString", "sample");
            startActivity(intent);
        }
        if(intent == null){

        }*/

        System.out.println("after scan");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String scanContent = intent.getStringExtra("SCAN_RESULT");
                String scanFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");

                appendLog(scanContent);
                String printData = formatString(scanContent);

                setContentView(R.layout.content_scanned);
                intent=new Intent(MainActivity.this,ScannedActivity.class);
                intent.putExtra("MyStudentObjectAsString", printData);
                startActivity(intent);
                //formatTxt.setText("FORMAT: " + scanFormat);
                //contentTxt.setText("" + printData);
                appendLog(printData);
                // Handle successful scan
            } else{
                System.out.println("inside else");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }

    public void appendLog(String text)
    {

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

            buf.append(text);
            buf.append("*******");
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String formatString(String extractText){

        String[] data = extractText.split("\n");
        String dataContains ="";
        for (String s : data) {
            //dataContains += ("Raw data: "+s+ "\n");
            //System.out.println("Raw data"+s);
            if(s.contains("DCS"))
                dataContains += (s.replace("DCS", "Last Name: ")+ "\n");
            if(s.contains("DAC"))
                dataContains += (s.replace("DAC", "First Name: ")+ "\n");
            if(s.contains("DAA"))
                dataContains += (s.replace("DAA", "First Name: ")+ "\n");
            if(s.contains("DBA"))
                dataContains += (s.replace("DBA", "License Expiration Date: ")+ "\n");
            if(s.contains("DAG"))
                dataContains += (s.replace("DAG", "Current Address: ")+ "\n");
            if(s.contains("DAI"))
                dataContains += (s.replace("DAI", "City: ")+ "\n");
            if(s.contains("DAJ"))
                dataContains += (s.replace("DAJ", "State: ")+ "\n");
            if(s.contains("DAK"))
                dataContains += (s.replace("DAK", "Postal code: ")+ "\n");
        }
        return dataContains;
    }


    private String getResponseText() throws IOException
    {
        StringBuilder response  = new StringBuilder();

        //URL url = new URL("https://77tjsn9jrl.execute-api.us-east-2.amazonaws.com/default/QuoteService");

        URL url = new URL("https://uje1bri222.execute-api.us-east-2.amazonaws.com/default/VehicleSearchService");
        HttpsURLConnection httpconn = (HttpsURLConnection)url.openConnection();



        httpconn.setDoOutput(true);
        httpconn.setRequestMethod("POST");
        httpconn.setRequestProperty("Content-Type", "application/json");

        String input1 = "{\"licensePlate\": \"XY56789\"}";

        OutputStream os = httpconn.getOutputStream();
        os.write(input1.getBytes());
        os.flush();

        /*if (httpconn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + httpconn.getResponseCode());
        }*/


        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }

        Log.d("Hitting URL",response.toString());
        return response.toString();
    }


}
