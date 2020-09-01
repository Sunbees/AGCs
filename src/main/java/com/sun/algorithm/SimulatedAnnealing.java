package com.sun.algorithm;

import com.sun.data.Data;
import com.sun.pojo.Location;
import com.sun.pojo.Route;
import org.springframework.stereotype.Service;

@Service
public class SimulatedAnnealing {
    // 初始温度
    private double currentTemperature = 5000;
    // 最低温度
    private final double minTemperature = 0.01;
    // 某一温度下迭代次数
    private final int internalLoop = 2000;
    // 冷却比率
    private final double coolingRate = 0.01;
    // 初始解
    private Route currentRoute;

    public void initRoute() {
        Route route = Data.route;
        currentRoute = route.genIndividual();
        System.out.println("最初距离：" + currentRoute.getDistance());
    }

    public Route anneal() {
        Route bestRoute = new Route(currentRoute.getLocationList());
        Route newRoute = null;

        while (currentTemperature > minTemperature) {
            for (int i = 0; i < internalLoop; i++) {
                newRoute = currentRoute.generateNext();
                double currentEnergy = currentRoute.getDistance();
                double newEnergy = newRoute.getDistance();

                if (acceptProbability(currentEnergy, newEnergy, currentTemperature) > Math.random()) {
                    currentRoute = newRoute.clone();
                    if (currentRoute.getDistance() < bestRoute.getDistance()) {
                        bestRoute = currentRoute.clone();
                    }
                }

            }
            currentTemperature *= (1 - coolingRate);
        }
        return bestRoute;

    }

    private double acceptProbability(double currentEnergy, double newEnergy, double temperature) {
        if (newEnergy < currentEnergy)
            return 1.1;
        return Math.exp(-(newEnergy - currentEnergy) / currentTemperature);

    }

    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing();
        sa.initRoute();
        Route best = sa.anneal();
        System.out.println(best);
        System.out.println("最短距离："+best.getDistance());
    }
}
