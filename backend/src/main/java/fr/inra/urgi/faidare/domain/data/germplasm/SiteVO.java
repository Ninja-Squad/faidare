package fr.inra.urgi.faidare.domain.data.germplasm;

import java.io.Serializable;

public class SiteVO implements Serializable, Site {

    private static final long serialVersionUID = 7058708694739141664L;

    private String locationDbId;
    private String siteName;
    private Float latitude;
    private Float longitude;
    private String siteType;

    @Override
    public String getLocationDbId() {
        return locationDbId;
    }


    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    @Override
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

}
