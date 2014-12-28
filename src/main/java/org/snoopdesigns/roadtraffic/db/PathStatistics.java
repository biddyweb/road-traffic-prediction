package org.snoopdesigns.roadtraffic.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PathStatistics {

    @Id
    @GeneratedValue
    private Integer id;

    private Integer pathId;
    private Long timestamp;
    private Integer pathSpeed;

    public PathStatistics(Integer pathId, Long timestamp, Integer pathSpeed) {
        this.pathId = pathId;
        this.timestamp = timestamp;
        this.pathSpeed = pathSpeed;
    }

    public Integer getPathId() {
        return pathId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPathSpeed() {
        return pathSpeed;
    }

    public void setPathSpeed(Integer pathSpeed) {
        this.pathSpeed = pathSpeed;
    }

    @Override
    public String toString() {
        return "PathStatistics{" +
                "pathId=" + pathId +
                ", timestamp=" + timestamp +
                ", pathSpeed=" + pathSpeed +
                '}';
    }
}
