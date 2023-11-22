package fr.inrae.urgi.faidare.web.study;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.config.FaidareProperties;
import fr.inrae.urgi.faidare.dao.XRefDocumentDao;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Dao;
import fr.inrae.urgi.faidare.dao.v1.LocationV1Dao;
import fr.inrae.urgi.faidare.dao.v1.StudyV1Dao;
import fr.inrae.urgi.faidare.dao.v1.TrialV1Dao;
import fr.inrae.urgi.faidare.domain.LocationVO;
import fr.inrae.urgi.faidare.domain.XRefDocumentVO;
import fr.inrae.urgi.faidare.domain.brapi.StudySitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.StudyV1VO;
import fr.inrae.urgi.faidare.domain.brapi.v1.TrialV1VO;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableVO;
import fr.inrae.urgi.faidare.web.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Stream;

import static fr.inrae.urgi.faidare.web.Fixtures.htmlContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MVC tests for {@link StudyController}
 * @author JB Nizet
 */
@WebMvcTest(StudyController.class)
public class StudyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyV1Dao mockStudyRepository;

    @MockBean
    private FaidareProperties mockFaidareProperties;

    @MockBean
    private XRefDocumentDao mockXRefDocumentRepository;

    @MockBean
    private GermplasmV1Dao mockGermplasmRepository;

    @MockBean
    private CropOntologyRepository mockCropOntologyRepository;

    @MockBean
    private TrialV1Dao mockTrialRepository;

    @MockBean
    private LocationV1Dao mockLocationRepository;

    @Autowired
    private StudyController studyController;

    private StudyV1VO study;
    private GermplasmV1VO germplasm;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;
    private LocationVO location;
    private TrialV1VO trial;

    @BeforeEach
    void prepare() {
        study = Fixtures.createStudy();
        when(mockStudyRepository.getByStudyDbId(study.getStudyDbId())).thenReturn(study);

        germplasm = Fixtures.createGermplasm();
        when(mockGermplasmRepository.findByGermplasmDbIdIn(any())).thenAnswer(
            invocation -> Stream.of(germplasm)
        );

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.findByLinkedResourcesID(any()))
            .thenReturn(crossReferences);

        dataSource = Fixtures.createDataSource();

        // FIXME JBN uncomment this line when study has sourceUri
        // when(mockFaidareProperties.getByUri(study.getSourceUri())).thenReturn(dataSource);
        when(mockFaidareProperties.getByUri(any())).thenReturn(dataSource);

        location = Fixtures.createSite();
        when(mockLocationRepository.getByLocationDbId(study.getLocationDbId())).thenReturn(location);

        trial = Fixtures.createTrial();
        when(mockTrialRepository.getByTrialDbId(study.getTrialsDbIds().iterator().next())).thenReturn(trial);

        Set<String> variableDbIds = Collections.singleton("variable1");
        // FIXME JBN uncomment this line once StudyV1Dao has a getVariableIds() method
        // when(mockStudyRepository.getVariableIds(study.getStudyDbId())).thenReturn(variableDbIds);

        when(mockCropOntologyRepository.getVariableByIds(variableDbIds)).thenReturn(
            Arrays.asList(Fixtures.createVariable())
        );
    }

    @Test
    void shouldDisplayStudy() throws Exception {
        mockMvc.perform(get("/studies/{id}", study.getStudyDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Study Doability: Study 1"))
               .andExpect(htmlContent().containsH2s("Identification", "Genotype", "Variables", "Data Set", "Contact", "Additional information", "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<StudySitemapVO> studies = Arrays.asList(
            new StudySitemapVO("study1"),
            new StudySitemapVO("study4"),
            new StudySitemapVO("study51"),
            new StudySitemapVO("study72")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // studies.forEach(study -> System.out.println(study.getStudyDbId() + " = " + Math.floorMod(study.getStudyDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockStudyRepository.findAllForSitemap()).thenAnswer(invocation -> studies.stream());
        testSitemap(6, "http://localhost/faidare/studies/study1\nhttp://localhost/faidare/studies/study72\n");
        testSitemap(9, "http://localhost/faidare/studies/study4\nhttp://localhost/faidare/studies/study51\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/studies/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    @Nested
    class Variables {
        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsFound() throws Exception {
            ObservationVariableVO variableWithEnglishLanguage = Fixtures.createVariable();
            variableWithEnglishLanguage.setLanguage("EN");

            ObservationVariableVO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");

            ObservationVariableVO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setLanguage(null);

            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithEnglishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.FRENCH))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).containsOnly(variableWithFrenchLanguage, variableWithNoLanguage);
        }

        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsNotFound() throws Exception {
            ObservationVariableVO variableWithEnglishLanguage = Fixtures.createVariable();
            variableWithEnglishLanguage.setLanguage("EN");

            ObservationVariableVO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");

            ObservationVariableVO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setLanguage(null);

            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithEnglishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.CHINA))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).containsOnly(variableWithEnglishLanguage, variableWithNoLanguage);
        }

        @Test
        void shouldFilterVariablesByLanguageWhenRequestedLanguageIsNotFoundAndEnglishAbsent() throws Exception {
            ObservationVariableVO variableWithSpanishLanguage = Fixtures.createVariable();
            variableWithSpanishLanguage.setLanguage("es");

            ObservationVariableVO variableWithFrenchLanguage = Fixtures.createVariable();
            variableWithFrenchLanguage.setLanguage("FRA");

            ObservationVariableVO variableWithNoLanguage = Fixtures.createVariable();
            variableWithNoLanguage.setLanguage(null);

            when(mockCropOntologyRepository.getVariableByIds(any())).thenReturn(
                Arrays.asList(variableWithSpanishLanguage, variableWithFrenchLanguage, variableWithNoLanguage)
            );

            ModelAndView modelAndView = mockMvc.perform(get("/studies/{id}", study.getStudyDbId())
                                                            .locale(Locale.CHINA))
                                               .andReturn()
                                               .getModelAndView();
            StudyModel model = (StudyModel) modelAndView.getModel().get("model");
            assertThat(model.getVariables()).hasSize(2).contains(variableWithNoLanguage);
        }
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/studies/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));

    }
}
