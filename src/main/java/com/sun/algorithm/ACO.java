package com.sun.algorithm;

import com.sun.data.Data;
import com.sun.pojo.Ant;
import com.sun.pojo.Location;
import com.sun.pojo.Route;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ACO {
    private Ant[] ants; // 蚂蚁
    private int antNum = 10; // 蚂蚁数量
    private int locationNum; // 城市数量
    private int MAX_GEN = 200; // 运行代数
    private float[][] pheromone; // 信息素矩阵
    private double[][] distance; // 距离矩阵
    private double bestLength; // 最佳长度
    private int[] bestRoute; // 最佳路径
    private Map<Integer, Location> map = new HashMap<>();

    // 三个参数
    private float alpha = 1f;
    private float beta = 5f;
    private float rho = .5f;

    public ACO() {
        locationNum = Data.Location_NUM;

        ants = new Ant[antNum];

    }



    public void init() {
        distance = new double[locationNum][locationNum];

        // 计算距离矩阵
        for (int i = 0; i < locationNum; i++) {
            distance[i][i] = 0;
            map.put(i, Data.route.getLocationList().get(i));
            for (int j = i + 1; j < locationNum; j++) {
                distance[i][j] = Data.route.getLocationList().get(i).distanceTo(Data.route.getLocationList().get(j));
                distance[j][i] = distance[i][j];
            }
        }
        // 初始化信息素矩阵
        pheromone = new float[locationNum][locationNum];
        for (int i = 0; i < locationNum; i++) {
            for (int j = 0; j < locationNum; j++) {
                pheromone[i][j] = 0.1f;
            }
        }

        bestLength = Double.MAX_VALUE;
        bestRoute = new int[locationNum + 1];
        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(locationNum);
            ants[i].init(distance, alpha, beta);
        }
    }

    public Route solve() {
        // 迭代MAX_GEN次
        for (int g = 0; g < MAX_GEN; g++) {
            // antNum只蚂蚁
            for (int i = 0; i < antNum; i++) {
                // i这只蚂蚁走cityNum步，完整一个路线
                for (int j = 1; j < locationNum; j++) {
                    ants[i].selectNextLocation(pheromone);
                }
                ants[i].getTaboo().add(ants[i].getFirstLocation());
                if (ants[i].getRouteLength() < bestLength) {
                    bestLength = ants[i].getRouteLength();
                    for (int k = 0; k < locationNum + 1; k++) {
                        bestRoute[k] = ants[i].getTaboo().get(k);
                    }
                }
            }
            updatePheromone();
            // 重新初始化蚂蚁
            for (int i = 0; i < antNum; i++) {
                ants[i].init(distance, alpha, beta);
            }
        }
        // 打印最佳结果
        return printOptimal();
    }

    private void updatePheromone() {
        for (int i = 0; i < locationNum; i++) {
            for (int j = 0; j < locationNum; j++) {
                pheromone[i][j] *= (1 - rho);
            }
        }
        for (int i = 0; i < antNum; i++) {
            for (int j = 0; j < locationNum; j++) {
                pheromone[ants[i].getTaboo().get(j)][ants[i].getTaboo().get(j + 1)] += 1.0 / (ants[i].getRouteLength());
                pheromone[ants[i].getTaboo().get(j + 1)][ants[i].getTaboo().get(j)] += 1.0 / (ants[i].getRouteLength());
            }
        }
    }

    private Route printOptimal() {
        Route best = new Route();
        System.out.println("The optimal length is: " + bestLength);
        System.out.println("The optimal tour is: ");
        for (int i = 0; i < locationNum; i++) {
            System.out.print(bestRoute[i] + ">>");
        }
        System.out.println(bestRoute[locationNum]);
        for (int i = 0; i < locationNum; i++) {
            best.addLocation(map.get(bestRoute[i]));
            System.out.print(map.get(bestRoute[i]) + ">>");
        }

        return best;
    }

    public static void main(String[] args) {
        System.out.println("Start....");
        ACO aco = new ACO();
        aco.init();
        aco.solve();
    }
}
