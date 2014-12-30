package org.snoopdesigns.roadtraffic;

import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;
import org.snoopdesigns.roadtraffic.db.PathStatistics;
import org.snoopdesigns.roadtraffic.db.RoadPath;
import org.snoopdesigns.roadtraffic.nnetwork.RoadTrafficPredictionPerceptron;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.*;

public class Controller {

    private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public String getCurrentDatetime() {
        return df.format(currentDatetime.getTime());
    }
    public String getPredictedDatetime() {
        Calendar incrementedTime = Calendar.getInstance(currentDatetime.getTimeZone());
        incrementedTime.setTime(currentDatetime.getTime());
        incrementedTime.add(Calendar.HOUR_OF_DAY, 1);
        return df.format(incrementedTime.getTime());
    }

    private Calendar currentDatetime;
    private ScheduledExecutorService executorService;
    private DatabaseUtils databaseUtils;
    private RoadTrafficPredictionPerceptron nnetwork;

    public Controller(DatabaseUtils utils) {
        nnetwork = new RoadTrafficPredictionPerceptron(utils);
        databaseUtils = utils;
    }

    public void start() {
        currentDatetime = Calendar.getInstance();
        currentDatetime.set(Calendar.YEAR, 2014);
        currentDatetime.set(Calendar.MONTH, 1);
        currentDatetime.set(Calendar.DAY_OF_MONTH, 20);
        currentDatetime.set(Calendar.HOUR_OF_DAY, 21);
        currentDatetime.set(Calendar.MINUTE, 00);
        currentDatetime.set(Calendar.SECOND, 00);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("============== Period tick !" + df.format(currentDatetime.getTime()));
                updateCurrentPathsSpeed();
                currentDatetime.add(Calendar.HOUR_OF_DAY, 1);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public Integer getPathSpeedPrediction(Integer currentPathSpeed, Integer pathId) {
        Integer speed = nnetwork.calculateSpeedPrediction(currentPathSpeed, pathId, currentDatetime.get(Calendar.HOUR_OF_DAY), 1/*currentDatetime.get(Calendar.DAY_OF_WEEK)*/);
        return speed;
    }

    public void destroy() {
        executorService.shutdown();
    }

    public Calendar getCurrentTime() {
        return currentDatetime;
    }

    public String calculatePathColor(Integer pathSpeed) {
        if(pathSpeed > 50) return RoadPath.PATH_COLOR_GREEN;
        if(pathSpeed > 15 && pathSpeed<=50) return RoadPath.PATH_COLOR_YELLOW;
        if(pathSpeed <= 15) return RoadPath.PATH_COLOR_RED;
        return RoadPath.PATH_COLOR_BLACK;
    }

    private void updateCurrentPathsSpeed() {
        Calendar incrementedTime = Calendar.getInstance(currentDatetime.getTimeZone());
        incrementedTime.setTime(currentDatetime.getTime());
        incrementedTime.add(Calendar.HOUR_OF_DAY, 1);
        for(RoadPath path : databaseUtils.getAllPaths()) {
            databaseUtils.addNewPathStatistics(new PathStatistics(path.getId(), currentDatetime.getTime().getTime(), path.getPathSpeed()));
            databaseUtils.addNewRule(new LearningRules(path.getId(), path.getPathSpeed(), currentDatetime.get(Calendar.HOUR_OF_DAY),
                    1/*currentDatetime.get(Calendar.DAY_OF_WEEK)*/, getPathSpeedByHour(path.getId(), incrementedTime.get(Calendar.HOUR_OF_DAY))));
            if(path.getPathSpeed() >= 10) databaseUtils.updatePathSpeed(path.getId(), getPathSpeedByHour(path.getId(), incrementedTime.get(Calendar.HOUR_OF_DAY)));
        }
        if(currentDatetime.get(Calendar.HOUR_OF_DAY) == 0) {
            nnetwork.learn();
        }
    }

    private Integer getPathSpeedByHour(Integer pathId, Integer hour) {
        return RoadPathSampleSpeed.pathSpeed.get(pathId).get(hour);
    }
}
