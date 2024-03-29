package fr.inra.urgi.faidare.web.germplasm;

import static fr.inra.urgi.faidare.web.Fixtures.htmlContent;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import fr.inra.urgi.faidare.web.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * MVC tests for {@link GermplasmController}
 * @author JB Nizet
 */
@WebMvcTest(GermplasmController.class)
@Import({GermplasmMcpdExportService.class, GermplasmExportService.class})
public class GermplasmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GermplasmRepository mockGermplasmRepository;

    @MockBean
    private FaidareProperties mockFaidareProperties;

    @MockBean
    private XRefDocumentRepository mockXRefDocumentRepository;

    @MockBean
    private GermplasmAttributeRepository mockGermplasmAttributeRepository;

    private GermplasmVO germplasm;
    private List<XRefDocumentVO> crossReferences;
    private DataSource dataSource;

    @BeforeEach
    void prepare() {
        germplasm = Fixtures.createGermplasm();
        when(mockGermplasmRepository.getById(germplasm.getGermplasmDbId())).thenReturn(germplasm);

        crossReferences = Arrays.asList(
            Fixtures.createXref("foobar"),
            Fixtures.createXref("bazbing")
        );
        when(mockXRefDocumentRepository.find(any()))
            .thenReturn(new PaginatedList<>(null, crossReferences));

        dataSource = Fixtures.createDataSource();
        when(mockFaidareProperties.getByUri(germplasm.getSourceUri())).thenReturn(dataSource);

        List<GermplasmAttributeValueListVO> attributes = Arrays.asList(Fixtures.createGermplasmAttributeValueList());
        when(mockGermplasmAttributeRepository.find(any())).thenReturn(new PaginatedList<>(null, attributes));
    }

    @Test
    void shouldDisplayGermplasm() throws Exception {
        mockMvc.perform(get("/germplasms/{id}", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithNullCollector() throws Exception {
        germplasm.setCollector(null);

        mockMvc.perform(get("/germplasms/{id}", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithIdAsParameter() throws Exception {
        mockMvc.perform(get("/germplasms").param("id", germplasm.getGermplasmDbId()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldDisplayGermplasmWithPuiAsParameter() throws Exception {
        PaginatedList<GermplasmVO> puiList = new PaginatedList<>(null, Collections.singletonList(germplasm));
        when(mockGermplasmRepository.find(any())).thenReturn(puiList);

        mockMvc.perform(get("/germplasms").param("pui", germplasm.getGermplasmPUI()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().containsH2s("Identification",
                                                    "Depositary",
                                                    "Collector",
                                                    "Breeder",
                                                    "Donors",
                                                    "Distributors",
                                                    "Evaluation Data",
                                                    "Genealogy",
                                                    "Population",
                                                    "Collection",
                                                    "Panel",
                                                    "Cross references"))
               .andExpect(htmlContent().endsCorrectly());

        ArgumentMatcher<GermplasmSearchCriteria> criteriaMatcher = criteria ->
            criteria instanceof GermplasmGETSearchCriteria
                && ((GermplasmGETSearchCriteria) criteria).getGermplasmPUI()
                                                          .equals(Collections.singletonList(germplasm.getGermplasmPUI()));
        verify(mockGermplasmRepository).find(argThat(criteriaMatcher));
    }

    @Test
    void shouldSupportLegacyPath() throws Exception {
        PaginatedList<GermplasmVO> puiList = new PaginatedList<>(null, Collections.singletonList(germplasm));
        when(mockGermplasmRepository.find(any())).thenReturn(puiList);

        mockMvc.perform(get("/germplasm").param("pui", germplasm.getGermplasmPUI()))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
               .andExpect(htmlContent().hasTitle("Germplasm: BLE BARBU DU ROUSSILLON"))
               .andExpect(htmlContent().endsCorrectly());
    }

    @Test
    void shouldGenerateSitemap() throws Exception {
        List<GermplasmSitemapVO> germplasms = Arrays.asList(
            new GermplasmSitemapVO("germplasm1"),
            new GermplasmSitemapVO("germplasm4"),
            new GermplasmSitemapVO("germplasm45"),
            new GermplasmSitemapVO("germplasm73")
        );

        // the hashCode algorithm is specified in the javadoc, so it's guaranteed to be
        // the same everywhere
        // uncomment the following line to see which sitemap index each study has
        // germplasms.forEach(germplasm -> System.out.println(germplasm.getGermplasmDbId() + " = " + Math.floorMod(germplasm.getGermplasmDbId().hashCode(), Sitemaps.BUCKET_COUNT)));

        when(mockGermplasmRepository.scrollAllForSitemap(anyInt())).thenAnswer(invocation -> germplasms.iterator());

        testSitemap(6, "http://localhost/faidare/germplasms/germplasm1\nhttp://localhost/faidare/germplasms/germplasm45\n");
        testSitemap(9, "http://localhost/faidare/germplasms/germplasm4\nhttp://localhost/faidare/germplasms/germplasm73\n");
        testSitemap(7, "");

        mockMvc.perform(get("/faidare/germplasms/sitemap-17.txt")
                            .contextPath("/faidare"))
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldExportMcpds() throws Exception {
        List<GermplasmMcpdVO> germplasms = Arrays.asList(
            Fixtures.createGermplasmMcpd(),
            Fixtures.createGermplasmMcpd()
        );

        GermplasmMcpdExportCommand command = new GermplasmMcpdExportCommand(
            Sets.newHashSet("g1", "g2"),
            Arrays.asList(GermplasmMcpdExportableField.PUID, GermplasmMcpdExportableField.INSTCODE));

        when(mockGermplasmRepository.scrollGermplasmMcpdsByIds(eq(command.getIds()), anyInt()))
            .thenAnswer(invocation -> germplasms.iterator());

        MvcResult mvcResult = mockMvc.perform(post("/germplasms/exports/mcpd")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsBytes(
                                                      command)))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/csv"))
               .andExpect(content().string("\"PUID\";\"INSTCODE\"\n" +
                                               "\"PUI1\";\"Inst1\"\n" +
                                               "\"PUI1\";\"Inst1\"\n"));
    }

    @Test
    void shouldExportPlantMaterials() throws Exception {
        List<GermplasmVO> germplasms = Arrays.asList(
            Fixtures.createGermplasm(),
            Fixtures.createGermplasm(),
            Fixtures.createGermplasmMinimal()
        );

        GermplasmExportCommand command = new GermplasmExportCommand(
            Sets.newHashSet("g1", "g2"),
            Arrays.asList(GermplasmExportableField.DOI,
                          GermplasmExportableField.ACCESSION_NUMBER,
                          GermplasmExportableField.ACCESSION_NAME));

        when(mockGermplasmRepository.scrollGermplasmsByIds(eq(command.getIds()), anyInt()))
            .thenAnswer(invocation -> germplasms.iterator());

        MvcResult mvcResult = mockMvc.perform(post("/germplasms/exports/plant-material")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsBytes(
                                                      command)))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/csv"))
                    .andExpect(content().string("\"DOI\";\"Accession number\";\"Accession name\"\n" +
                                                    "\"germplasmPUI\";\"1408\";\"BLE BARBU DU ROUSSILLON\"\n" +
                                                    "\"germplasmPUI\";\"1408\";\"BLE BARBU DU ROUSSILLON\"\n" +
                                                    "\"germplasmPUI mini\";\"1408-mini\";\"BLE BARBU DU ROUSSILLON mini\"\n"));
    }

    private void testSitemap(int index, String expectedContent) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/faidare/germplasms/sitemap-" + index + ".txt")
                                                  .contextPath("/faidare"))
                                     .andExpect(request().asyncStarted())
                                     .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string(expectedContent));
    }
}
