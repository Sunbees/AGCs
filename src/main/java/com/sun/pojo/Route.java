package com.sun.pojo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Route {
    private List<Location> locationList;

    private double distance;

    public Route(List<Location> route) {
        locationList = new ArrayList<>();
        locationList.addAll(route);
    }

    public Route() {
        locationList = new ArrayList<>();
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public Route clone() {
        Route route_copy = new Route();
        route_copy.locationList.addAll(this.locationList);
        return route_copy;
    }

    public String toString() {
        String geneString = "|";
        for (int i = 0; i < numOfLocations(); i++) {
            geneString += getLocation(i) + "->";
        }
        return geneString;
    }

    public void setLocation(int locationIndex, Location location) {
        this.locationList.set(locationIndex, location);
        this.distance = 0;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Location getLocation(int locationIndex) {
        return this.locationList.get(locationIndex);
    }

    public int numOfLocations() {
        return this.locationList.size();
    }

    public Route addLocation(Location location) {
        this.locationList.add(location);
        return this;
    }



    public double getDistance() {
        if(Math.abs(distance)<=1e-6){
            double tempDistance = 0.0;
            for (int locationIndex = 0; locationIndex < numOfLocations(); locationIndex++) {
                Location fromL = getLocation(locationIndex);
                Location toL = getLocation((locationIndex+1)%numOfLocations());
                tempDistance += fromL.distanceTo(toL);
            }
            distance = tempDistance;
        }
        return distance;
    }

    /**
     * For 模拟退火
     * */

    public Route genIndividual() {
        for (int locationIndex = 0; locationIndex < locationList.size(); locationIndex++) {
            setLocation(locationIndex, this.getLocation(locationIndex));
        }
        Collections.shuffle(this.locationList);
        return this;
    }

    public Route generateNext() {
        Route newRoute = this.clone();

        int pos1 = (int)(newRoute.numOfLocations()*Math.random());
        int pos2 = (int)(newRoute.numOfLocations()*Math.random());

        if(pos1!=pos2){
            Location swap1 = newRoute.getLocation(pos1);
            Location swap2 = newRoute.getLocation(pos2);

            newRoute.setLocation(pos1,swap2);
            newRoute.setLocation(pos2,swap1);
        }
        return newRoute;
    }

    /**
     * For 遗传算法
     * */
    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void createRandomly(List<Location> locationList) {
        List<Location> list = new ArrayList<>(locationList);
        Collections.shuffle(list);
        this.locationList = list;
        this.distance = 0.0;
    }


}
