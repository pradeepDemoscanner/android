package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends  Activity implements OnClickListener {

    private ImageView scanBtn;
    private TextView formatTxt, contentTxt;
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


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageView){

            Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");

            intent.putExtra("SCAN_MODE", "PDF417_MODE");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result

        System.out.println("after scan");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String scanContent = intent.getStringExtra("SCAN_RESULT");
                String scanFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");

                appendLog(scanContent);
                String printData = formatString(scanContent);
                //formatTxt.setText("FORMAT: " + scanFormat);
                contentTxt.setText("" + printData);
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
                dataContains += (s.replace("DAG", "Current address: ")+ "\n");
            if(s.contains("DAI"))
                dataContains += (s.replace("DAI", "City: ")+ "\n");
            if(s.contains("DAJ"))
                dataContains += (s.replace("DAJ", "State: ")+ "\n");
            if(s.contains("DAK"))
                dataContains += (s.replace("DAK", "Postal code: ")+ "\n");
        }
        return dataContains;
    }
}
