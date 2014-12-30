package org.snoopdesigns.roadtraffic.db;

import javax.persistence.*;
import java.util.Arrays;

@SequenceGenerator(name="seq", initialValue=1, allocationSize=100)

@Entity
public class RoadPath {

    public static final String PATH_COLOR_GREEN = "#009900";
    public static final String PATH_COLOR_YELLOW = "#FFFF33";
    public static final String PATH_COLOR_RED = "#CC0000";
    public static final String PATH_COLOR_BLACK = "#000000";

    @Id
    //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private Integer id;
    private double[] startCoords;
    private double[] endCoords;
    private Integer pathSpeed;

    public RoadPath(double[] startCoords, double[] endCoords, Integer pathSpeed) {
        this.startCoords = startCoords;
        this.endCoords = endCoords;
        this.pathSpeed = pathSpeed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double[] getStartCoords() {
        return startCoords;
    }

    public void setStartCoords(double[] startCoords) {
        this.startCoords = startCoords;
    }

    public double[] getEndCoords() {
        return endCoords;
    }

    public void setEndCoords(double[] endCoords) {
        this.endCoords = endCoords;
    }

    public Integer getPathSpeed() {
        return pathSpeed;
    }

    public void setPathSpeed(Integer pathSpeed) {
        this.pathSpeed = pathSpeed;
    }

    @Override
    public String toString() {
        return "RoadPath{" +
                "id=" + id +
                ", startCoords=" + Arrays.toString(startCoords) +
                ", endCoords=" + Arrays.toString(endCoords) +
                ", pathSpeed=" + pathSpeed +
                '}';
    }
}
