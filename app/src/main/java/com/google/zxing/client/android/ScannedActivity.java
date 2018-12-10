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
import java.lang.String;

import javax.net.ssl.HttpsURLConnection;

public class ScannedActivity extends AppCompatActivity {

    private TextView sampleText,vehDetails, licenseExp, vehyear,
            vehmake, vehmodel, vehVin, vehMessage;
    public static TextView vehYearValue, vehMakeValue, vehModelValue, vehVinValue, driverName, driverAddress;
    private Button getQuote, getVehButton;
    private Spinner spinner1;
    private String firstName, lastName, address, expDate, city, state, postal;
    private String year, make, model, vin, yearVal, makeVal, modelVal, vinVal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyApp","I am here");
        setContentView(R.layout.content_scanned);
        //sampleText = (TextView)findViewById(R.id.scannedContent);

        String studentDataObjectAsAString = getIntent().getStringExtra("MyStudentObjectAsString");
        Log.d("MyApp",studentDataObjectAsAString);
        getVehButton = (Button)findViewById(R.id.GetVehiclebutton);
        getVehButton.setVisibility(View.VISIBLE);
        //sampleText.setText(studentDataObjectAsAString);

        String[] driverDetails = studentDataObjectAsAString.split("\n");
        for(int i=0; i<driverDetails.length; i++){
            if(driverDetails[i].contains("First")){
                int value = driverDetails[i].indexOf("First Name:");
                int subIndex = value + "First Name:".length();
                firstName = driverDetails[i].substring(subIndex);
                Log.d("First Name",firstName);
            }else if(driverDetails[i].contains("Last")){
                int value = driverDetails[i].indexOf("Last Name:");
                int subIndex = value + "Last Name:".length();
                lastName = driverDetails[i].substring(subIndex);
                Log.d("Last Name",lastName);
            }else if(driverDetails[i].contains("Address")){
                int value = driverDetails[i].indexOf("Current Address:");
                int subIndex = value + "Current Address:".length();
                address = driverDetails[i].substring(subIndex);
                Log.d("Current Address",address);
            }else if(driverDetails[i].contains("Expiration")){
                int value = driverDetails[i].indexOf("License Expiration Date:");
                int subIndex = value + "License Expiration Date:".length();
                expDate = driverDetails[i].substring(subIndex);
                Log.d("License Expiration Date",expDate);
            }else if(driverDetails[i].contains("City")){
                int value = driverDetails[i].indexOf("City:");
                int subIndex = value + "City:".length();
                city = driverDetails[i].substring(subIndex);
                Log.d("City",city);
            }else if(driverDetails[i].contains("State")){
                int value = driverDetails[i].indexOf("State:");
                int subIndex = value + "State:".length();
                state = driverDetails[i].substring(subIndex);
                Log.d("State",state);
            }else if(driverDetails[i].contains("Postal")){
                int value = driverDetails[i].indexOf("Postal Code:");
                int subIndex = value + "Postal Code:".length();
                postal = driverDetails[i].substring(subIndex);
                Log.d("Postal",postal);
            }
        }

        driverName = (TextView)findViewById(R.id.DriverNameValue);
        if(lastName != null){
            driverName.setText(firstName+ lastName);
        }else{
            driverName.setText(firstName);
        }
        if(city != null){
            address = address +","+ city;
        }
        if(state != null){
            address = address +","+ state;
        }
        if(postal != null){
            address = address +","+ postal;
        }
        driverAddress = (TextView)findViewById(R.id.DriverAddressValue);
        driverAddress.setText(address);
        licenseExp = (TextView)findViewById(R.id.DriverLicenseExpValue);
        licenseExp.setText(expDate);

        vehyear = (TextView)findViewById(R.id.VehYear);
        vehyear.setVisibility(View.INVISIBLE);
        vehmake = (TextView)findViewById(R.id.VehMake);
        vehmake.setVisibility(View.INVISIBLE);
        vehmodel = (TextView)findViewById(R.id.VehicleModel);
        vehmodel.setVisibility(View.INVISIBLE);
        vehVin = (TextView)findViewById(R.id.VehicleVin);
        vehVin.setVisibility(View.INVISIBLE);
        vehMessage = (TextView)findViewById(R.id.VehicleMessage);
        vehMessage.setVisibility(View.INVISIBLE);

        getQuote = (Button)findViewById(R.id.getQuote);
        getQuote.setVisibility(View.INVISIBLE);

        /*vehDetails = (TextView)findViewById(R.id.vehDetails);
        getQuote = (Button)findViewById(R.id.getQuote);
        if (vehDetails.getVisibility() == View.VISIBLE){
            vehDetails.setVisibility(View.INVISIBLE);
            getQuote.setVisibility(View.INVISIBLE);
        }*/

        addItemsOnSpinner2();

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    public void sendMessage(View view) throws JSONException {
        // Do something in response to button click
        EditText myVehNum = (EditText)findViewById(R.id.vehNum);
        Spinner myVehState = (Spinner) findViewById(R.id.vehState);

        try{
            String vehResponse = getResponseText(myVehNum.getText().toString());
            JSONObject jObject = new JSONObject(vehResponse);
            String aJsonString = jObject.getString("body");
            jObject = new JSONObject(aJsonString);

            if(aJsonString.contains("Vin")){
                yearVal = jObject.getString("Model Year");
                makeVal = jObject.getString("Make");
                modelVal = jObject.getString("Model");
                vinVal = jObject.getString("Vin");

                myVehNum.setVisibility(View.INVISIBLE);
                getVehButton = (Button)findViewById(R.id.GetVehiclebutton);
                getVehButton.setVisibility(View.INVISIBLE);
                myVehState.setVisibility(View.INVISIBLE);
                vehyear = (TextView)findViewById(R.id.VehYear);
                vehYearValue = (TextView)findViewById(R.id.VehYearValue);
                vehyear.setVisibility(View.VISIBLE);
                vehYearValue.setText(yearVal);
                vehmake = (TextView)findViewById(R.id.VehMake);
                vehMakeValue = (TextView)findViewById(R.id.VehMakeValue);
                vehmake.setVisibility(View.VISIBLE);
                vehMakeValue.setText(makeVal);
                vehmodel = (TextView)findViewById(R.id.VehicleModel);
                vehModelValue = (TextView)findViewById(R.id.VehicleModelValue);
                vehmodel.setVisibility(View.VISIBLE);
                vehModelValue.setText(modelVal);
                vehVin = (TextView)findViewById(R.id.VehicleVin);
                vehVinValue = (TextView)findViewById(R.id.VehicleVinValue);
                vehVin.setVisibility(View.VISIBLE);
                vehVinValue.setText(vinVal);
                vehMessage = (TextView)findViewById(R.id.VehicleMessage);
                vehMessage.setVisibility(View.INVISIBLE);

                getQuote = (Button)findViewById(R.id.getQuote);
                getQuote.setVisibility(View.VISIBLE);
            }else{
                String message = jObject.getString("Error");
                vehMessage = (TextView)findViewById(R.id.VehicleMessage);
                vehMessage.setVisibility(View.VISIBLE);
                vehMessage.setText(message);

                vehyear = (TextView)findViewById(R.id.VehYear);
                vehyear.setVisibility(View.INVISIBLE);
                vehmake = (TextView)findViewById(R.id.VehMake);
                vehmake.setVisibility(View.INVISIBLE);
                vehmodel = (TextView)findViewById(R.id.VehicleModel);
                vehmodel.setVisibility(View.INVISIBLE);
                vehVin = (TextView)findViewById(R.id.VehicleVin);
                vehVin.setVisibility(View.INVISIBLE);

                getQuote = (Button)findViewById(R.id.getQuote);
                getQuote.setVisibility(View.INVISIBLE);

                myVehNum.setVisibility(View.VISIBLE);
                getVehButton = (Button)findViewById(R.id.GetVehiclebutton);
                getVehButton.setVisibility(View.VISIBLE);
                myVehState.setVisibility(View.VISIBLE);
            }


        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

            /*JSONObject jObject = new JSONObject(vb.toString());
            String aJsonString = jObject.getString("body");
            jObject = new JSONObject(aJsonString);

            /*boolean didVehicleDataReturned = jObject.has("Info");

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
            }*/

            /*vehDetails = (TextView)findViewById(R.id.vehDetails);
            vehDetails.setText(vb);
            if (vehDetails.getVisibility() == View.INVISIBLE){
                vehDetails.setVisibility(View.VISIBLE);
                getQuote.setVisibility(View.VISIBLE);
            }*/

        /*} /*catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void addItemsOnSpinner2() {

        spinner1 = (Spinner) findViewById(R.id.vehState);
        List<String> list = new ArrayList<String>();
        list.add("Select State");
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

    public static String getVehicleDetails(){
        String data = (String)vehYearValue.getText()+ " "+(String)vehMakeValue.getText()+ " "+(String)vehModelValue.getText()
                +" "+(String)vehVinValue.getText();
        return data;
    }

    public static String getDriverName(){
        String data = (String)driverName.getText();
        return data;
    }

    public static String getAddress(){
        String data = (String)driverAddress.getText();
        return data;
    }
}
