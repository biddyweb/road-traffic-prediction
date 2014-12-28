package org.snoopdesigns.roadtraffic.geojson.object;

import org.snoopdesigns.roadtraffic.geojson.geometry.Geometry;

import java.io.Serializable;
import java.util.Map;

public class Feature {
	
	private final String type = "Feature";
	private Properties properties;
	private Geometry geometry;
	private String id;

	public Feature(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public String getType() {
		return type;
	}

	public Geometry getGeometry() {
		return geometry;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getId() { return id; }

	public void setId(String id) { this.id = id; }
}
