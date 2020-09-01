package com.sun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private int x;
    private int y;

    public double distanceTo(Location otherLocation) {
        int xDistance = Math.abs(this.getX() - otherLocation.getX());
        int yDistance = Math.abs(this.getY() - otherLocation.getY());
        double distance = Math.sqrt(1.0 * xDistance * xDistance + 1.0 * yDistance * yDistance);
        return distance;
    }

}
