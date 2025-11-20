package service;

import domain.*;
import java.util.List;

public interface ITransitService extends IExternalService {

    List<ETA> getTransitETAs(RouteRequest req);
}
