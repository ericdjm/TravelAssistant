package service;

import domain.*;

public interface IWeatherService extends IExternalService {

    Weather getWeather(LatLng loc);
}
