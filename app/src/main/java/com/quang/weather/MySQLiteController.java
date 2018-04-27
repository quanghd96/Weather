package com.quang.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLiteController extends SQLiteOpenHelper {
    private static final String DB_NAME = "data.sqlite";
    private static final String TABLE_WEATHER = "weather";

    public MySQLiteController(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  tempMax DOUBLE," +
                "  tempMin DOUBLE," +
                "  humidity DOUBLE," +
                "  weatherMain TEXT," +
                "  weatherDescription TEXT," +
                "  weatherIcon TEXT," +
                "  speed DOUBLE" +
                ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        onCreate(db);
    }

    public ArrayList<Weather> getWeather() {
        ArrayList<Weather> listWeather = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER, null);
            while (cursor.moveToNext()) {
                double tempMax = cursor.getDouble(1);
                double tempMin = cursor.getDouble(2);
                double humidity = cursor.getDouble(3);
                String weatherMain = cursor.getString(4);
                String weatherDescription = cursor.getString(5);
                String weatherIcon = cursor.getString(6);
                double speed = cursor.getDouble(7);
                listWeather.add(new Weather(tempMax, tempMin, humidity, weatherMain, weatherDescription, weatherIcon, speed));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return listWeather;
    }

    public boolean insertWeather(Weather weather) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content = new ContentValues();
            content.put("tempMax", weather.getTempMax());
            content.put("tempMin", weather.getTempMin());
            content.put("humidity", weather.getHumidity());
            content.put("weatherMain", weather.getWeatherMain());
            content.put("weatherDescription", weather.getWeatherDescription());
            content.put("weatherIcon", weather.getWeatherIcon());
            content.put("speed", weather.getSpeed());
            if (db.insert(TABLE_WEATHER, null, content) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    public boolean deleteWeather() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db.delete(TABLE_WEATHER, null, null) == -1) {
                close();
                return false;
            }
            close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
