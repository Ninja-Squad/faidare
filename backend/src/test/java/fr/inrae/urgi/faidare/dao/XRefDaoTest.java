package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
public class XRefDaoTest {

    @Autowired
    protected XRefDocumentDao dao;

    /**
     * To be used in thymeleaf controlers for the following code :
     * List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
     *             XRefDocumentSearchCriteria.forXRefId(study.getStudyDbId())
     */
    @Test
    public void should_get_perDbId(){
        List<XRefDocumentVO> lVos = dao.findByLinkedResourcesID("aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L002QVBUUA==");
        assertThat(lVos).hasSize(34);
        assertThat(lVos).allMatch(vo -> !vo.getUrl().isBlank());
        assertThat(lVos).anyMatch(vo -> vo.getDatabaseName().equals("GnpIS"));
        assertThat(lVos).anyMatch(vo -> vo.getEntryType().equals("GWAS analysis"));
        assertThat(lVos).anyMatch(vo -> vo.getSpecies().get(0).equals("Triticum aestivum aestivum"));

    }
}
