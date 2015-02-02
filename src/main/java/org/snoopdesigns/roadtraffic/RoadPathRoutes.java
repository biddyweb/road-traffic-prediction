package org.snoopdesigns.roadtraffic;

import java.util.*;

/**
 * Created by dimka on 02.02.15.
 */
public class RoadPathRoutes {

    public static final Map<Integer, List<Integer>> routePaths = new HashMap<Integer, List<Integer>>();

    static {
        routePaths.put(1,
                Arrays.asList(55,56,57,19,20,21,22,23,1,2, 3, 70,69,68,67,66,65,64));
        routePaths.put(2,
                Arrays.asList(55,56,57,28,29,30,31,8, 9,10,11,70,69,68,67,66,65,64));
    }
}
