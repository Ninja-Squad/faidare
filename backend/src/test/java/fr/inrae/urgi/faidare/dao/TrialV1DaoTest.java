package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.dao.v1.TrialV1Dao;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
public class TrialV1DaoTest {

    @Autowired
    protected TrialV1Dao dao;

    @Test
    public void should_get_one_location_perDbId(){
        TrialV1VO vo = dao.getByTrialDbId("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
        assertThat(vo).isNotNull();
         assertThat(vo.getTrialName()).isEqualTo("INRA Wheat Network technological variables");

    }
}
