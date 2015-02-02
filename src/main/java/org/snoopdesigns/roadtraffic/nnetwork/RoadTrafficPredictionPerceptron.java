package org.snoopdesigns.roadtraffic.nnetwork;

import org.easyrules.core.AnnotatedRulesEngine;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;
import org.snoopdesigns.roadtraffic.rules.RouteRule;

import java.util.*;

public class RoadTrafficPredictionPerceptron {


    private final DatabaseUtils databaseUtils;
    private Map<Integer, MultiLayerPerceptron> nnetworks = new HashMap<Integer, MultiLayerPerceptron>();

    public RoadTrafficPredictionPerceptron(DatabaseUtils databaseUtils) {
        this.databaseUtils = databaseUtils;
        for(int i=0;i<24; i++) {
            MultiLayerPerceptron network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 20, 3);
            nnetworks.put(i, network);
        }
    }

    public Integer calculateSpeedPrediction(List<Integer> currentSpeed, Integer pathId, Integer hourNumber, Integer dayOfWeekNumber) {

        /*DataSetRow dataRow = new DataSetRow(concat(createArrayOfSpeeds(currentSpeed),
                createPathIDArray(pathId),
                createWeekdayArray(dayOfWeekNumber)),
                getSpeedArray(0));
        MultiLayerPerceptron nnetwork = nnetworks.get(hourNumber);
        if(nnetwork != null) {
            nnetwork.setInput(dataRow.getInput());
            nnetwork.calculate();
            double[] networkOutput = nnetwork.getOutput();
            Integer result = getResult(networkOutput);
            if(result == 1) return 60;
            if(result == 2) return 30;
            if(result == 3) return 10;
            else return 0;
            //return result;
        } else {
            System.out.println("NETWORK IS NULL!");
            return 60;
        }*/
        Map<LearningRules, Integer> rules = new HashMap<LearningRules, Integer>();
        for(LearningRules rule : databaseUtils.getRulesByHour(hourNumber)) {
            if(rule.getPathId() == pathId) {
                Integer ruleDepth = 0;
                if(currentSpeed.get(2) == rule.getPathCurrentSpeed().get(2)) {
                    ruleDepth = 1;
                    if(currentSpeed.get(1) == rule.getPathCurrentSpeed().get(1)) {
                        ruleDepth = 2;
                        if(currentSpeed.get(0) == rule.getPathCurrentSpeed().get(0)) {
                            ruleDepth = 3;
                        }
                    }
                }
                rules.put(rule, ruleDepth);
            }
        }

        Iterator entries = rules.entrySet().iterator();
        Integer maxDepth = 0;
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Integer value = (Integer)thisEntry.getValue();
            if(value > maxDepth) maxDepth = value;
        }

        List<LearningRules> maxDepthRules = new ArrayList<LearningRules>();
        entries = rules.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            LearningRules key = (LearningRules)thisEntry.getKey();
            Integer value = (Integer)thisEntry.getValue();
            if(value == maxDepth) {
                maxDepthRules.add(key);
            }
        }

        if(maxDepth == 3) {
            Integer total = 0;
            for(LearningRules rulee : maxDepthRules) {
                total += rulee.getActualResult();
            }
            return total / maxDepthRules.size();
        } else if(maxDepth == 2) {
            return currentSpeed.get(0) + (currentSpeed.get(0) - currentSpeed.get(1));
        } else if(maxDepth == 1) {
            return currentSpeed.get(0) + (currentSpeed.get(0) - currentSpeed.get(1));
        } else if(maxDepth == 0) {
            return currentSpeed.get(0);
        }

        return 0;

    }

    public void learn() {
        System.out.println("Starting network learn....");
        for(int i=0;i<24;i++) {
            MultiLayerPerceptron nnetwork = nnetworks.get(i);
            nnetwork.reset();
            nnetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 20, 3);
            nnetwork.reset();
            nnetworks.put(i, nnetwork);

            DataSet trainingSet = new DataSet(20, 3);
            for(LearningRules rule : databaseUtils.getRulesByHour(i)) {
                List<Integer> speeds = new ArrayList<Integer>();
                for(Integer nn : rule.getPathCurrentSpeed()) {
                    speeds.add(nn);
                }
                trainingSet.addRow(new DataSetRow(concat(createArrayOfSpeeds(speeds),
                        createPathIDArray(rule.getPathId()),
                        createWeekdayArray(rule.getDayOfWeekNumber())),
                        getSpeedArray(rule.getActualResult())));
            }

            nnetwork.getLearningRule().setMaxIterations(15000);

            nnetwork.learn(trainingSet);
        }
        System.out.println("Neuro network learned successfully!");
    }

    public static void test() {



        MultiLayerPerceptron nn = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 20, 3);
        DataSet trainingSet = new DataSet(20,3);

        double[] test1 = createPathIDArray(14);
        double[] test2 = createPathIDArray(7);


        trainingSet.addRow(new DataSetRow(concat(createArrayOfSpeeds(Arrays.asList(60,60,60)),
                createPathIDArray(1),
                createWeekdayArray(1)),
                getSpeedArray(60)));

        nn.getLearningRule().setMaxIterations(500);
        nn.learn(trainingSet);

        DataSetRow dataRow = new DataSetRow(concat(createArrayOfSpeeds(Arrays.asList(60, 60, 60)),
                createPathIDArray(1),
                createWeekdayArray(1)),
                getSpeedArray(0));
        nn.setInput(dataRow.getInput());
        nn.calculate();
        double[] networkOutput = nn.getOutput();
        Integer result =  getResult(networkOutput);
        System.out.println("Output for path :" + result);
    }

    public static Integer getResult(double[] arr) {
        double max = -1;
        int result = -1;
        for(int i=0;i<3;i++) {
            if(arr[i] > max) {
                result = i+1;
                max = arr[i];
            }
        }
        return result;
    }

    public static double[] concat(double[] a, double[] b) {
        int aLen = a.length;
        int bLen = b.length;
        double[] c= new double[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static double[] concat(double[] a, double[] b, double[] c) {
        int aLen = a.length;
        int bLen = b.length;
        int cLen = c.length;
        double[] d= new double[aLen+bLen+cLen];
        System.arraycopy(a, 0, d, 0, aLen);
        System.arraycopy(b, 0, d, aLen, bLen);
        System.arraycopy(c, 0, d, aLen + bLen, cLen);
        return d;
    }

    public static double[] concat(double[] a, double[] b, double[] c, double[] d) {
        int aLen = a.length;
        int bLen = b.length;
        int cLen = c.length;
        int dLen = c.length;
        double[] r= new double[aLen+bLen+cLen+dLen];
        System.arraycopy(a, 0, r, 0, aLen);
        System.arraycopy(b, 0, r, aLen, bLen);
        System.arraycopy(c, 0, r, aLen + bLen, cLen);
        System.arraycopy(d, 0, r, aLen + bLen+cLen, dLen);
        return r;
    }

    public static double[] createWeekdayArray(Integer weekDay) {
        double[] result = new double[3];
        for(int i=0;i<3;i++) {
            result[i] = 0;
        }
        String s = Integer.toBinaryString(weekDay);
        int jj = 2;
        for(int i=s.length()-1;i>-1;i--) {
            if(s.charAt(i) == '0') result[jj] = 0; else result[jj] = 1;
            jj--;
        }
        return result;
    }

    public static double[] createPathIDArray(Integer id) {
        double[] result = new double[8];
        for(int i=0;i<8;i++) {
            result[i] = 0;
        }
        String s = Integer.toBinaryString(id);
        int jj = 7;
        for(int i=s.length()-1;i>-1;i--) {
            if(s.charAt(i) == '0') result[jj] = 0; else result[jj] = 1;
            jj--;
        }
        return result;
    }

    public static double[] createArrayOfSpeeds(List<Integer> speeds) {
        double[] result = new double[0];
        for(int i=0;i<speeds.size();i++) {
            double[] arr = getSpeedArray(speeds.get(i));
            result = concat(result, arr);
        }
        return result;
    }

    public static double[] getSpeedArray(Integer speed) {
        double[] result = new double[3];
        for(int i=0;i<3;i++) {
            result[i] = 0;
        }
        if(speed > 50) result[0] = 1;
        if(speed > 15 && speed <= 50) result[1] = 1;
        if(speed <= 15) result[2] = 1;

        return result;
    }
}
