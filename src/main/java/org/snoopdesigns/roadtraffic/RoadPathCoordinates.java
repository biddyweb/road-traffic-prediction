package org.snoopdesigns.roadtraffic;

import java.util.ArrayList;
import java.util.List;

public class RoadPathCoordinates {
    public static final List<double[][]> pathCoordinates = new ArrayList<double[][]>(); /*= Arrays.asList(
            new double[][] { new double[]{30.420817,59.898037},new double[]{30.423564,59.896374} }
            new double[][] { new double[]{30.420817,59.898037},new double[]{30.419090,59.899032} },
            new double[][] { new double[]{30.418814,59.898935},new double[]{30.420542,59.897918} },
            new double[][] { new double[]{30.420542,59.897918},new double[]{30.423331,59.896277} },
            new double[][] { new double[]{30.413321,59.897773},new double[]{30.413600,59.897869} },
            new double[][] { new double[]{30.413600,59.897869},new double[]{30.416336,59.898434} },
            new double[][] { new double[]{30.416336,59.898434},new double[]{30.418782,59.898929} },
            new double[][] { new double[]{30.418589,59.899080},new double[]{30.415971,59.898515} },
            new double[][] { new double[]{30.415971,59.898515},new double[]{30.413471,59.897982} },
            new double[][] { new double[]{30.413503,59.897988},new double[]{30.411486,59.897197} },
            new double[][] { new double[]{30.411486,59.897197},new double[]{30.409716,59.896438} },
            new double[][] { new double[]{30.409845,59.896357},new double[]{30.413332,59.897783} },
            new double[][] { new double[]{30.413391,59.897781},new double[]{30.415800,59.896361} },
            new double[][] { new double[]{30.415735,59.896321},new double[]{30.413289,59.897758} },
            new double[][] { new double[]{30.415800,59.896361},new double[]{30.418104,59.894533} },
            new double[][] { new double[]{30.415735,59.896321},new double[]{30.417975,59.894490} },
            new double[][] { new double[]{30.423254,59.896303},new double[]{30.420443,59.895356} },
            new double[][] { new double[]{30.420443,59.895356},new double[]{30.417664,59.894415} },
            new double[][] { new double[]{30.417664,59.894415},new double[]{30.414638,59.893451} },
            new double[][] { new double[]{30.414638,59.893451},new double[]{30.412847,59.894554} },
            new double[][] { new double[]{30.412847,59.894554},new double[]{30.412847,59.894554} },
            new double[][] { new double[]{30.412847,59.894554},new double[]{30.409918,59.896363} },
            new double[][] { new double[]{30.409692,59.896390},new double[]{30.409692,59.896390} },
            new double[][] { new double[]{30.409692,59.896390},new double[]{30.413469,59.894076} },
            new double[][] { new double[]{30.413469,59.894076},new double[]{30.414595,59.893354} },
            new double[][] { new double[]{30.414595,59.893354},new double[]{30.416720,59.894022} },
            new double[][] { new double[]{30.416720,59.894022},new double[]{30.419016,59.894786} },
            new double[][] { new double[]{30.419016,59.894786},new double[]{30.421719,59.895706} },
            new double[][] { new double[]{30.421719,59.895706},new double[]{30.423640,59.896325} },
            new double[][] { new double[]{30.420561,59.897915},new double[]{30.418318,59.896898} },
            new double[][] { new double[]{30.418318,59.896898},new double[]{30.416291,59.896010} }
    );*/

    static {
        pathCoordinates.add(new double[][]{new double[]{59.896380, 30.423562}, new double[]{59.897156, 30.422194}});
        pathCoordinates.add(new double[][]{new double[]{59.897156, 30.422194}, new double[]{59.898033, 30.420789}});
        pathCoordinates.add(new double[][]{new double[]{59.898033, 30.420789}, new double[]{59.899147, 30.418836}});
        /*pathCoordinates.add(new double[][]{new double[]{59.899147, 30.418836}, new double[]{59.898533, 30.416100}});
        pathCoordinates.add(new double[][]{new double[]{59.898533, 30.416100}, new double[]{59.898006, 30.413482}});
        pathCoordinates.add(new double[][]{new double[]{59.898006, 30.413482}, new double[]{59.897145, 30.411369}});
        pathCoordinates.add(new double[][]{new double[]{59.897145, 30.411369}, new double[]{59.896435, 30.409663}});
        pathCoordinates.add(new double[][]{new double[]{59.896359, 30.409845}, new double[]{59.897150, 30.411798}});
        pathCoordinates.add(new double[][]{new double[]{59.897150, 30.411798}, new double[]{59.897828, 30.413429}});
        pathCoordinates.add(new double[][]{new double[]{59.897828, 30.413429}, new double[]{59.898431, 30.416400}});
        pathCoordinates.add(new double[][]{new double[]{59.898431, 30.416400}, new double[]{59.898937, 30.418793}});
        pathCoordinates.add(new double[][]{new double[]{59.898937, 30.418793}, new double[]{59.897898, 30.420552}});
        pathCoordinates.add(new double[][]{new double[]{59.897898, 30.420552}, new double[]{59.897064, 30.421990}});
        pathCoordinates.add(new double[][]{new double[]{59.897064, 30.421990}, new double[]{59.896268, 30.423342}});
        pathCoordinates.add(new double[][]{new double[]{59.896281, 30.423264}, new double[]{59.895698, 30.421499}});
        pathCoordinates.add(new double[][]{new double[]{59.895698, 30.421499}, new double[]{59.894532, 30.418015}});
        pathCoordinates.add(new double[][]{new double[]{59.894532, 30.418015}, new double[]{59.893894, 30.416111}});
        pathCoordinates.add(new double[][]{new double[]{59.893894, 30.416111}, new double[]{59.893434, 30.414627}});
        pathCoordinates.add(new double[][]{new double[]{59.893392, 30.414609}, new double[]{59.893856, 30.416151}});
        pathCoordinates.add(new double[][]{new double[]{59.893856, 30.416151}, new double[]{59.894304, 30.417495}});
        pathCoordinates.add(new double[][]{new double[]{59.894304, 30.417495}, new double[]{59.894865, 30.419198}});
        pathCoordinates.add(new double[][]{new double[]{59.894865, 30.419198}, new double[]{59.895654, 30.421556}});
        pathCoordinates.add(new double[][]{new double[]{59.895654, 30.421556}, new double[]{59.896255, 30.423312}});
        pathCoordinates.add(new double[][]{new double[]{59.896429, 30.409633}, new double[]{59.895213, 30.411631}});
        pathCoordinates.add(new double[][]{new double[]{59.895213, 30.411631}, new double[]{59.894177, 30.413316}});
        pathCoordinates.add(new double[][]{new double[]{59.894177, 30.413316}, new double[]{59.893738, 30.414024}});
        pathCoordinates.add(new double[][]{new double[]{59.893738, 30.414024}, new double[]{59.893410, 30.414518}});
        pathCoordinates.add(new double[][]{new double[]{59.893437, 30.414614}, new double[]{59.894121, 30.413536}});
        pathCoordinates.add(new double[][]{new double[]{59.894121, 30.413536}, new double[]{59.894995, 30.412130}});
        pathCoordinates.add(new double[][]{new double[]{59.894995, 30.412130}, new double[]{59.895764, 30.410859}});
        pathCoordinates.add(new double[][]{new double[]{59.895764, 30.410859}, new double[]{59.896469, 30.409727}});
        pathCoordinates.add(new double[][]{new double[]{59.898587, 30.405951}, new double[]{59.897823, 30.407302}});
        pathCoordinates.add(new double[][]{new double[]{59.897823, 30.407302}, new double[]{59.897010, 30.408676}});
        pathCoordinates.add(new double[][]{new double[]{59.897010, 30.408676}, new double[]{59.896394, 30.409690}});
        pathCoordinates.add(new double[][]{new double[]{59.896437, 30.409775}, new double[]{59.897241, 30.408445}});
        pathCoordinates.add(new double[][]{new double[]{59.897241, 30.408445}, new double[]{59.897968, 30.407200}});
        pathCoordinates.add(new double[][]{new double[]{59.896348, 30.423605}, new double[]{59.896814, 30.424989}});
        pathCoordinates.add(new double[][]{new double[]{59.896814, 30.424989}, new double[]{59.897319, 30.426496}});
        pathCoordinates.add(new double[][]{new double[]{59.897319, 30.426496}, new double[]{59.897780, 30.427848}});
        pathCoordinates.add(new double[][]{new double[]{59.897780, 30.427848}, new double[]{59.898651, 30.430498}});
        pathCoordinates.add(new double[][]{new double[]{59.898697, 30.430439}, new double[]{59.897823, 30.427805}});
        pathCoordinates.add(new double[][]{new double[]{59.897823, 30.427805}, new double[]{59.897330, 30.426335}});
        pathCoordinates.add(new double[][]{new double[]{59.897330, 30.426335}, new double[]{59.896892, 30.425026}});
        pathCoordinates.add(new double[][]{new double[]{59.896892, 30.425026}, new double[]{59.896378, 30.423535}});
        pathCoordinates.add(new double[][]{new double[]{59.896254, 30.423353}, new double[]{59.895657, 30.424581}});
        pathCoordinates.add(new double[][]{new double[]{59.895657, 30.424581}, new double[]{59.895124, 30.425804}});
        pathCoordinates.add(new double[][]{new double[]{59.895124, 30.425804}, new double[]{59.894664, 30.426684}});
        pathCoordinates.add(new double[][]{new double[]{59.894726, 30.426931}, new double[]{59.895178, 30.426115}});
        pathCoordinates.add(new double[][]{new double[]{59.895178, 30.426115}, new double[]{59.895810, 30.424742}});
        pathCoordinates.add(new double[][]{new double[]{59.895810, 30.424742}, new double[]{59.896359, 30.423578}});
        pathCoordinates.add(new double[][]{new double[]{59.893376, 30.414571}, new double[]{59.892659, 30.415547}});
        pathCoordinates.add(new double[][]{new double[]{59.892659, 30.415547}, new double[]{59.891943, 30.416486}});
        pathCoordinates.add(new double[][]{new double[]{59.891943, 30.416486}, new double[]{59.891195, 30.417479}});
        pathCoordinates.add(new double[][]{new double[]{59.891195, 30.417479}, new double[]{59.890652, 30.418198}});
        pathCoordinates.add(new double[][]{new double[]{59.890679, 30.418294}, new double[]{59.891973, 30.416556}});
        pathCoordinates.add(new double[][]{new double[]{59.891973, 30.416556}, new double[]{59.892686, 30.415639}});
        pathCoordinates.add(new double[][]{new double[]{59.892686, 30.415639}, new double[]{59.893410, 30.414646}});

        pathCoordinates.add(new double[][]{new double[]{59.899162, 30.418857}, new double[]{59.899813, 30.419721}});
        pathCoordinates.add(new double[][]{new double[]{59.899813, 30.419721}, new double[]{59.900384, 30.420451}});
        pathCoordinates.add(new double[][]{new double[]{59.900384, 30.420451}, new double[]{59.900931, 30.421151}});
        pathCoordinates.add(new double[][]{new double[]{59.900931, 30.421151}, new double[]{59.901423, 30.421770}});
        pathCoordinates.add(new double[][]{new double[]{59.901423, 30.421770}, new double[]{59.901974, 30.422451}});
        pathCoordinates.add(new double[][]{new double[]{59.901974, 30.422451}, new double[]{59.902871, 30.423629}});

        pathCoordinates.add(new double[][]{new double[]{59.902730, 30.423841}, new double[]{59.902057, 30.422940}});
        pathCoordinates.add(new double[][]{new double[]{59.902057, 30.422940}, new double[]{59.901557, 30.422312}});
        pathCoordinates.add(new double[][]{new double[]{59.901557, 30.422312}, new double[]{59.901191, 30.421824}});
        pathCoordinates.add(new double[][]{new double[]{59.901191, 30.421824}, new double[]{59.900782, 30.421304}});
        pathCoordinates.add(new double[][]{new double[]{59.900782, 30.421304}, new double[]{59.900292, 30.420692}});
        pathCoordinates.add(new double[][]{new double[]{59.900292, 30.420692}, new double[]{59.899770, 30.420016}});
        pathCoordinates.add(new double[][]{new double[]{59.899770, 30.420016}, new double[]{59.899031, 30.419072}});*/


        //replacing lat and lon
        for(double[] [] coords : pathCoordinates) {
            double[] start = coords[0];
            double[] end = coords[1];

            double tmp_start = start[0];
            double tmp_end = end[0];

            start[0] = start[1];
            start[1] = tmp_start;

            end[0] = end[1];
            end[1] = tmp_end;
        }
    }
}
