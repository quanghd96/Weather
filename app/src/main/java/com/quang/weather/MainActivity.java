package com.quang.weather;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Weather> listWeather;
    private ListView lvWeather;
    private WeatherAdapter adapter;
    private MySQLiteController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listWeather = new ArrayList<>();
        lvWeather = findViewById(R.id.listView);
        adapter = new WeatherAdapter(this, R.layout.item_weather, listWeather);
        lvWeather.setAdapter(adapter);

        controller = new MySQLiteController(this);

        if (NetworkHelper.isOnline(this)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new GetData().execute(getResources().getString(R.string.link));
                }
            });
        } else {
            ArrayList<Weather> list = controller.getWeather();
            for (int i = 0; i < list.size(); i++) {
                listWeather.add(list.get(i));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return getJson(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject root = new JSONObject(s);
                JSONArray list = root.getJSONArray("list");
                controller.deleteWeather();
                for (int i = 0; i < list.length(); i++) {
                    JSONObject object = list.getJSONObject(i);
                    JSONObject temp = object.getJSONObject("temp");
                    double tempMax = temp.getDouble("max");
                    double tempMin = temp.getDouble("min");
                    double humidity = object.getDouble("humidity");
                    JSONArray weather = object.getJSONArray("weather");
                    String weatherMain = weather.getJSONObject(0).getString("main");
                    String weatherDescription = weather.getJSONObject(0).getString("description");
                    String weatherIcon = weather.getJSONObject(0).getString("icon");
                    double speed = object.getDouble("speed");
                    Weather wea = new Weather(tempMax, tempMin, humidity, weatherMain, weatherDescription, weatherIcon, speed);
                    listWeather.add(wea);
                    adapter.notifyDataSetChanged();
                    controller.insertWeather(wea);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getJson(String url) {
        URLConnection connection = null;
        try {
            connection = (new URL(url)).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            // Read and store the result line by line then return the entire string.
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder html = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                html.append(line);
            }
            in.close();

            return html.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
