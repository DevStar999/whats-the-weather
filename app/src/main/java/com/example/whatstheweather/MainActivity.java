package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    // Views
    private EditText cityEditText;

    private void initialise() {
        cityEditText = findViewById(R.id.cityEditText);
    }

    public void getWeather(View view) {
        String cityName = cityEditText.getText().toString();
        String appId = "27611062ac1d743b33fb0db1f3683b09";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+appId;

        DownloadTask task = new DownloadTask(this);
        task.execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
    }
}