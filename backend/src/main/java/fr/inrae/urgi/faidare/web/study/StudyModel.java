package fr.inrae.urgi.faidare.web.study;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import fr.inrae.urgi.faidare.web.site.MapLocation;

/**
 * The model used by the study page
 * @author JB Nizet
 */
public final class StudyModel {
    private final StudyV1VO study;
    private final DataSource source;
    private final List<GermplasmV1VO> germplasms;
    private final List<ObservationVariableVO> variables;
    private final List<TrialV1VO> trials;
    private final List<XRefDocumentVO> crossReferences;
    private final LocationVO location;
    private final List<Map.Entry<String, Object>> additionalInfoProperties;

    public StudyModel(StudyV1VO study,
                      DataSource source,
                      List<GermplasmV1VO> germplasms,
                      List<ObservationVariableVO> variables,
                      List<TrialV1VO> trials,
                      List<XRefDocumentVO> crossReferences,
                      LocationVO location) {
        this.study = study;
        this.source = source;
        this.germplasms = germplasms;
        this.variables = variables;
        this.trials = trials;
        this.crossReferences = crossReferences;
        this.location = location;

        // FIXME JBN uncomment this once study has additionalInfo
//        Map<String, Object> additionalInfo =
//            study.getAdditionalInfo() == null ? Collections.emptyMap() : study.getAdditionalInfo().getProperties();
        Map<String, Object> additionalInfo = Collections.emptyMap();
        this.additionalInfoProperties =
            additionalInfo.entrySet()
                          .stream()
                          .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                          .sorted(Map.Entry.comparingByKey())
                          .collect(Collectors.toList());
    }

    public StudyV1VO getStudy() {
        return study;
    }

    public DataSource getSource() {
        return source;
    }

    public List<XRefDocumentVO> getCrossReferences() {
        return crossReferences;
    }

    public List<GermplasmV1VO> getGermplasms() {
        return germplasms;
    }

    public List<ObservationVariableVO> getVariables() {
        return variables;
    }

    public List<TrialV1VO> getTrials() {
        return trials;
    }

    public List<Map.Entry<String, Object>> getAdditionalInfoProperties() {
        return additionalInfoProperties;
    }

    public List<MapLocation> getMapLocations() {
        if (this.location == null) {
            return Collections.emptyList();
        }
        return MapLocation.locationsToDisplayableMapLocations(Collections.singletonList(this.location));
    }
}
