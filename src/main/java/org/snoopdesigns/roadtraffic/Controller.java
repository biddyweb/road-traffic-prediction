package org.snoopdesigns.roadtraffic;

import org.easyrules.core.AnnotatedRulesEngine;
import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;
import org.snoopdesigns.roadtraffic.db.PathStatistics;
import org.snoopdesigns.roadtraffic.db.RoadPath;
import org.snoopdesigns.roadtraffic.nnetwork.RoadTrafficPredictionPerceptron;
import org.snoopdesigns.roadtraffic.rules.RouteRule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class Controller {

    public final static Integer N_PATHS = 3;

    private final DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private int optimalRoute;

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
    private Integer currentInterval; // in seconds

    private ScheduledFuture<?> timer;

    public Controller(DatabaseUtils utils) {
        nnetwork = new RoadTrafficPredictionPerceptron(utils);
        databaseUtils = utils;
        currentInterval = 1;
    }

    public void start() {
        currentDatetime = Calendar.getInstance();
        currentDatetime.set(Calendar.YEAR, 2014);
        currentDatetime.set(Calendar.MONTH, 1);
        currentDatetime.set(Calendar.DAY_OF_MONTH, 20);
        currentDatetime.set(Calendar.HOUR_OF_DAY, 1);
        currentDatetime.set(Calendar.MINUTE, 00);
        currentDatetime.set(Calendar.SECOND, 00);
        executorService = Executors.newSingleThreadScheduledExecutor();
        timer = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateCurrentPathsSpeed();
                calculateOptimalRoute();
                currentDatetime.add(Calendar.HOUR_OF_DAY, 1);
                System.out.println("============== Period tick !" + df.format(currentDatetime.getTime()));
            }
        }, 1, currentInterval, TimeUnit.SECONDS);
    }

    public Integer getPathSpeedPrediction(Integer pathId) {
        List<Integer> pathPreviousSpeeds = new ArrayList<Integer>();
        pathPreviousSpeeds.add(databaseUtils.getPathById(pathId).getPathSpeed());
        for(PathStatistics st : databaseUtils.getStatisticsByPathId(pathId, 2)) {
            pathPreviousSpeeds.add(st.getPathSpeed());
        }
        Integer speed = nnetwork.calculateSpeedPrediction(pathPreviousSpeeds, pathId, currentDatetime.get(Calendar.HOUR_OF_DAY), 1/*currentDatetime.get(Calendar.DAY_OF_WEEK)*/);
        return speed;
    }

    public Integer getPathSpeedPrediction(Integer pathId, Integer hour) {
        List<Integer> pathPreviousSpeeds = new ArrayList<Integer>();
        for(PathStatistics st : databaseUtils.getStatisticsByPathId(pathId, 3)) {
            pathPreviousSpeeds.add(st.getPathSpeed());
        }
        Integer speed = nnetwork.calculateSpeedPrediction(pathPreviousSpeeds, pathId, hour, 1);
        return speed;
    }

    public List<Integer> getOptimalRoute() {
        return RoadPathRoutes.routePaths.get(optimalRoute);
    }

    public void reinitTimer(Integer interval) {
        timer.cancel(true);
        currentInterval = interval;
        timer = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateCurrentPathsSpeed();
                calculateOptimalRoute();
                currentDatetime.add(Calendar.HOUR_OF_DAY, 1);
                System.out.println("============== Period tick !" + df.format(currentDatetime.getTime()));
            }
        }, currentInterval, currentInterval, TimeUnit.SECONDS);
    }

    private void calculateOptimalRoute() {
        RouteRule rule = new RouteRule();
        rule.setController(this);
        AnnotatedRulesEngine rulesEngine =
                new AnnotatedRulesEngine();
        rulesEngine.registerRule(rule);
        rulesEngine.fireRules();
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

            List<Integer> pathPreviousSpeeds = new ArrayList<Integer>();
            for(PathStatistics st : databaseUtils.getStatisticsByPathId(path.getId(), 3)) {
                pathPreviousSpeeds.add(st.getPathSpeed());
            }

            databaseUtils.addNewRule(new LearningRules(path.getId(), pathPreviousSpeeds, currentDatetime.get(Calendar.HOUR_OF_DAY),
                    1/*currentDatetime.get(Calendar.DAY_OF_WEEK)*/, getPathSpeedByHour(path.getId(), incrementedTime.get(Calendar.HOUR_OF_DAY))));


            if(path.getPathSpeed() >= 10) databaseUtils.updatePathSpeed(path.getId(), getPathSpeedByHour(path.getId(), incrementedTime.get(Calendar.HOUR_OF_DAY)));
        }
        if(incrementedTime.get(Calendar.HOUR_OF_DAY) == 0) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    timer.cancel(true);
                    nnetwork.learn();
                    timer = executorService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            updateCurrentPathsSpeed();
                            calculateOptimalRoute();
                            currentDatetime.add(Calendar.HOUR_OF_DAY, 1);
                            System.out.println("============== Period tick !" + df.format(currentDatetime.getTime()));
                        }
                    }, currentInterval, currentInterval, TimeUnit.SECONDS);
                }
            };
            new Thread(r).start();
        }
    }

    private Integer getPathSpeedByHour(Integer pathId, Integer hour) {
        return RoadPathSampleSpeed.pathSpeed.get(pathId).get(hour);
    }

    public void setOptimalRoute(int i) {
        this.optimalRoute = i;
    }
}
