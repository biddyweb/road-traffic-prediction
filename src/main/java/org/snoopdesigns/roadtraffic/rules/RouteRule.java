package org.snoopdesigns.roadtraffic.rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.snoopdesigns.roadtraffic.Controller;
import org.snoopdesigns.roadtraffic.RoadPathRoutes;
import org.snoopdesigns.roadtraffic.db.RoadPath;

@Rule(name = "Route rule",
        description = "This is route definition rule")
public class RouteRule {

    private int opt = -1;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private Controller controller;

    @Condition
    public boolean checkInput() {
        int total1 = 0;
        int total2 = 0;
        for(Integer pathId : RoadPathRoutes.routePaths.get(1)) {
            Integer pathSpeed = controller.getPathSpeedPrediction(pathId);
            if(pathSpeed > 15 && pathSpeed<=50) total1 += 1;
            if(pathSpeed <= 15) total1 += 2;
        }

        for(Integer pathId : RoadPathRoutes.routePaths.get(2)) {
            Integer pathSpeed = controller.getPathSpeedPrediction(pathId);
            if(pathSpeed > 15 && pathSpeed<=50) total2 += 1;
            if(pathSpeed <= 15) total2 += 2;
        }

        if(total1 != total2) {
            if (total1 > total2) opt = 2;
            else opt = 1;
        } else {
            if(RoadPathRoutes.routePaths.get(1).size() > RoadPathRoutes.routePaths.get(2).size()) opt = 2; else opt = 1;
        }

        return true;
    }

    @Action
    public void sayHelloToDukeFriend() throws Exception {
        controller.setOptimalRoute(opt);
    }
}
