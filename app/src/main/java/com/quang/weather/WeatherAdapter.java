package com.quang.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherAdapter extends ArrayAdapter<Weather> {

    private ArrayList<Weather> listWeather;
    private Context context;
    private int layoutId;

    public WeatherAdapter(@NonNull Context context, int resource, ArrayList<Weather> listWeather) {
        super(context, resource);
        this.listWeather = listWeather;
        this.context = context;
        this.layoutId = resource;
    }

    @Override
    public int getCount() {
        return listWeather.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutId, parent, false);
        if (listWeather.size() > 0 && position >= 0) {
            ImageView imvIcon = convertView.findViewById(R.id.imvIcon);
            Glide.with(convertView).load(convertView.getResources().getString(R.string.linkIcon) + listWeather.get(position).getWeatherIcon() + ".png").into(imvIcon);
            TextView tvTemp = convertView.findViewById(R.id.tvTemp);
            tvTemp.setText("Temperature: " + listWeather.get(position).getTempMin() + " - " + listWeather.get(position).getTempMax() + " °C");
            TextView tvHumidity = convertView.findViewById(R.id.tvHumidity);
            tvHumidity.setText("Humidity: " + listWeather.get(position).getHumidity() + " %");
            TextView tvSpeed = convertView.findViewById(R.id.tvSpeed);
            tvSpeed.setText("Wind speed: " + listWeather.get(position).getSpeed() + " km/h");
            TextView tvWeatherMain = convertView.findViewById(R.id.tvWeatherMain);
            tvWeatherMain.setText("Weather: " + listWeather.get(position).getWeatherMain());
            TextView tvWeatherDescription = convertView.findViewById(R.id.tvWeatherDescription);
            tvWeatherDescription.setText("Description: " + listWeather.get(position).getWeatherDescription());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            TextView tvDate = convertView.findViewById(R.id.tvDate);
            tvDate.setText(simpleDateFormat.format(new Date().getTime() + (1000 * 60 * 60 * 24) * position));
        }
        return convertView;
    }
}
