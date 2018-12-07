package com.google.zxing.client.android;

import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CoverageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            getResponseText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getResponseText() throws IOException, JSONException {
        StringBuilder response  = new StringBuilder();

        URL url = new URL("https://77tjsn9jrl.execute-api.us-east-2.amazonaws.com/default/QuoteService");
        HttpsURLConnection httpconn = (HttpsURLConnection)url.openConnection();
        //httpconn.setDoOutput(true);
        //httpconn.setRequestMethod("GET");
        //httpconn.setRequestProperty("Content-Type", "application/json");



        OutputStream os = httpconn.getOutputStream();
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
