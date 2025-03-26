package fr.inrae.urgi.faidare.dao.v2;
//https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
//https://www.baeldung.com/spring-data-criteria-queries
//https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repositories.single-repository-behavior
//https://www.baeldung.com/spring-data-elasticsearch-queries

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.SynonymsVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GermplasmV2DaoCustomImpl implements GermplasmV2DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public BrapiListResponse<GermplasmV2VO> findGermplasmsByCriteria(GermplasmV2Criteria germplasmV2Criteria) {

        Criteria esCrit = new Criteria();

        Map<String, List<String>> fieldMappings = new HashMap<>();
        fieldMappings.put("accessionNumber", germplasmV2Criteria.getAccessionNumber());
        fieldMappings.put("genusSpecies", germplasmV2Criteria.getBinomialNames());
        fieldMappings.put("commonCropName", germplasmV2Criteria.getCommonCropNames());
        fieldMappings.put("externalReferenceIDs", germplasmV2Criteria.getExternalReferenceIDs());
        fieldMappings.put("externalReferenceIds", germplasmV2Criteria.getExternalReferenceIds());
        fieldMappings.put("externalReferenceSources", germplasmV2Criteria.getExternalReferenceSources());
        fieldMappings.put("familyCodes", germplasmV2Criteria.getFamilyCodes());
        fieldMappings.put("genus", germplasmV2Criteria.getGenus());
        fieldMappings.put("germplasmDbId", germplasmV2Criteria.getGermplasmDbId());
        fieldMappings.put("germplasmName", germplasmV2Criteria.getGermplasmName());
        fieldMappings.put("germplasmPUI", germplasmV2Criteria.getGermplasmPUIs());
        fieldMappings.put("instituteCode", germplasmV2Criteria.getInstituteCodes());
        fieldMappings.put("parentDbIds", germplasmV2Criteria.getParentDbIds());
        fieldMappings.put("progenyDbIds", germplasmV2Criteria.getProgenyDbIds());
        fieldMappings.put("programDbIds", germplasmV2Criteria.getProgramDbIds());
        fieldMappings.put("programNames", germplasmV2Criteria.getProgramNames());
        fieldMappings.put("species", germplasmV2Criteria.getSpecies());
        fieldMappings.put("studyDbIds", germplasmV2Criteria.getStudyDbIds());
        fieldMappings.put("studyNames", germplasmV2Criteria.getStudyNames());
        fieldMappings.put("trialDbIds", germplasmV2Criteria.getTrialDbIds());
        fieldMappings.put("trialNames", germplasmV2Criteria.getTrialNames());

        fieldMappings.forEach((key, value) ->
            Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .ifPresent(v -> esCrit.and(new Criteria(key).in(v)))
        );

        Optional.ofNullable(germplasmV2Criteria.getCollections())
            .filter(c -> !c.isEmpty())
            .ifPresent(c -> {
                Criteria panelCrit = new Criteria("panel.name").in(c);
                Criteria popCrit = new Criteria("population.name").in(c);
                Criteria collCrit = new Criteria("collection.name").in(c);
                esCrit.subCriteria(popCrit.or(panelCrit).or(collCrit));
            });

        Optional.ofNullable(germplasmV2Criteria.getSynonyms())
            .filter(s -> !s.isEmpty())
            .map(s -> s.stream().map(SynonymsVO::getSynonym).toList())
            .ifPresent(synonyms -> {
                Criteria nestedCriteria = new Criteria("synonymsV2.synonym").in(synonyms);
                esCrit.and(Criteria.where("synonymsV2").subCriteria(nestedCriteria));

            });

        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        criteriaQuery.setPageable(PageRequest.of(
            Optional.ofNullable(germplasmV2Criteria.getPage()).orElse(0),
            Optional.ofNullable(germplasmV2Criteria.getPageSize()).orElse(10)
        ));

        SearchHits<GermplasmV2VO> searchHits = esTemplate.search(criteriaQuery, GermplasmV2VO.class);
        return BrapiListResponse.brapiResponseForPageOf(searchHits, criteriaQuery.getPageable());

    }


}
