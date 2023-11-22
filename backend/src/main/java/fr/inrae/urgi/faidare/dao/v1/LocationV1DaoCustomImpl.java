package fr.inrae.urgi.faidare.dao.v1;

import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.brapi.LocationSitemapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.Queries;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;

import java.util.stream.Stream;

public class LocationV1DaoCustomImpl implements LocationV1DaoCustom {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Stream<LocationSitemapVO> findAllForSitemap() {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();
        NativeQuery query = nativeQueryBuilder
            .withQuery(builder -> builder.matchAll(Queries.matchAllQuery()))
            .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("locationDbId").build())
            .build();
        return esTemplate.searchForStream(query, LocationSitemapVO.class, IndexCoordinates.of(LocationVO.INDEX_NAME))
                         .stream()
                         .map(SearchHit::getContent);
    }
}
