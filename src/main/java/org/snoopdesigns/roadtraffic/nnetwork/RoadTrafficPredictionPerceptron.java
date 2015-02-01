package org.snoopdesigns.roadtraffic.nnetwork;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;

import java.util.*;

public class RoadTrafficPredictionPerceptron {


    private final DatabaseUtils databaseUtils;
    private Map<Integer, MultiLayerPerceptron> nnetworks = new HashMap<Integer, MultiLayerPerceptron>();

    public RoadTrafficPredictionPerceptron(DatabaseUtils databaseUtils) {
        this.databaseUtils = databaseUtils;
        for(int i=0;i<24; i++) {
            MultiLayerPerceptron network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 150, 30);
            nnetworks.put(i, network);
        }
    }

    public Integer calculateSpeedPrediction(List<Integer> currentSpeed, Integer pathId, Integer hourNumber, Integer dayOfWeekNumber) {

        DataSetRow dataRow = new DataSetRow(concat(createArray(currentSpeed), parseNum(pathId), parseNum(dayOfWeekNumber)), parseNum(0));
        MultiLayerPerceptron nnetwork = nnetworks.get(hourNumber);
        if(nnetwork != null) {
            nnetwork.setInput(dataRow.getInput());
            nnetwork.calculate();
            double[] networkOutput = nnetwork.getOutput();
            Integer result = parseIntegerFromArray(networkOutput);
            //System.out.println("Output for path " + pathId + ": " + result);
            return result;
        } else {
            System.out.println("NETWORK IS NULL!");
            return 60;
        }
    }

    public void learn() {
        System.out.println("Starting network learn....");
        for(int i=0;i<24;i++) {
            MultiLayerPerceptron nnetwork = nnetworks.get(i);
            nnetwork.reset();
            nnetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 150, 30);
            nnetwork.reset();
            nnetworks.put(i, nnetwork);

            DataSet trainingSet = new DataSet(150, 30);
            for(LearningRules rule : databaseUtils.getRulesByHour(i)) {
                List<Integer> numbers = new ArrayList<Integer>();
                for(Integer nn : rule.getPathCurrentSpeed()) {
                    numbers.add(nn);
                }
                numbers.add(rule.getPathId());
                numbers.add(rule.getDayOfWeekNumber());
                trainingSet.addRow(new DataSetRow(createArray(numbers),
                        parseNum(rule.getActualResult())));
            }

            nnetwork.getLearningRule().setMaxIterations(5000);

            nnetwork.learn(trainingSet);
        }
        System.out.println("Neuro network learned successfully!");
    }

    public static void test() {

        MultiLayerPerceptron nn = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 150, 30);
        System.out.println(nn.getLayersCount());
        DataSet trainingSet = new DataSet(150, 30);


        trainingSet.addRow(new DataSetRow(createArray(Arrays.asList(10,10,60,1,1)),
                parseNum(20)));
        trainingSet.addRow(new DataSetRow(createArray(Arrays.asList(10,60,60,1,1)),
                parseNum(20)));
        trainingSet.addRow(new DataSetRow(createArray(Arrays.asList(10,60,60,1,1)),
                parseNum(60)));
        trainingSet.addRow(new DataSetRow(createArray(Arrays.asList(60,60,60,1,1)),
                parseNum(60)));

        nn.getLearningRule().setMaxIterations(500);
        nn.learn(trainingSet);

        DataSetRow dataRow = new DataSetRow(createArray(Arrays.asList(10, 60, 60, 1, 1)), parseNum(0));
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

    public static double[] createArray(List<Integer> numbers) {
        double[] result = new double[0];
        for(int i=0;i<numbers.size();i++) {
            double[] arr = parseNum(numbers.get(i));
            result = concat(result, arr);
        }
        return result;
    }
}
