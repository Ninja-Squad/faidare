package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.dao.v2.StudyCriteria;
import fr.inrae.urgi.faidare.dao.v2.StudyV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({ElasticSearchConfig.class})
@DataElasticsearchTest
class StudyV2DaoTest {

    /**
     * StudyV1VO and StudyV2VO are compatible enough.
     * Therefore, there is no studyV1DAO, only a StudyV2DAO that serves both
     */

    @Autowired
    protected StudyV2Dao studyV2Dao;

    @Test
    void getByStudyDbId_should_return_empty_result() {
        StudyV2VO studyVO =
                studyV2Dao.getByStudyDbId("foo");
        assertThat(studyVO).isNull();
    }

    @Test
    void getByStudyDbID_studyDbId(){
        StudyV2VO studyVO = studyV2Dao.getByStudyDbId("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMDRfVEVDSA==");
        assertThat(studyVO).isNotNull();
        assertThat(studyVO.getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMDRfVEVDSA==");
    }
//    It is commented because in the actual data test we have just null commonCropNames
//    @Test
//    void custom_should_search_by_commonCropNames(){
//        StudyCriteria sCrit = new StudyCriteria();
//        sCrit.setCommonCropNames(List.of("Wheat"));
//        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
//        assertThat(studyVOs).isNotNull().isNotEmpty();
//        assertThat(studyVOs.getSearchHit(0).getContent().getCommonCropName()).isEqualTo("Wheat");
//    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setGermplasmDbIds((List.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4")));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getGermplasmDbIds()).contains("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4");
    }

    @Test
    void custom_should_search_by_locationDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationDbIds(List.of("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ="));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getLocationDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvbG9jYXRpb24vMzQwNjQ=");
    }

    @Test
    void custom_should_search_by_locationNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationNames(List.of("Le Moulon"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getLocationName()).isEqualTo("Le Moulon");
    }

    @Test
    void custom_should_search_by_observationVariableDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setObservationVariableDbIds(List.of("CO_321:1000070"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getObservationVariableDbIds()).contains("CO_321:1000070");
    }

    @Test
    void custom_should_search_by_programDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramDbIds(List.of("dXJuOklOUkFFLVVSR0kvcHJvZ3JhbS9JTlJBX1doZWF0X0JyZWVkaW5nX05ldHdvcms="));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getProgramDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvcHJvZ3JhbS9JTlJBX1doZWF0X0JyZWVkaW5nX05ldHdvcms=");
    }

    @Test
    void custom_should_search_by_programNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramNames(List.of("INRA Wheat Breeding Network"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getProgramName()).isEqualTo("INRA Wheat Breeding Network");
    }

    @Test
    void custom_should_search_by_studyDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyDbIds(List.of("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAwNV9URUNI"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAwNV9URUNI");
    }

    @Test
    void custom_should_search_by_studyName(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyNames(List.of("BTH_Estrées-Mons_2005_TECH"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyName()).isEqualTo("BTH_Estrées-Mons_2005_TECH");
    }

    @Test
    void custom_should_search_by_studyTypes(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyTypes(List.of("Phenotyping Study"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyType()).isEqualTo("Phenotyping Study");
    }

    @Test
    void custom_should_search_by_trialDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialDbIds(List.of("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw=="));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getTrialDbId()).contains("dXJuOklOUkFFLVVSR0kvdHJpYWwvNw==");
    }

    @Test
    void custom_should_search_by_trialNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialNames(List.of("INRA Wheat Network technological variables"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getTrialName()).isEqualTo("INRA Wheat Network technological variables");
    }

}
