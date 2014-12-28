package org.snoopdesigns.roadtraffic.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LearningRules {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer pathId;
    private Integer pathCurrentSpeed;
    private Integer hourNumber;
    private Integer dayOfWeekNumber;
    private Integer actualResult;

    public LearningRules(Integer pathId, Integer pathCurrentSpeed, Integer hourNumber, Integer dayOfWeekNumber, Integer actualResult) {
        this.pathId = pathId;
        this.pathCurrentSpeed = pathCurrentSpeed;
        this.hourNumber = hourNumber;
        this.dayOfWeekNumber = dayOfWeekNumber;
        this.actualResult = actualResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActualResult() {
        return actualResult;
    }

    public void setActualResult(Integer actualResult) {
        this.actualResult = actualResult;
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public Integer getPathCurrentSpeed() {
        return pathCurrentSpeed;
    }

    public void setPathCurrentSpeed(Integer pathCurrentSpeed) {
        this.pathCurrentSpeed = pathCurrentSpeed;
    }

    public Integer getHourNumber() {
        return hourNumber;
    }

    public void setHourNumber(Integer hourNumber) {
        this.hourNumber = hourNumber;
    }

    public Integer getDayOfWeekNumber() {
        return dayOfWeekNumber;
    }

    public void setDayOfWeekNumber(Integer dayOfWeekNumber) {
        this.dayOfWeekNumber = dayOfWeekNumber;
    }

    @Override
    public String toString() {
        return "LearningRules{" +
                "id=" + id +
                ", pathId=" + pathId +
                ", pathCurrentSpeed=" + pathCurrentSpeed +
                ", hourNumber=" + hourNumber +
                ", dayOfWeekNumber=" + dayOfWeekNumber +
                ", actualResult=" + actualResult +
                '}';
    }
}
