package org.snoopdesigns.roadtraffic.nnetwork;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoadTrafficPredictionPerceptron {


    private final DatabaseUtils databaseUtils;
    private MultiLayerPerceptron nnetwork;

    public RoadTrafficPredictionPerceptron(DatabaseUtils databaseUtils) {
        this.databaseUtils = databaseUtils;
        nnetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 120, 30);
    }

    public Integer calculateSpeedPrediction(Integer currentSpeed, Integer pathId, Integer hourNumber, Integer dayOfWeekNumber) {

        DataSetRow dataRow = new DataSetRow(concat(parseNum(currentSpeed), parseNum(pathId), parseNum(hourNumber), parseNum(dayOfWeekNumber)), parseNum(0));
        nnetwork.setInput(dataRow.getInput());
        nnetwork.calculate();
        double[] networkOutput = nnetwork.getOutput();
        Integer result =  parseIntegerFromArray(networkOutput);
        //System.out.println("Output for path " + pathId + ": " + result);
        return result;
    }

    public void learn() {
        nnetwork.reset();
        nnetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 120, 30);
        nnetwork.reset();
        DataSet trainingSet = new DataSet(120, 30);
        for(LearningRules rule : databaseUtils.getAllRules()) {
            trainingSet.addRow(new DataSetRow(concat(
                    parseNum(rule.getPathCurrentSpeed()),
                    parseNum(rule.getPathId()),
                    parseNum(rule.getHourNumber()),
                    parseNum(rule.getDayOfWeekNumber())),
                    parseNum(rule.getActualResult())));
        }

        nnetwork.getLearningRule().setMaxIterations(5000);
        System.out.println("Starting network learn....");
        nnetwork.learn(trainingSet);
        System.out.println("Neuro network learned successfully!");
    }

    public static void test() {
        MultiLayerPerceptron nn = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 120, 30);
        DataSet trainingSet = new DataSet(120, 30);

        trainingSet.addRow(new DataSetRow(concat(
                parseNum(60),
                parseNum(1),
                parseNum(10),
                parseNum(1)),
                parseNum(60)));
        trainingSet.addRow(new DataSetRow(concat(
                parseNum(60),
                parseNum(1),
                parseNum(10),
                parseNum(1)),
                parseNum(10)));

        nn.getLearningRule().setMaxIterations(1000);
        nn.learn(trainingSet);

        DataSetRow dataRow = new DataSetRow(concat(parseNum(60), parseNum(1), parseNum(10), parseNum(1)), parseNum(0));
        nn.setInput(dataRow.getInput());
        nn.calculate();
        double[] networkOutput = nn.getOutput();
        Integer result =  parseIntegerFromArray(networkOutput);
        System.out.println("Output for path :" + result);
    }

    public static Integer parseIntegerFromArray(double[] array) {

        List<Integer> tens = new ArrayList<Integer>();
        List<Double> tmp = new ArrayList<Double>();
        for(int i=10;i<20;i++) {
            tmp.add(array[i]);
        }
        double maxTensValue = Collections.max(tmp);
        for(int i=10;i<20;i++) {
            if(array[i] > maxTensValue / 3 * 2) {
                tens.add(i-10);
            }
        }

        tmp.clear();
        List<Integer> units = new ArrayList<Integer>();

        for(int i=20;i<30;i++) {
            tmp.add(array[i]);
        }
        double maxUnitsValue = Collections.max(tmp);

        for(int i=20;i<30;i++) {
            tmp.add(array[i]);
            if(array[i] > maxUnitsValue / 3 * 2) {
                units.add(i-20);
            }
        }



        int minTens = Collections.min(tens);
        int maxTens = Collections.max(tens);
        int minUnits = Collections.min(units);
        int maxUnits = Collections.max(units);

        int ten = (minTens + maxTens) / 2;
        int unit = (minUnits + maxUnits) / 2;

        return ((10 * (minTens) + minUnits) + (10 * (maxTens) + maxUnits)) / 2;
    }

    /*public Integer parseIntegerFromArray(double[] array) {
        double max = -1;
        int maxIndex = 0;
        for(int i=0;i<10;i++) {
            if(array[i] > max) {
                max = array[i];
                maxIndex = i;
            }
        }
        int hundreds = maxIndex;

        max = -1;
        maxIndex = 0;

        for(int i=10;i<20;i++) {
            if(array[i] > max) {
                max = array[i];
                if(array[i] > 0.1) System.out.println("Max: " + Integer.valueOf(i-10) + ": " + array[i]);
                maxIndex = i-10;
            }
        }
        int tens = maxIndex;

        max = -1;
        maxIndex = 0;

        for(int i=20;i<30;i++) {
            if(array[i] > max) {
                max = array[i];
                maxIndex = i-20;
            }
        }
        int units = maxIndex;

        return 100 * hundreds + 10 * tens + units;
    }*/

    public static double[] parseNum(Integer num) {
        Integer first = 0;
        Integer second = 0;
        Integer third;
        if(num < 10) return concat(convertNumberToArray(0), convertNumberToArray(0), convertNumberToArray(num));
        else {
            third = num % 10;
            num /= 10;
            second = num % 10;
            num /= 10;
            first = num % 10;
            return concat(convertNumberToArray(first), convertNumberToArray(second), convertNumberToArray(third));
        }
    }

    private static double[] convertNumberToArray(Integer num) {
        if(num == 0) return new double[] {1,0,0,0,0,0,0,0,0,0};
        if(num == 1) return new double[] {0,1,0,0,0,0,0,0,0,0};
        if(num == 2) return new double[] {0,0,1,0,0,0,0,0,0,0};
        if(num == 3) return new double[] {0,0,0,1,0,0,0,0,0,0};
        if(num == 4) return new double[] {0,0,0,0,1,0,0,0,0,0};
        if(num == 5) return new double[] {0,0,0,0,0,1,0,0,0,0};
        if(num == 6) return new double[] {0,0,0,0,0,0,1,0,0,0};
        if(num == 7) return new double[] {0,0,0,0,0,0,0,1,0,0};
        if(num == 8) return new double[] {0,0,0,0,0,0,0,0,1,0};
        if(num == 9) return new double[] {0,0,0,0,0,0,0,0,0,1};
        return null;
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
}
