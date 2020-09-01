package com.sun.pojo;

import com.sun.data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant implements Cloneable {
    private List<Integer> taboo; // 禁忌表
    private List<Integer> allowed; // 允许搜索的位置
    private double[][] distance; // 距离矩阵
    private float alpha;
    private float beta;

    private double routeLength;
    private int locationNum;
    private int firstLocation;
    private int currentLocation;

    public Ant() {
        locationNum = 30;
        routeLength = 0;
    }

    public Ant(int num) {
        locationNum = num;
        routeLength = 0;
    }

    // 初始化蚂蚁，并随机选择位置
    public void init(double[][] distance, float a, float b) {
        this.alpha = a;
        this.beta = b;

        allowed = new ArrayList<>();
        taboo = new ArrayList<>();
        this.distance = distance;
        for (int i = 0; i < locationNum; i++) {
            Integer integer = i;
            allowed.add(integer);
        }
        // 随机挑选一个城市作为起始城市
        Random random = new Random(System.currentTimeMillis());
        firstLocation = random.nextInt(locationNum);
        for (Integer i : allowed) {
            if (firstLocation == i) {
                allowed.remove(i);
                break;
            }
        }
        // 将起始城市添加至禁忌表
        taboo.add(firstLocation);
        // 当前城市为起始城市
        currentLocation = firstLocation;

    }

    /**
     * 选择下一个城市
     *
     * @param pheromone 信息素矩阵
     */

    public void selectNextLocation(float[][] pheromone) {
        float[] p = new float[locationNum];
        float sum = 0.0f;
        for (Integer i : allowed) {
            sum += Math.pow(pheromone[currentLocation][i], alpha) * Math.pow(1.0 / distance[currentLocation][i], beta);
        }
        for (Integer integer : allowed) {
            p[integer] = (float) (Math.pow(pheromone[currentLocation][integer], alpha) * Math.pow(1.0 / distance[currentLocation][integer], beta) / sum);
        }

        // 轮盘赌选择下一个城市
        Random random = new Random(System.currentTimeMillis());
        float selectP = random.nextFloat();
        int selectLocation = 0;
        float sum1 = 0.f;
        for (int i = 0; i < locationNum; i++) {
            sum1 += p[i];
            if (sum1 > selectP) {
                selectLocation = i;
                break;
            }
        }
        // 从允许选择的城市中去除select city
        allowed.remove((Integer) selectLocation);
        // 在禁忌表中添加select city
        taboo.add(selectLocation);
        // 将当前城市改为选择的城市
        currentLocation = selectLocation;
    }

    private double calculateRouteLength() {
        double len = 0.0;
        for (int i = 0; i < locationNum; i++) {
            len += distance[this.taboo.get(i)][this.taboo.get(i + 1)];
        }
        return len;
    }

    public double getRouteLength() {
        this.routeLength = calculateRouteLength();
        return routeLength;
    }

    public List<Integer> getTaboo() {
        return taboo;
    }

    public int getFirstLocation() {
        return firstLocation;
    }

}
