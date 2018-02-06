package com.evancc.downloadingjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                int data = inputStream.read();

                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStream.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Log.i("JSON", result);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        try {
            String weather = task.execute("http://api.openweathermap.org/data/2.5/forecast?q=London,us&APPID=0f39e5392290578788165eeac5ae55bc").get();
            JSONObject weatherObject = new JSONObject(weather);
            //Iterator keys = weatherObject.keys();

            String weatherInfo = weatherObject.getString("list");
            Log.i("Weather Info", weatherInfo);
            JSONArray weatherArray = new JSONArray(weatherInfo);
            weatherInfo = weatherArray.get(1).toString();
            Log.i("Weather Info", weatherInfo);
            //for (int cnt = 0; cnt < weatherArray.length(); cnt++) {
            //    Log.i("Weather Info", weatherArray.get(cnt).toString());
            //}


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
