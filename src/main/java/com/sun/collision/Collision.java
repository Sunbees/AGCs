package com.sun.collision;

import java.util.LinkedList;
import java.util.Queue;

class Crane {
    public String getId() {
        return id;
    }

    private String id;
    private int x;
    private boolean isUsed;
    private int status = 0; //状态：暂时0表示无载重，1表示有载重
    private Queue<Integer> toDoList;
    private int taskNo;

    public Crane(String id, int x, boolean isUsed) {
        this.id = id;
        this.x = x;
        this.isUsed = isUsed;
        toDoList = new LinkedList<>();
    }

    public int getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(int taskNo) {
        this.taskNo = taskNo;
    }

    public Queue<Integer> getToDoList() {
        return toDoList;
    }

    public Crane addTask(int location) {
        this.toDoList.offer(location);
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }


}

public class Collision {

    public static int nowTime = 0;
    public static int safeDistance = 1;

    public static void main(String[] args) {
        Crane crane1 = new Crane("ACG-1", 50, false);
        Crane crane2 = new Crane("ACG-2", 100, false);

        int[] priority = new int[]{2, 1, 0};
        int[] startTime = new int[]{0, 3, 5};
        int[] endTime = new int[startTime.length];
        int[] start = new int[]{80, 60, 50};
        int[] end = new int[]{10, 20, 40};

        Queue<Integer> taskList = new LinkedList<>();
        for (int task : priority) {
            taskList.add(task);
        }

        Crane last_crane = null;
        Crane current_crane = null;

        while (!taskList.isEmpty() || crane1.isUsed() || crane2.isUsed()) {
            if (!crane1.isUsed() && !crane2.isUsed()) {
                int current = taskList.poll();
                if (nowTime < startTime[current]) {
                    nowTime = startTime[current];
                }
                if (Math.abs(crane1.getX() - start[current]) <= Math.abs(crane2.getX() - start[current])) {
                    current_crane = crane1;
                    last_crane = crane2;
                } else {
                    current_crane = crane2;
                    last_crane = crane1;
                }
                current_crane.setUsed(true);
                current_crane.addTask(start[current]).addTask(end[current]);
                current_crane.setTaskNo(current);
            }
            if (!taskList.isEmpty() && !last_crane.isUsed()) {
                // 判断应不应该给下一个任务分配行车
                if (allocated(current_crane, last_crane, taskList.peek(), startTime)) {
                    int current = taskList.poll();
                    last_crane.setUsed(true);
                    last_crane.addTask(start[current]).addTask(end[current]);
                    last_crane.setTaskNo(current);

                    Crane temp = last_crane;
                    last_crane = current_crane;
                    current_crane = temp;
                }
            }
            // 仅有一台行车在运行
            if (!current_crane.isUsed() || !last_crane.isUsed()) {
                while (!current_crane.getToDoList().isEmpty()) {
                    int location = current_crane.getToDoList().poll();
                    if (isBetween(current_crane.getX(), last_crane.getX(), location)) {
                        System.out.print(last_crane.getId() + ":" + last_crane.getX() + "->");
                        last_crane.setX(location + safeDistance * sign(location - current_crane.getX()));
                        System.out.println(last_crane.getX());
                    }
                    System.out.print(current_crane.getId() + ":" + current_crane.getX() + "->");
                    nowTime += Math.abs(current_crane.getX() - location);
                    current_crane.setX(location);
                    System.out.println(current_crane.getX());
                }
                endTime[current_crane.getTaskNo()] = nowTime;
                current_crane.setUsed(false);

            } else {
                // 两台行车都在运行
                int lastTask = last_crane.getTaskNo();
                int currentTask = current_crane.getTaskNo();
                // 下一个任务还没开始
                if (startTime[currentTask] > nowTime) {
                    // 算出lastCrane提前运行的距离
                    int deltaTime = startTime[currentTask] - nowTime;
                    while (!last_crane.getToDoList().isEmpty()) {
                        int location = last_crane.getToDoList().peek();
                        if (deltaTime < Math.abs(last_crane.getX() - location)) {
                            System.out.print(last_crane.getId() + ":" + last_crane.getX() + "->");
                            last_crane.setX(last_crane.getX() + 1 * deltaTime * sign(location - last_crane.getX()));
                            System.out.println(last_crane.getX());
                            break;
                        } else {
                            deltaTime -= Math.abs(last_crane.getX() - location);
                            System.out.print(last_crane.getId() + ":" + last_crane.getX() + "->");
                            last_crane.setX(location);
                            System.out.println(last_crane.getX());
                            last_crane.getToDoList().poll();
                        }
                    }
                    nowTime = startTime[currentTask];
                }

                // 开始正式计算
                int lastCost = Math.abs(last_crane.getX() - last_crane.getToDoList().peek());
                int currentCost = Math.abs(current_crane.getX() - current_crane.getToDoList().peek()); // 计算不计碰撞的情况下，每辆行车完成各自任务的时间
                if (currentCost < lastCost) {  // 优先让耗时较小的行车完成其任务
                    Crane temp = last_crane;
                    last_crane = current_crane;
                    current_crane = temp;
                    lastCost = currentCost;
                }
                int lastLocation = last_crane.getToDoList().poll();
                int currentLocation = current_crane.getToDoList().peek();
                int currentX = current_crane.getX() + lastCost * 1 * sign(currentLocation - current_crane.getX());
                if (((last_crane.getX() - lastLocation) * (current_crane.getX() - currentLocation)) < 0 && (isBetween(last_crane.getX(), currentX, lastLocation) || isBetween(last_crane.getX(), current_crane.getX(), lastLocation))) {
                    currentX = lastLocation + safeDistance * sign(lastLocation - last_crane.getX());
                }
                System.out.print(current_crane.getId() + ":" + current_crane.getX() + "->");
                current_crane.setX(currentX);
                System.out.println(current_crane.getX());

                System.out.print(last_crane.getId() + ":" + last_crane.getX() + "->");
                nowTime += Math.abs(last_crane.getX() - lastLocation);
                last_crane.setX(lastLocation);
                System.out.println(last_crane.getX());
                if (last_crane.getToDoList().isEmpty()) {
                    last_crane.setUsed(false);
                    endTime[last_crane.getTaskNo()] = nowTime;
                }

            }
            System.out.println("nowTime=" + nowTime);
            System.out.println("=================================");

        }
        System.out.println(endTime[0] + " " + endTime[1] + " " + endTime[2]);
        System.out.println("nowTime=" + nowTime);
    }

    private static boolean allocated(Crane current_crane, Crane last_crane, int nextTask, int[] startTime) {
        // 下一个任务还没开始
        if (startTime[nextTask] > nowTime) {
            // currentCrane完成任务的时间
            int finishTime = 0;
            int startX = current_crane.getX();
            for (int location : current_crane.getToDoList()) {
                finishTime += Math.abs(startX - location);
                startX = location;
            }
            if (startTime[nextTask] >= nowTime + finishTime) {
                return false;
            } else {
                // 算出下一任务开始时，current_crane的位置
                int deltaTime = startTime[nextTask] - nowTime; // current可运行的时间
                startX = current_crane.getX();
                int nextLocation = 0;
                for (int location : current_crane.getToDoList()) {
                    if (deltaTime < Math.abs(startX - location)) {
                        startX = startX + 1 * deltaTime * sign(location - startX);
                        nextLocation = location;
                        break;
                    } else {
                        deltaTime -= Math.abs(startX - location);
                        startX = location;
                    }
                }
                return !(isBetween(last_crane.getX(), startX, startTime[nextTask]) && (startX - nextLocation) * (last_crane.getX() - startTime[nextTask]) < 0);
            }
        } else {
            return !(isBetween(last_crane.getX(), current_crane.getX(), startTime[nextTask]) && (current_crane.getX() - current_crane.getToDoList().peek()) * (last_crane.getX() - startTime[nextTask]) < 0);
        }
    }

    private static boolean isBetween(int x, int y, int z) {
        return (y - x) * (y - z) <= 0;
    }

    private static int sign(int x) {
        if (x > 0)
            return 1;
        else
            return -1;
    }


}
