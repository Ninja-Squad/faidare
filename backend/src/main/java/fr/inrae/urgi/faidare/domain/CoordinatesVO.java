package fr.inrae.urgi.faidare.domain;

public class CoordinatesVO {

    private String geometry;  // A geometry as defined by GeoJSON

    private String type;


    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
