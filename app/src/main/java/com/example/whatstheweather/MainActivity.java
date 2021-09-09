package com.example.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    // Views
    private EditText cityEditText;
    private TextView weatherTextView;

    private void initialise() {
        cityEditText = findViewById(R.id.city_edittext);
        weatherTextView = findViewById(R.id.weather_textview);
    }

    public void getWeather(View view) {
        String cityName = cityEditText.getText().toString();
        try {
            // Adding safety for cases like when there could be spaces in the city name
            cityName = URLEncoder.encode(cityName, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String appId = "27611062ac1d743b33fb0db1f3683b09";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + appId;

        DownloadTask task = new DownloadTask();
        task.execute(url);

        // Move down the keyboard when we hit the button to get weather
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }

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
                while (data != -1) {
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

                Log.i("Info", "weatherJsonArray = " + weatherJsonArray.toString());

                for (int i = 0; i < weatherJsonArray.length(); i++) {
                    textForWeatherTextView += weatherJsonArray.getJSONObject(i)
                            .getString("main") + " : ";
                    textForWeatherTextView += weatherJsonArray.getJSONObject(i)
                            .getString("description") + "\n";
                }

                if (!textForWeatherTextView.isEmpty()) {
                    weatherTextView.setText(textForWeatherTextView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}