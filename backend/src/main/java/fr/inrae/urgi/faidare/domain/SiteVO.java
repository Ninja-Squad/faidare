package fr.inrae.urgi.faidare.domain;

import java.util.Objects;

public class SiteVO {

    private Double latitude;

    private Double longitude;

    private String siteId;

    private String siteName;

    private String siteType;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteVO siteVO = (SiteVO) o;
        return Objects.equals(latitude, siteVO.latitude) && Objects.equals(longitude, siteVO.longitude) && Objects.equals(siteId, siteVO.siteId) && Objects.equals(siteName, siteVO.siteName) && Objects.equals(siteType, siteVO.siteType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, siteId, siteName, siteType);
    }
}
