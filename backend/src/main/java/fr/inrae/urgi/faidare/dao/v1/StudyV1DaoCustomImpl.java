package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.dao.v2.StudyCriteria;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.stream.Stream;

public class StudyV1DaoCustomImpl implements StudyV1DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHits<StudyV2VO> findStudiesByCriteria(StudyCriteria studyCriteria) {

        Criteria esCrit = new Criteria();

        if (studyCriteria.getCommonCropNames() != null
                && !studyCriteria.getCommonCropNames().isEmpty()) {
            esCrit.and(new Criteria("commonCropName").in(studyCriteria.getCommonCropNames()));
        }

        if (studyCriteria.getExternalReferenceIDs() != null
                && !studyCriteria.getExternalReferenceIDs().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceIDs").in(studyCriteria.getExternalReferenceIDs()));
        }

        if (studyCriteria.getExternalReferenceSources() != null
                && !studyCriteria.getExternalReferenceSources().isEmpty()) {
            esCrit.and(new Criteria("externalReferenceSources").in(studyCriteria.getExternalReferenceSources()));
        }

        if (studyCriteria.getGermplasmDbIds() != null
                && !studyCriteria.getGermplasmDbIds().isEmpty()) {
            esCrit.and(new Criteria("germplasmDbIds").in(studyCriteria.getGermplasmDbIds()));
        }

        if (studyCriteria.getGermplasmNames() != null
                && !studyCriteria.getGermplasmNames().isEmpty()) {
            esCrit.and(new Criteria("germplasmNames").in(studyCriteria.getGermplasmNames()));
        }

        if (studyCriteria.getLocationDbIds() != null
                && !studyCriteria.getLocationDbIds().isEmpty()) {
            esCrit.and(new Criteria("locationDbIds").in(studyCriteria.getLocationDbIds()));
        }

        if (studyCriteria.getLocationNames() != null
                && !studyCriteria.getLocationNames().isEmpty()) {
            esCrit.and(new Criteria("locationNames").in(studyCriteria.getLocationNames()));
        }

        if (studyCriteria.getObservationVariableDbIds() != null
                && !studyCriteria.getObservationVariableDbIds().isEmpty()) {
            esCrit.and(new Criteria("observationVariableDbIds").in(studyCriteria.getObservationVariableDbIds()));
        }

        if (studyCriteria.getObservationVariableNames() != null
                && !studyCriteria.getObservationVariableNames().isEmpty()) {
            esCrit.and(new Criteria("observationVariableNames").in(studyCriteria.getObservationVariableNames()));
        }

        if (studyCriteria.getObservationVariablePUIs() != null
                && !studyCriteria.getObservationVariablePUIs().isEmpty()) {
            esCrit.and(new Criteria("observationVariablePUIs").in(studyCriteria.getObservationVariablePUIs()));
        }

        if (studyCriteria.getProgramDbIds() != null
                && !studyCriteria.getProgramDbIds().isEmpty()) {
            esCrit.and(new Criteria("programDbIds").in(studyCriteria.getProgramDbIds()));
        }

        if (studyCriteria.getProgramNames() != null
                && !studyCriteria.getProgramNames().isEmpty()) {
            esCrit.and(new Criteria("programNames").in(studyCriteria.getProgramNames()));
        }

        if (studyCriteria.getSeasonDbIds() != null
                && !studyCriteria.getSeasonDbIds().isEmpty()) {
            esCrit.and(new Criteria("seasonDbIds").in(studyCriteria.getSeasonDbIds()));
        }

        if (studyCriteria.getSortBy() != null
                && !studyCriteria.getSortBy().isEmpty()) {
            esCrit.and(new Criteria("sortBy").in(studyCriteria.getSortBy()));
        }

        if (studyCriteria.getSortOrder() != null
                && !studyCriteria.getSortOrder().isEmpty()) {
            esCrit.and(new Criteria("sortOrder").in(studyCriteria.getSortOrder()));
        }

        if (studyCriteria.getStudyCodes() != null
                && !studyCriteria.getStudyCodes().isEmpty()) {
            esCrit.and(new Criteria("studyCodes").in(studyCriteria.getStudyCodes()));
        }

        if (studyCriteria.getStudyDbIds() != null
                && !studyCriteria.getStudyDbIds().isEmpty()) {
            esCrit.and(new Criteria("studyDbIds").in(studyCriteria.getStudyDbIds()));
        }

        if (studyCriteria.getStudyNames() != null
                && !studyCriteria.getStudyNames().isEmpty()) {
            esCrit.and(new Criteria("studyNames").in(studyCriteria.getStudyNames()));
        }

        if (studyCriteria.getStudyPUIs() != null
                && !studyCriteria.getStudyPUIs().isEmpty()) {
            esCrit.and(new Criteria("studyPUIs").in(studyCriteria.getStudyPUIs()));
        }

        if (studyCriteria.getStudyTypes() != null
                && !studyCriteria.getStudyTypes().isEmpty()) {
            esCrit.and(new Criteria("studyTypes").in(studyCriteria.getStudyTypes()));
        }

        if (studyCriteria.getTrialDbIds() != null
                && !studyCriteria.getTrialDbIds().isEmpty()) {
            esCrit.and(new Criteria("trialDbIds").in(studyCriteria.getTrialDbIds()));
        }

        if (studyCriteria.getTrialNames() != null
                && !studyCriteria.getTrialNames().isEmpty()) {
            esCrit.and(new Criteria("trialNames").in(studyCriteria.getTrialNames()));
        }

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        return esTemplate.search(criteriaQuery, StudyV2VO.class);
    }

    @Override
    public Stream<StudySitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
                .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("studyDbId").build())
                .build();
        return esTemplate.searchForStream(query, StudySitemapVO.class, IndexCoordinates.of(StudyV1VO.INDEX_NAME))
                .stream()
                .map(SearchHit::getContent);
    }
}
