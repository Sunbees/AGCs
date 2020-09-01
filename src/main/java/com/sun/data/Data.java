package com.sun.data;

import com.sun.pojo.Location;
import com.sun.pojo.Locations;
import com.sun.pojo.Route;


public class Data {
    static public int Location_NUM; //城市数
    static public Route route;

    static public void init(Locations locations){
        route = new Route();
        for (Location location : locations.getLocations()) {
            route.addLocation(location);
        }
        Location_NUM = route.numOfLocations();
    }

    static {
        route = new Route();
        route.addLocation(new Location(60, 200))
                .addLocation(new Location(180, 200))
                .addLocation(new Location(80, 180))
                .addLocation(new Location(140, 180))
                .addLocation(new Location(20, 160))
                .addLocation(new Location(100, 160))
                .addLocation(new Location(200, 160))
                .addLocation(new Location(140, 140))
                .addLocation(new Location(40, 120))
                .addLocation(new Location(100, 120))
                .addLocation(new Location(180, 100))
                .addLocation(new Location(60, 80))
                .addLocation(new Location(120, 80))
                .addLocation(new Location(180, 60))
                .addLocation(new Location(20, 40))
                .addLocation(new Location(100, 40))
                .addLocation(new Location(200, 40))
                .addLocation(new Location(20, 20))
                .addLocation(new Location(60, 20))
                .addLocation(new Location(160, 20));
        Location_NUM = route.numOfLocations();
    }
}
