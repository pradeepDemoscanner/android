package com.scanner.dl;

import android.os.Environment;
import android.os.Bundle;




import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;


public class MainActivity /*extends  Activity implements OnClickListener*/ {/*

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
            List<String> oDesiredFormats = Arrays.asList("PDF_417".split(","));

            *//*Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);*//*


            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan(oDesiredFormats);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
//we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();



            appendLog(scanContent);
            String printData = formatString(scanContent);
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + printData);
            appendLog(printData);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
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

        String[] data = extractText.split(" ");
        String dataContains ="";
        for (String s : data) {
            //dataContains += ("Raw data: "+s+ "\n");
            //System.out.println("Raw data"+s);
            if(s.contains("DCS"))
                dataContains += (s.replace("DCS", "Last Name: ")+ "\n");
            if(s.contains("DAC"))
                dataContains += (s.replace("DAC", "First Name: ")+ "\n");
            if(s.contains("DBA"))
                dataContains += (s.replace("DBA", "License Expiration Date: ")+ "\n");
        }
        return dataContains;
    }*/
}
