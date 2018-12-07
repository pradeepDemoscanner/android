package com.google.zxing.client.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ScannedActivity extends AppCompatActivity {

    private TextView sampleText,vehDetails;
    private Button getQuote;
    private Spinner spinner1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyApp","I am here");
        setContentView(R.layout.activity_scanned);
        sampleText = (TextView)findViewById(R.id.scannedContent);

        String studentDataObjectAsAString = getIntent().getStringExtra("MyStudentObjectAsString");
        Log.d("MyApp",studentDataObjectAsAString);
        sampleText.setText(studentDataObjectAsAString);

        vehDetails = (TextView)findViewById(R.id.vehDetails);
        getQuote = (Button)findViewById(R.id.getQuote);
        if (vehDetails.getVisibility() == View.VISIBLE){
            vehDetails.setVisibility(View.INVISIBLE);
            getQuote.setVisibility(View.INVISIBLE);
        }

        addItemsOnSpinner2();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void sendMessage(View view) throws JSONException {
        // Do something in response to button click
        EditText a = (EditText)findViewById(R.id.vehNum);

        try {
            String vb = getResponseText(a.getText().toString());

            JSONObject jObject = new JSONObject(vb.toString());
            String aJsonString = jObject.getString("body");
            jObject = new JSONObject(aJsonString);

            boolean didVehicleDataReturned = jObject.has("Info");

            if(didVehicleDataReturned){
                vb = jObject.getString("Info");
            }else{
                StringBuilder sb=new StringBuilder("Please find below vehicle details: ");
                sb.append(System.lineSeparator());
                sb.append("Model Year: "+ jObject.getString("Model Year")+System.lineSeparator());
                sb.append("Make: "+ jObject.getString("Make")+System.lineSeparator());
                sb.append("Model: "+ jObject.getString("Model")+System.lineSeparator());
                sb.append("VIN: "+ jObject.getString("Vin")+System.lineSeparator());
                vb = sb.toString();
            }

            vehDetails = (TextView)findViewById(R.id.vehDetails);
            vehDetails.setText(vb);
            if (vehDetails.getVisibility() == View.INVISIBLE){
                vehDetails.setVisibility(View.VISIBLE);
                getQuote.setVisibility(View.VISIBLE);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemsOnSpinner2() {

        spinner1 = (Spinner) findViewById(R.id.vehState);
        List<String> list = new ArrayList<String>();
        list.add("IL");
        list.add("AZ");
        list.add("VA");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
    }


    private String getResponseText(String vehDetails) throws IOException, JSONException {
        StringBuilder response  = new StringBuilder();

        //URL url = new URL("https://77tjsn9jrl.execute-api.us-east-2.amazonaws.com/default/QuoteService");

        URL url = new URL("https://uje1bri222.execute-api.us-east-2.amazonaws.com/default/VehicleSearchService");
        HttpsURLConnection httpconn = (HttpsURLConnection)url.openConnection();



        httpconn.setDoOutput(true);
        httpconn.setRequestMethod("POST");
        httpconn.setRequestProperty("Content-Type", "application/json");

        String input1 = "{\"licensePlate\": \""+ vehDetails+"\"}";

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

    public void callRates(View view) {
        Intent intent = new Intent(ScannedActivity.this, CoverageActivity.class);
        startActivity(intent);
    }
}
