package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.dao.v1.StudyV1Dao;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
class StudyV1DaoTest {

    @Autowired
    protected StudyV1Dao studyV1Dao;

    /**
     *     Code tested For futur implementation in Faidare cards:
     *         public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
     *         GermplasmVO germplasm = germplasmDao.getByGermplasmDbId(germplasmId);
     *
     *         @GetMapping(params = "id")
     *         public ModelAndView getById(@RequestParam("id") String germplasmId) {
     *         GermplasmVO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);
     */
    @Test
    void getByStudyDbId_should_return_one_result() {
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");
    }

    @Test
    void getByStudyDbId_should_return_lastUpdate() {
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");
        assertThat(sVo.getLastUpdate().getTimestamp()).isNotNull().containsSubsequence("2017-02-21");
    }

    /**
     * Present for historic reasons.
     * replacement code for
     *     public Set<String> getVariableIds(String studyDbId)
     */
    @Test
    void should_get_variables_by_study_id(){
        StudyV1VO sVo =
                studyV1Dao.getByStudyDbId("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");

        assertThat(sVo).isNotNull();
        assertThat(sVo.getStudyDbId())
                .isEqualTo("dXJuOlVSR0kvc3R1ZHkvQlRIX0NoYXV4X2Rlc19QciVDMyVBOXNfMjAwMF9TZXRCMQ==");
        Set<String> obsVarIds = Set.copyOf(sVo.getObservationVariableDbIds());
        assertThat(obsVarIds).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    void findAllForSitemap() {
        List<StudySitemapVO> list = studyV1Dao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(StudySitemapVO.class);
        assertThat(list.get(0).getStudyDbId()).isNotNull();
    }
}
