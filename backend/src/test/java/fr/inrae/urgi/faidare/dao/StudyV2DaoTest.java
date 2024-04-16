package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.dao.v2.StudyCriteria;
import fr.inrae.urgi.faidare.dao.v2.StudyV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
                studyV2Dao.getByStudyDbId("toto");
        assertThat(studyVO).isNull();
    }

    @Test
    void getByStudyDbID_studyDbId(){
        StudyV2VO studyVO = studyV2Dao.getByStudyDbId("dXJuOklCRVQvc3R1ZHkvMQ==");
        assertThat(studyVO).isNotNull();
        assertThat(studyVO.getStudyDbId()).isEqualTo("dXJuOklCRVQvc3R1ZHkvMQ==");
    }

    @Test
    void custom_should_search_by_commonCropNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setCommonCropNames(List.of("Rice"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getCommonCropName()).isEqualTo("Rice");
    }


        //TODO : check if this is in the spec
    void custom_should_search_by_externalReferenceIDs(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setExternalReferenceIDs(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
//        assertThat(studyVOs.getSearchHit(0).getContent().getExternalReferences()).contains("");
    }


        //TODO : check if this is in the spec
    void custom_should_search_by_externalReferenceSources(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setExternalReferenceSources(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
    //    assertThat(studyVOs.getSearchHit(0).getContent().get()).isEqualTo("");
    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setGermplasmDbIds((List.of("dXJuOklCRVQvYTY3OTk1MDgtMmFhOS00NmVjLThjM2MtMjcyZmViODg1MDVi")));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getGermplasmDbIds()).contains("dXJuOklCRVQvYTY3OTk1MDgtMmFhOS00NmVjLThjM2MtMjcyZmViODg1MDVi");
    }



    //TODO : check if this is in the spec
    void custom_should_search_by_germplasmNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setGermplasmNames(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
 //       assertThat(studyVOs.getSearchHit(0).getContent().get()).isEqualTo("");
    }

    @Test
    void custom_should_search_by_locationDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationDbIds(List.of("dXJuOklCRVQvbG9jYXRpb24vdW5kZWZpbmVk"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getLocationDbId()).isEqualTo("dXJuOklCRVQvbG9jYXRpb24vdW5kZWZpbmVk");
    }

    @Test
    void custom_should_search_by_locationNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setLocationNames(List.of("Grandola (Barradas da Serra)"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getLocationName()).isEqualTo("Grandola (Barradas da Serra)");
    }

    @Test
    void custom_should_search_by_observationVariableDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setObservationVariableDbIds(List.of("17"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getObservationVariableDbIds()).contains("17");
    }


        //TODO : check if this is in the spec
    void custom_should_search_by_observationVariableNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setObservationVariableNames(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
//        assertThat(studyVOs.getSearchHit(0).getContent().get()).isEqualTo("");
    }


        //TODO : check if this is in the spec
    void custom_should_search_by_observationVariablePUIs(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setObservationVariablePUIs(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
//        assertThat(studyVOs.getSearchHit(0).getContent().get()).isEqualTo("");
    }

    @Test
    void custom_should_search_by_programDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramDbIds(List.of("dXJuOklCRVQvcHJvZ3JhbS8x"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getProgramDbId()).isEqualTo("dXJuOklCRVQvcHJvZ3JhbS8x");
    }

    @Test
    void custom_should_search_by_programNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setProgramNames(List.of("INRA Wheat Breeding Network"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getProgramName()).isEqualTo("INRA Wheat Breeding Network");
    }

    //@Test
    void custom_should_search_by_seasonDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setSeasonDbIds(List.of("2000"));
        //TODO : test data is still V1 compliant
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getTotalHits()).isEqualTo(10);
    }

    //@Test
    void custom_should_search_by_studyCodes(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyCodes(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyCode()).isEqualTo("");
    }

    @Test
    void custom_should_search_by_studyDbIds(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyDbIds(List.of("dXJuOklCRVQvc3R1ZHkvMQ=="));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyDbId()).isEqualTo("dXJuOklCRVQvc3R1ZHkvMQ==");
    }

    @Test
    void custom_should_search_by_studyName(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyNames(List.of("D4"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyName()).isEqualTo("D4");
    }

    void custom_should_search_by_studyPUIs(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setStudyPUIs(List.of(""));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getStudyPUI()).isEqualTo("");
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
        sCrit.setTrialDbIds(List.of("dXJuOklCRVQvdHJpYWwvMQ=="));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getTrialDbId()).contains("dXJuOklCRVQvdHJpYWwvMQ==");
    }

    @Test
    void custom_should_search_by_trialNames(){
        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialNames(List.of("Cork quality traits in three populations of Quercus suber"));
        SearchHits<StudyV2VO> studyVOs = studyV2Dao.findStudiesByCriteria(sCrit);
        assertThat(studyVOs).isNotNull().isNotEmpty();
        assertThat(studyVOs.getSearchHit(0).getContent().getTrialName()).isEqualTo("Cork quality traits in three populations of Quercus suber");
    }

}
