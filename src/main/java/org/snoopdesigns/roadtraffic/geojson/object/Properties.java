package org.snoopdesigns.roadtraffic.geojson.object;

public class Properties {

    private String popupContent;
    private Style style;

    public Properties(String popupContent, Style style) {

        this.popupContent = popupContent;
        this.style = style;
    }

    public String getPopupContent() {
        return popupContent;
    }

    public void setPopupContent(String popupContent) {
        this.popupContent = popupContent;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
