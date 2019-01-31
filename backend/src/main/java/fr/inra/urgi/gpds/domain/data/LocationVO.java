package fr.inra.urgi.gpds.domain.data;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiLocation;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURI;
import fr.inra.urgi.gpds.domain.jsonld.data.HasURL;
import fr.inra.urgi.gpds.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.util.List;

/**
 * LocationVO extending the official BreedingAPI specs with specific fields
 *
 * @author gcornut
 */
@Document(type = "location")
public class LocationVO implements GnpISInternal, BrapiLocation, HasURI, HasURL, IncludedInDataCatalog {

    @Id
    private String locationDbId;

    private String locationName;

    private String locationType;
    private String abbreviation;

    private String countryCode;
    private String countryName;

    private String institutionAddress;
    private String institutionName;

    private Double altitude;
    private Double latitude;
    private Double longitude;

    private BrapiAdditionalInfo additionalInfo;

    // GnpIS specific fields
    private List<Long> speciesGroup;
    private Long groupId;

    // JSON-LD fields
    private String url;
    private String uri;
    private String sourceUri;

    @Override
    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getSourceUri() {
        return this.sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }


    @Override
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    @Deprecated
    @Override
    public String getAbreviation() {
        return abbreviation;
    }

    @Override
    public Double getAltitude() {
        return altitude;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getCountryName() {
        return countryName;
    }

    @Override
    public String getInstitutionAddress() {
        return institutionAddress;
    }

    @Deprecated
    @Override
    public String getInstitutionAdress() {
        return institutionAddress;
    }

    @Override
    public String getInstitutionName() {
        return institutionName;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public String getLocationDbId() {
        return locationDbId;
    }

    @Override
    public String getLocationType() {
        return locationType;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String getName() {
        return locationName;
    }

    @Override
    public BrapiAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setName(String name) {
        this.locationName = name;
    }

    public void setLocationDbId(String locationDbId) {
        this.locationDbId = locationDbId;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abbreviation = abreviation;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setInstitutionAddress(String institutionAddress) {
        this.institutionAddress = institutionAddress;
    }

    public void setInstitutionAdress(String institutionAdress) {
        this.institutionAddress = institutionAdress;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAdditionalInfo(BrapiAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getDocumentationURL() {
        return url;
    }

    public void setDocumentationURL(String documentationURL) {
        this.url = documentationURL;
    }
}
