package org.snoopdesigns.roadtraffic;

import com.google.gson.Gson;
import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.LearningRules;
import org.snoopdesigns.roadtraffic.db.PathStatistics;
import org.snoopdesigns.roadtraffic.db.RoadPath;
import org.snoopdesigns.roadtraffic.geojson.geometry.LineString;
import org.snoopdesigns.roadtraffic.geojson.geometry.Point;
import org.snoopdesigns.roadtraffic.geojson.object.Feature;
import org.snoopdesigns.roadtraffic.geojson.object.FeatureCollection;
import org.snoopdesigns.roadtraffic.geojson.object.Properties;
import org.snoopdesigns.roadtraffic.geojson.object.Style;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainApplicationServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        DatabaseUtils utils = (DatabaseUtils) getServletContext().getAttribute(DatabaseUtils.class.getName());
        Controller controller = (Controller) getServletContext().getAttribute(Controller.class.getName());

        if(request.getParameter("action") != null) {
            if (request.getParameter("action").equals("current")) {
                List<Feature> features = new ArrayList<Feature>();
                for (RoadPath path : utils.getAllPaths()) {
                    Feature lineFeature = new Feature(new LineString(Arrays.asList(
                            new Point(path.getStartCoords()[0], path.getStartCoords()[1]),
                            new Point(path.getEndCoords()[0], path.getEndCoords()[1]))));
                    lineFeature.setProperties(
                            new Properties(
                                    "Speed: " + path.getPathSpeed() + ", ID = " + path.getId(),
                                    new Style(controller.calculatePathColor(path.getPathSpeed()))));
                    lineFeature.setId(String.valueOf(path.getId()));
                    features.add(lineFeature);
                }
                out.print(new Gson().toJson(new FeatureCollection(features)));
            } else if (request.getParameter("action").equals("prediction")) {
                List<Feature> features = new ArrayList<Feature>();
                for (RoadPath path : utils.getAllPaths()) {
                    Feature lineFeature = new Feature(new LineString(Arrays.asList(
                            new Point(path.getStartCoords()[0], path.getStartCoords()[1]),
                            new Point(path.getEndCoords()[0], path.getEndCoords()[1]))));
                    Integer pathSpeedPrediction = controller.getPathSpeedPrediction(path.getPathSpeed(), path.getId());
                    lineFeature.setProperties(
                            new Properties(
                                    "Speed: " + pathSpeedPrediction + ", ID = " + path.getId(),
                                    new Style(controller.calculatePathColor(pathSpeedPrediction))));
                    lineFeature.setId(String.valueOf(path.getId()));
                    features.add(lineFeature);
                }
                out.print(new Gson().toJson(new FeatureCollection(features)));
            } else if (request.getParameter("action").equals("time")) {
                out.print("{\"time\": \"" + controller.getCurrentDatetime() + "\"}");
            } else if (request.getParameter("action").equals("predictedtime")) {
                out.print("{\"time\": \"" + controller.getPredictedDatetime() + "\"}");
            } else if (request.getParameter("action").equals("getstats")) {
                for(PathStatistics stat : utils.getAllStatistics()) {
                    out.println(stat);
                }
            } else if (request.getParameter("action").equals("getrules")) {
                out.println("Total rules size: " + utils.getAllRules().size());

                for(RoadPath path : utils.getAllPaths()) {
                    out.println("Size for path: " + path.getId() + ": " + utils.getRulesByPath(path.getId()).size());
                }

                for(LearningRules rule : utils.getAllRules()) {
                    out.println(rule);
                }
            } else if (request.getParameter("action").equals("getpathrules")) {

                for(LearningRules rule : utils.getRulesByPath(Integer.valueOf(request.getParameter("path")))) {
                    out.println(rule);
                }
            } else if (request.getParameter("action").equals("setpathspeed")) {
                RoadPathSampleSpeed.setPathSpeed(Integer.valueOf(request.getParameter("id")),
                        Integer.valueOf(request.getParameter("hour")),
                        Integer.valueOf(request.getParameter("speed")));
            } else if (request.getParameter("action").equals("setupdate")) {
                Integer interval = Integer.valueOf(request.getParameter("sec"));
                controller.reinitTimer(interval);
            }
        }
        out.flush();
        out.close();
    }

    private String getPathStats(DatabaseUtils utils, RoadPath path) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        for(PathStatistics stat : utils.getStatisticsByPath(path)) {
            sb.append(df.format(new Date(stat.getTimestamp())));
            sb.append(": ");
            sb.append(stat.getPathSpeed());
            sb.append("km/h");
            sb.append("<br>");
        }
        return sb.toString();
    }
}
