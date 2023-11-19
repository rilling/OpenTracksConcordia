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

    public static final String API_KEY = "fa97a8d025bc2ed677edfd981a7491b7";
    public static final String API_URL = "http://api.weatherstack.com/current";

    @Nullable
    public static WeatherInfo fetchWeatherData(double latitudeDouble, double longitudeDouble) {
        try {

            String latitude = String.valueOf(latitudeDouble);
            String longitude = String.valueOf(longitudeDouble);

            URL url = getURL(latitude, longitude);

            HttpURLConnection connection = getHttpURLConnection(url);

            StringBuilder result = getWeatherData(connection);

            JSONObject current = getJsonConverter(result);

            // Extract weather information
            double temperature = getTemperature(current);
            double windSpeed = getWindSpeed(current);
            double humidity = getHumidity(current);
            String windDirection = getWindDirection(current);

            return new WeatherInfo(temperature, windSpeed, humidity, windDirection);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getWindDirection(JSONObject current) throws JSONException {
        return current.getString("wind_dir");
    }

    private static double getHumidity(JSONObject current) throws JSONException {
        return current.getDouble("humidity");
    }

    private static double getWindSpeed(JSONObject current) throws JSONException {
        return current.getDouble("wind_speed");
    }

    private static double getTemperature(JSONObject current) throws JSONException {
        return current.getDouble("temperature");
    }

    private static JSONObject getJsonConverter(StringBuilder result) throws JSONException {
        JSONObject json = new JSONObject(result.toString());

        return json.getJSONObject("current");
    }

    private static StringBuilder getWeatherData(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result;
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

