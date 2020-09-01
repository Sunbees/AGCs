package com.sun.pojo;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private List<Route> population;
    private int speciesNum;

    public List<Route> getPopulation() {
        return population;
    }

    public void setPopulation(List<Route> population) {
        this.population = population;
    }

    public int getSpeciesNum() {
        return speciesNum;
    }

    public void setSpeciesNum(int speciesNum) {
        this.speciesNum = speciesNum;
    }

    public Population(int speciesNum){
        population = new ArrayList<>();
        this.speciesNum = speciesNum;
    }
    public void add(Route route){
        population.add(route);
    }


}
