package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.domain.data.phenotype.ObservationUnitVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.domain.response.PaginationImpl;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.ObservationUnitRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudyController.class)
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObservationUnitRepository observationUnitRepository;

    @MockBean
    private GermplasmRepository germplasmRepository;

    @MockBean
    private CropOntologyRepository cropOntologyRepository;

    @MockBean
    private StudyRepository repository;

    private static StudyDetailVO STUDY;
    static {
        String id = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjM4MTc4MzY5NkUxMg==";
        String uri = "http://doi.org/foo/bar";
        STUDY = new StudyDetailVO();
        STUDY.setUri(uri);
        STUDY.setStudyDbId(id);
    }

    @Test
    void should_Not_Show_JSON_LD_Fields_By_Default() throws Exception {
        when(repository.getById(STUDY.getStudyDbId())).thenReturn(STUDY);
        mockMvc.perform(get("/brapi/v1/studies/" + STUDY.getStudyDbId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id").doesNotExist());
    }

    @Test
    void should_Show_JSON_LD_Fields_When_Asked() throws Exception {
        when(repository.getById(STUDY.getStudyDbId())).thenReturn(STUDY);

        mockMvc.perform(get("/brapi/v1/studies/"+ STUDY.getStudyDbId())
                            .accept(BrapiJSONViewHandler.APPLICATION_LD_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id", is(STUDY.getUri())));
    }

    @Test
    void should_Get_By_Id() throws Exception {
        String identifier = "identifier";

        StudyDetailVO study = new StudyDetailVO();
        when(repository.getById(identifier)).thenReturn(study);

        mockMvc.perform(get("/brapi/v1/studies/" + identifier))
            .andExpect(status().isOk());
    }

    @Test
    void should_Return_Not_Found() throws Exception {
        when(repository.getById("foo")).thenReturn(null);

        mockMvc.perform(get("/brapi/v1/studies/foo"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.metadata.status", hasSize(1)))
            .andExpect(jsonPath("$.metadata.status[0].code", is("404")));
    }

    @Test
    void should_Paginate_ObservationUnits_By_Study() throws Exception {
        String studyDbId = "foo";
        int page = 2;
        int pageSize = 12;

        Pagination pagination = PaginationImpl.create(pageSize, page, 1000);
        PaginatedList<ObservationUnitVO> observationUnits = new PaginatedList<>(pagination, new ArrayList<>());
        when(observationUnitRepository.find(any())).thenReturn(observationUnits);

        mockMvc.perform(get("/brapi/v1/studies/{id}/observationUnits?page={page}&pageSize={pageSize}", studyDbId, page, pageSize))
            .andExpect(jsonPath("$.metadata.pagination.currentPage", is(page)))
            .andExpect(jsonPath("$.metadata.pagination.pageSize", is(pageSize)));
    }


}
