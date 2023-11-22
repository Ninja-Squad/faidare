package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.Set;
import java.util.stream.Stream;

public class GermplasmV1DaoCustomImpl implements GermplasmV1DaoCustom{

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHitsIterator<GermplasmV1VO> scrollGermplasmsByGermplasmDbIds(Set<String> germplasmDbIds, int fetchSize) {
        Criteria esCrit = new Criteria("germplasmDbId").in(germplasmDbIds);
        CriteriaQuery criteriaQuery = new CriteriaQueryBuilder(esCrit).build();
        SearchHitsIterator<GermplasmV1VO> stream = elasticsearchOperations.searchForStream(criteriaQuery, GermplasmV1VO.class);
        return stream;
    }

    @Override
    public Stream<GermplasmSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
                .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("germplasmDbId").build())
                .build();
        return elasticsearchOperations.searchForStream(query, GermplasmSitemapVO.class, IndexCoordinates.of(GermplasmV1VO.INDEX_NAME))
                .stream()
                .map(SearchHit::getContent);
    }
}
