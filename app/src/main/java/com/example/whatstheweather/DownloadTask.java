package com.example.whatstheweather;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {
    private Context context;

    public DownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        Log.i("Info DownloadTask", "Url = " + urls[0]);

        String result = "";
        URL url;
        HttpURLConnection urlConnection;

        try {
            Log.i("Info DownloadTask", "Inside try block of doInBackground(), checkpoint 1");
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            Log.i("Info DownloadTask", "Inside try block of doInBackground(), checkpoint 2");
            while(data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            Log.i("Info DownloadTask", "Inside try block of doInBackground(), checkpoint 3");
            return result;
        } catch (Exception e) {
            Log.i("Info", "Inside catch block");
            e.printStackTrace();

            return "Process of downloading content failed";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            String textForWeatherTextView = "";
            JSONObject jsonObject = new JSONObject(result);
            JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");

            for (int i=0; i<weatherJsonArray.length(); i++) {
                textForWeatherTextView += weatherJsonArray.getJSONObject(i).getString("main");
                textForWeatherTextView += " : ";
                textForWeatherTextView += weatherJsonArray.getJSONObject(i).getString("description");
                textForWeatherTextView += "\n";
            }

            Integer textViewResourceId = context.getResources()
                    .getIdentifier("weatherTextView","id", context.getPackageName());
            TextView weatherTextView = ((Activity) context).findViewById(textViewResourceId);
            weatherTextView.setText(textForWeatherTextView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
