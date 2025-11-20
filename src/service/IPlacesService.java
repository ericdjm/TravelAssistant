package service;

import domain.*;
import java.util.List;

public interface IPlacesService extends IExternalService {

    List<POI> searchPlaces(LatLng coords, Preferences prefs);

    LatLng geocode(String address);
}
