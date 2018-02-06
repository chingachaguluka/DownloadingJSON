package com.evancc.downloadingjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    EditText editText = null;
    TextView mainTextView = null;
    TextView weatherTextView = null;
    String weatherURL = "";

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

    public void onButtonClick(View view) {
        //Clearing previous results
        mainTextView.setText("-");
        weatherTextView.setText("-");

        if(editText.getText().length() > 0) {
            weatherURL = "http://api.openweathermap.org/data/2.5/forecast?q=" + editText.getText() + "&APPID=0f39e5392290578788165eeac5ae55bc";

            DownloadTask task = new DownloadTask();
            try {
                String weather = task.execute(weatherURL).get();
                JSONObject weatherObject = new JSONObject(weather);
                //Iterator keys = weatherObject.keys();

                String weatherInfo = weatherObject.getString("list");
                Log.i("Weather Info", weatherInfo);
                JSONArray weatherArray = new JSONArray(weatherInfo);
                JSONObject currentWeather = weatherArray.getJSONObject(1);
                weatherInfo = currentWeather.getString("weather");
                weatherArray = new JSONArray(weatherInfo);
                currentWeather = weatherArray.getJSONObject(0);

                //Log.i("Main", currentWeather.getString("main"));
                //Log.i("Description", currentWeather.getString("description"));
                mainTextView.setText(currentWeather.getString("main"));
                weatherTextView.setText(currentWeather.getString("description"));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        mainTextView = (TextView) findViewById(R.id.mainTextView);
        weatherTextView = (TextView) findViewById(R.id.weatherTextView);



    }
}
