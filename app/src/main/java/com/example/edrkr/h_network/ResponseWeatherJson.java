package com.example.edrkr.h_network;

public class ResponseWeatherJson {

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }
    public String getWeather_imgurl() { return weather_imgurl; }
    public void setWeather_imgurl(String weather_imgurl) { this.weather_imgurl = weather_imgurl; }

    private String weather;
    private String weather_imgurl;
}
