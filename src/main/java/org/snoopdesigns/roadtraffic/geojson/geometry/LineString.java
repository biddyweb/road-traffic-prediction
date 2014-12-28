package org.snoopdesigns.roadtraffic.geojson.geometry;

import java.util.ArrayList;
import java.util.List;

public class LineString extends Geometry {
	
	private List<double[]> coordinates;
	
	public LineString(List<Point> coordinates) {
		super(LineString.class.getSimpleName());
		if(coordinates.size() > 0) {
			this.coordinates = new ArrayList<double[]>();
		}
		for(Point p: coordinates) {
			this.coordinates.add(p.getCoordinates());
		}
	}
	
	public List<double[]> getCoordinates() {
		return coordinates;
	}
}

