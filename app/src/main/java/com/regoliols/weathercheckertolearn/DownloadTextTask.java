package com.regoliols.weathercheckertolearn;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by user on 28/02/16.
 */
public class DownloadTextTask extends AsyncTask<String, Void, String> {

    // fields
    MainActivity context;
    LinearLayout displayLayout;

    // constructor
    public DownloadTextTask(MainActivity context, LinearLayout displayLayout) {
        super();
        this.context = context;
        this.displayLayout = displayLayout;
    }

    public static InputStream openHttpGetConnection(String url) throws Exception {
        //TODO: change to Valley
        InputStream inputStream = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(new HttpGet(url));
        inputStream = httpResponse.getEntity().getContent();
        return inputStream;
    }

    // methods
    @Override
    protected String doInBackground(String... params) {
        return downloadText(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            WeatherChecker(s);
        } catch (JSONException e) {
            Log.d("Lev", "WeatherChecker failed");
            e.printStackTrace();
        }
    }

    public void WeatherChecker(String s) throws JSONException {
        Log.d("Lev", "WeatherChecker on line");
        JSONObject jsonReader = new JSONObject(s);
        if (jsonReader.has("weather")) {
            JSONArray weather = jsonReader.getJSONArray("weather");
            JSONObject clouds = weather.getJSONObject(0);
            JSONObject temperature = jsonReader.getJSONObject("main");
            final float temp = Float.valueOf(temperature.getString("temp")) - 273.15f;
            final float tempMin = Float.valueOf(temperature.getString("temp_max")) - 273.15f;
            final float tempMax = Float.valueOf(temperature.getString("temp_min")) - 273.15f;
            String[] weatherOutput = new String[5];
            weatherOutput[0] = jsonReader.getString("name");
            weatherOutput[1] = "Sky status: " + clouds.getString("main") + " : " + clouds.getString("description");
            weatherOutput[2] = "Current Temp: " + (int) temp;
            weatherOutput[3] = "Day possible temp: " + "max: " + (int) tempMax + " ,min: " + (int) tempMin;
            weatherOutput[4] = "Air humidity: " + temperature.getString("humidity") + "%";
            final TextView weatherOutputDisplay[] = new TextView[weatherOutput.length];
            for (int i = 0; i < weatherOutputDisplay.length; i++) {
                weatherOutputDisplay[i] = new TextView(context);
                weatherOutputDisplay[i].setTextColor(Color.BLACK);
                weatherOutputDisplay[i].setText(weatherOutput[i] == null ? " " : weatherOutput[i]);
                displayLayout.addView(weatherOutputDisplay[i], LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            context.unblockUI();
            Log.d("Lev", "WeatherChecker finished");
        } else if (jsonReader.has("message")) {
            String weatherOutput = jsonReader.getString("message");
            final TextView weatherOutputDisplay = new TextView(context);
            weatherOutputDisplay.setTextColor(Color.BLACK);
            weatherOutputDisplay.setText(weatherOutput == null ? " " : weatherOutput);
            displayLayout.addView(weatherOutputDisplay, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            context.unblockUI();
            Log.d("Lev", "WeatherChecker finished");
        }
    }

    public String downloadText(String url) {
        int BUFFER_SIZE = 1024;
        InputStream inputStream = null;
        try {
            inputStream = openHttpGetConnection(url);
        } catch (Exception ex) {
            Log.d("Lev", "ERROR" + ex.getMessage());
            return "ERROR";
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = inputStreamReader.read(inputBuffer)) > 0) {
                String readString = new String(inputBuffer, 0, charRead);
                str += readString;
            }
        } catch (Exception ex) {
            Log.d("Lev", "ERROR" + ex.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}
