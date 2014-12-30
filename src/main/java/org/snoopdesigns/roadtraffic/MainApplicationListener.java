package org.snoopdesigns.roadtraffic;

import org.snoopdesigns.roadtraffic.db.DatabaseUtils;
import org.snoopdesigns.roadtraffic.db.RoadPath;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;

public class MainApplicationListener  implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        com.objectdb.Enhancer.enhance("org.snoopdesigns.roadtraffic.db.RoadPath");
        com.objectdb.Enhancer.enhance("org.snoopdesigns.roadtraffic.db.PathStatistics");
        com.objectdb.Enhancer.enhance("org.snoopdesigns.roadtraffic.db.LearningRules");

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("$objectdb/db/manager.odb");
        DatabaseUtils utils = new DatabaseUtils(emf.createEntityManager());
        initDB(utils, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        Controller cntrlr = new Controller(utils);
        cntrlr.start();
        servletContextEvent.getServletContext().setAttribute(DatabaseUtils.class.getName(), utils);
        servletContextEvent.getServletContext().setAttribute(Controller.class.getName(), cntrlr);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DatabaseUtils utils = (DatabaseUtils) servletContextEvent.getServletContext().getAttribute(DatabaseUtils.class.getName());
        if(utils != null) {
            utils.destroy();
        }
        Controller ctnrlr = (Controller) servletContextEvent.getServletContext().getAttribute(Controller.class.getName());
        if(ctnrlr != null) {
            ctnrlr.destroy();
        }
    }

    private void initDB(DatabaseUtils dbUtils, Integer currentHour) {
        int i=1;
        for(double[][] coords : RoadPathCoordinates.pathCoordinates) {
            RoadPath path = new RoadPath(coords[0], coords[1], RoadPathSampleSpeed.pathSpeed.get(i).get(currentHour));
            path.setId(i);
            dbUtils.addNewPath(path);
            i++;
        }
    }
}
