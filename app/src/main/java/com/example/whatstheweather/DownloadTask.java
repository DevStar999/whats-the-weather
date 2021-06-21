package com.example.whatstheweather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {
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
}
