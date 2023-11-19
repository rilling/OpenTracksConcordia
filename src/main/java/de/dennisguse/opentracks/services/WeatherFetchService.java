package de.dennisguse.opentracks.services;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.dennisguse.opentracks.data.models.WeatherInfo;

public class WeatherFetchService {

    
    @Nullable
    public static WeatherInfo fetchWeatherData(double latitudeDouble, double longitudeDouble) {
        try {

            String latitude = String.valueOf(latitudeDouble);
            String longitude = String.valueOf(longitudeDouble);

            URL url = getURL(latitude, longitude);

            HttpURLConnection connection = getHttpURLConnection(url);

            JSONObject current = getJsonConverter(result);

            // Extract weather information
            double temperature = null;
            double windSpeed = null;
            double humidity = null;
            String windDirection = null;

            return new WeatherInfo(temperature, windSpeed, humidity, windDirection);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

 
    private static JSONObject getJsonConverter(StringBuilder result) throws JSONException {
        JSONObject json = new JSONObject(result.toString());

        return json.getJSONObject("current");
    }

   

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

    private static URL getURL(String latitude, String longitude) throws MalformedURLException {
        return new URL(API_URL + "?access_key=" + API_KEY +
                "&query=" + latitude + "," + longitude);
    }
}
