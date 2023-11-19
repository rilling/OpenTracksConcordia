package de.dennisguse.opentracks.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class WeatherInfo implements Parcelable {

    private Track.Id id;
    private double temperature;
    private double windSpeed;

    private double humidity;
    private String windDirection;

    public WeatherInfo(double temperature, double windSpeed, double humidity, String windDirection) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.windDirection = windDirection;
    }

    protected WeatherInfo(Parcel in) {
        temperature = in.readDouble();
        windSpeed = in.readDouble();
        humidity = in.readDouble();
        windDirection = in.readString();
    }

    public static final Creator<WeatherInfo> CREATOR = new Creator<WeatherInfo>() {
        @Override
        public WeatherInfo createFromParcel(Parcel in) {
            return new WeatherInfo(in);
        }

        @Override
        public WeatherInfo[] newArray(int size) {
            return new WeatherInfo[size];
        }
    };

    @Nullable
    public Track.Id getId() {
        return id;
    }

    public void setId(Track.Id id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeatherInfo{" +
                "temperature=" + temperature +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(temperature);
        parcel.writeDouble(windSpeed);
        parcel.writeString(windDirection);
    }
}
