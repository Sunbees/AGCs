package com.sun.algorithm;

import com.sun.data.Data;
import com.sun.pojo.Location;
import com.sun.pojo.Population;
import com.sun.pojo.Route;

import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm {
    static public final int SPECIES_NUM = 200; // 种群数
    static public final int EVOLVE_NUM = 1000; // 进化代数
    static public final float pc = 0.6f; // 交叉概率
    static public final float pm = 0.05f; // 变异概率

    // 开始遗传
    public Route run(Population population) {
        createBeginningSpecies(population);
        for (int i = 1; i <= EVOLVE_NUM; i++) {
            // 选择
            select(population);

            // 交叉
            crossover(population);

            // 变异
            mutate(population);
        }
        return getBest(population);
    }

    private Route getBest(Population population) {
        Route bestRoute = null;
        double distance = Double.MAX_VALUE;
        for (Route route : population.getPopulation()) {
            if(route.getDistance()<distance){
                distance = route.getDistance();
                bestRoute = route;
            }
        }
        return bestRoute;
    }

    private void mutate(Population population) {
        for (Route route : population.getPopulation()) {
            if(Math.random()<pm){
                Random random = new Random();
                int left = random.nextInt(Data.Location_NUM);
                int right = random.nextInt(Data.Location_NUM);
                if (left > right) {
                    int tmp = left;
                    left = right;
                    right = tmp;
                }
                while (left<right){
                    Location temp = route.getLocation(left);
                    route.setLocation(left,route.getLocation(right));
                    route.setLocation(right,temp);
                    left++;
                    right--;
                }
            }
        }
    }

    private void crossover(Population population) {
        if (Math.random() <= pc) {
            Random random = new Random();
            int find = random.nextInt(population.getSpeciesNum() - 1);
            Route route1 = population.getPopulation().get(find);
            Route route2 = population.getPopulation().get(find+1);
            int begin = random.nextInt(Data.Location_NUM);
            for(int i=begin;i<Data.Location_NUM;i++){
                int first,second;
                for(first=0;!route1.getLocation(first).equals(route2.getLocation(i));first++);
                for(second=0;!route1.getLocation(i).equals(route2.getLocation(second));second++);

                Location temp = route1.getLocation(i);
                route1.setLocation(i,route2.getLocation(i));
                route2.setLocation(i,temp);

                route1.setLocation(first,route2.getLocation(i));
                route2.setLocation(second,route1.getLocation(i));
                route1.setDistance(0);
                route2.setDistance(0);
            }
        }

    }
    private void select(Population population) {
        Route bestRoute = null;
        double distance = Double.MAX_VALUE;
        for (Route route : population.getPopulation()) {
            if (route.getDistance() < distance) {
                bestRoute = route;
                distance = route.getDistance();
            }
        }

        Population newPopulation = new Population(SPECIES_NUM);
        int talentNum = SPECIES_NUM / 4;
        for (int i = 0; i < talentNum; i++) {
            newPopulation.add(bestRoute.clone());
        }

        calRate(population);
        int lastNum = SPECIES_NUM - talentNum;
        for (int i = 0; i < lastNum; i++) {
            double p = Math.random();
            boolean flag = false;
            for (Route route : population.getPopulation()) {
                if (p < route.getRate() && route != bestRoute) {
                    flag = true;
                    newPopulation.add(route.clone());
                    break;
                }
            }
            if (!flag) {
                newPopulation.add(population.getPopulation().get(SPECIES_NUM - 1).clone());
            }
        }
        Collections.shuffle(newPopulation.getPopulation());
        population.setPopulation(newPopulation.getPopulation());
    }

    private void createBeginningSpecies(Population population) {
        for (int i = 0; i < population.getSpeciesNum(); i++) {
            Route route = new Route();
            route.createRandomly(Data.route.getLocationList());
            population.add(route);
        }
    }

    private void calRate(Population population) {
        double totalFitness = 0;
        for (Route route : population.getPopulation()) {
            totalFitness += 1.0 / route.getDistance();
        }
        double tmp = 0.0;
        for (Route route : population.getPopulation()) {
            tmp += (1.0 / route.getDistance()) / totalFitness;
            route.setRate(tmp);
        }
    }

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        Population population = new Population(SPECIES_NUM);
        Route bestRoute = ga.run(population);
        System.out.println(bestRoute);
        System.out.println(bestRoute.getDistance());
    }


}
