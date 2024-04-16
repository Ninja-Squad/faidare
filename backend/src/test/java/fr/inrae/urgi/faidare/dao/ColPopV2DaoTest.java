package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.dao.v2.CollectionV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
public class ColPopV2DaoTest {

    @Autowired
    protected CollectionV2Dao dao;

    @Test
    public void should_get_all_CollPops(){
        BrapiListResponse<CollPopVO> response = dao.getAllCollections();
        assertThat(response).isNotNull();
        assertThat(response.getMetadata().getPagination().getTotalCount()).isGreaterThan(11);
        assertThat(response.getResult().getData()).anyMatch(collPop -> collPop.getName().equals("SMALL_GRAIN_CEREALS_NETWORK_COL"));

    }
}
