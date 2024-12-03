package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Dao;
import fr.inrae.urgi.faidare.domain.PuiNameValueVO;
import fr.inrae.urgi.faidare.domain.brapi.GermplasmSitemapVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataElasticsearchTest
class GermplasmV1DaoTest {

    @Autowired
    protected GermplasmV1Dao germplasmDao;

    /**
          Code tested For futur implementation in Faidare cards:
              public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
              GermplasmVO germplasm = germplasmDao.getByGermplasmDbId(germplasmId);

              &#064;GetMapping(params  = "id")
              public ModelAndView getById(@RequestParam("id") String germplasmId) {
              GermplasmVO germplasm = germplasmRepository.getByGermplasmDbId(germplasmId);
     */
    @Test
    void getByGermplasmDbId_should_return_one_result() {
        GermplasmV1VO germplasmVo =
                germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");

        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmDbId())
                .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
    }


    /**
     * Code tested For Faidare cards:
     * &#064;GetMapping(params  = "pui")
     *     public ModelAndView getByPui(@RequestParam("pui") String pui) {
     *     GermplasmV1VO germplasmVo =
     *                 germplasmDao.getByGermplasmPUI(pui);
     */
    @Test
    void getByGermplasmPUI_should_return_one_result() {
        GermplasmV1VO germplasmVo =
                germplasmDao.getByGermplasmPUI("https://doi.org/10.15454/4NCDUP");
        assertThat(germplasmVo).isNotNull();
        assertThat(germplasmVo.getGermplasmPUI())
                .isEqualTo("https://doi.org/10.15454/4NCDUP");
        assertThat(germplasmVo.getGermplasmDbId())
                    .isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2MDAy");
    }

// test :     Iterator<GermplasmVO> scrollGermplasmsByIds(Set<String> ids, int fetchSize);

    /**
     * Should be usable for backend/src/main/java/fr/inra/urgi/faidare/web/germplasm/GermplasmController.java

     &#064;PostMapping("/exports/plant-material")
     &#064;ResponseBody
     public ResponseEntity<StreamingResponseBody> export(@Validated @RequestBody GermplasmExportCommand command) {
     List<GermplasmExportableField> fields = getFieldsToExport(command);

     StreamingResponseBody body = out -> {
     Iterator<GermplasmVO> iterator = germplasmRepository.scrollGermplasmsByIds(command.getIds(), 1000);
     germplasmExportService.export(out, iterator, fields);

     By using germplasmDao.scrollGermplasmsByGermplasmDbIds instead of germplasmRepository.scrollGermplasmsByIds
     */
    @Test
    void should_get_germplasms_by_id1(){
        Set<String> dbIds = Set.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",//recital
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",//soisson
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1");//TREMIE
        SearchHitsIterator<GermplasmV1VO> gVoIter = germplasmDao.scrollGermplasmsByGermplasmDbIds(dbIds, 10);
        assertThat(gVoIter).isNotNull();
        assertThat(gVoIter.getTotalHits()).isEqualTo(3);
        GermplasmV1VO gVo = Objects.requireNonNull(gVoIter.stream()
                .filter(gVoHit -> "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4".equals(gVoHit.getContent().getGermplasmDbId()))
                .findAny().orElse(null)).getContent();
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmName()).isEqualTo("SOISSONS");
    }

    /**
     * Code to use in controllers for
     * germplasmCriteria.setGermplasmDbIds(Lists.newArrayList(study.getGermplasmDbIds()));
     *             return germplasmRepository.find(germplasmCriteria)
     *
     */
    @Test
    void should_get_germplasms_by_id2(){
        Set<String> dbIds = Set.of("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5",//recital
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MzI4",//soisson
            "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0NTA1",//TREMIE
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1NTg1",//ISENGRAIN
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1NjEy",//APACHE
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1ODk1",//CF00193
                "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI1OTEz");//CAPHORN
        SearchHitsIterator<GermplasmV1VO> gVoIter = germplasmDao.scrollGermplasmsByGermplasmDbIds(dbIds, 3);
        assertThat(gVoIter).isNotNull();
        assertThat(gVoIter.getTotalHits()).isEqualTo(7);
        Set<String> resultSet = new HashSet<>();
        while (gVoIter.hasNext()){
            GermplasmV1VO v1VO = gVoIter.next().getContent();
            resultSet.add( v1VO.getGermplasmDbId());
        }
        assertThat(resultSet.size()).isEqualTo(7);
        assertThat(resultSet).isEqualTo(dbIds);
    }

    @Test
    void findByGermplasmDbIdIn() {
        String id = "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5";
        List<GermplasmV1VO> list = germplasmDao.findByGermplasmDbIdIn(Set.of(id)).toList();
        assertThat(list).extracting(GermplasmV1VO::getGermplasmDbId).containsOnly(id);
    }

    @Test
    void findAllForSitemap() {
        List<GermplasmSitemapVO> list = germplasmDao.findAllForSitemap().toList();
        assertThat(list.size()).isGreaterThan(1);
        assertThat(list.get(0)).isInstanceOf(GermplasmSitemapVO.class);
        assertThat(list.get(0).getGermplasmDbId()).isNotNull();
    }

    @Test
    void should_find_by_germplasmId(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI0MDU5");
        assertThat(gVo.getGermplasmPUI()).isEqualTo("https://doi.org/10.15454/WL6NIE");
        assertThat(gVo.getGermplasmName()).isEqualTo("RECITAL");
        assertThat(gVo.getAccessionNames()).contains("RECITAL");
    }

    @Test
    void should_find_by_germplasmId_with_collecting_site2(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU5");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU5");
        assertThat(gVo.getCollectingSite()).isNotNull();
        assertThat(gVo.getCollectingSite().getSiteId()).isEqualTo("1626");
        assertThat(gVo.getCollectingSite().getSiteName()).isEqualTo("France");
        assertThat(gVo.getCollectingSite().getLatitude()).isEqualTo(47.428085);
        assertThat(gVo.getCollectingSite().getLongitude()).isEqualTo(2.680664);
        assertThat(gVo.getCollectingSite().getSiteType()).isEqualTo("Origin, Breeding and Collecting site");
    }

    @Test
    void should_get_by_germplasm_id_and_have_collector(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU3");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI2ODU3");
        assertThat(gVo.getCollector()).isNotNull();
        assertThat(gVo.getCollector().getInstitute().getAcronym()).isEqualTo("INRAE_UMR_GDEC");
        //assertThat(gVo.getCollector().getMaterialType()).isEqualTo("Cutting"); is null in this case
        //assertThat(gVo.getCollector().getAccessionCreationDate()).isEqualTo(20091200); null also
    }

    @Test
    void should_get_by_germplasm_id_and_have_children(){
        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTY1");
        assertThat(gVo).isNotNull();
        assertThat(gVo.getGermplasmDbId()).isEqualTo("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzQzMTY1");
        assertThat(gVo.getChildren()).isNotNull();
        assertThat(gVo.getChildren().size()).isEqualTo(11);
        assertThat(gVo.getChildren().get(0).getFirstParentPUI()).isEqualTo("https://doi.org/10.15454/SPA0QI");
        PuiNameValueVO pnv = new PuiNameValueVO();
        pnv.setPui("https://doi.org/10.15454/OMH2PC");
        assertThat(gVo.getChildren().get(0).getSibblings()).isNotNull().isNotEmpty().contains(pnv);
    }
    //TODO: criteria search, to reactivate for full BrAPIV1
    /*
    @Test
    void custom_should_search_by_accessionNumber(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setAccessionNumber(List.of("IRGC53931"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getAccessionNumber()).isEqualTo("IRGC53931");
    }

    @Test
    void custom_should_search_by_binomialNames(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setBinomialNames(List.of("Zea mays"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenusSpecies()).isEqualTo("Zea mays");
    }

    @Test
    void custom_should_search_by_collection(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setCollections((List.of("Wheat INRA collection")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("Wheat INRA collection") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getCollection().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_panel(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setCollections((List.of("RIL")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("RIL") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPanel().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_pop(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setCollections((List.of("ILN028")));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        Predicate<CollPopVO> streamsPredicate = item -> item.getName().equals("ILN028") ;
        assertThat(germplasmVOs.getResult().getData().get(0).getPopulation().stream().filter(streamsPredicate)).isNotEmpty();
    }

    @Test
    void custom_should_search_by_commonCropNames(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setCommonCropNames(List.of("Maize"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        GermplasmV2VO toto = germplasmVOs.getResult().getData().get(0);
        assertThat(toto.getCommonCropName()).isEqualTo("Maize");
    }

    void custom_should_search_by_externalReferenceIDs(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setExternalReferenceIDs(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_externalReferenceIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setExternalReferenceIds(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_externalReferenceSources(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setExternalReferenceSources(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
    //    assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_familyCodes(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setFamilyCodes(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }
    @Test
    void custom_should_search_by_genus(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setGenus(List.of("Oryza"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Oryza");
    }

    @Test
    void custom_should_search_by_genus_pageSize1(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setGenus(List.of("Oryza"));
        gCrit.setPageSize(1);
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getPageSize()).isEqualTo(1);
        assertThat(germplasmVOs.getMetadata().getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(60);
        assertThat(germplasmVOs.getResult().getData().get(0).getGenus()).isEqualTo("Oryza");
    }

    @Test
    void custom_should_search_by_germplasmDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setGermplasmDbIds(List.of("dXJuOklCRVQvYmU0ZTljZGMtNTgwMC00NDU3LWE2YzgtNDA1NjNjMDI3ZGQ5", "aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L1NQQTBRSQ=="));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isEqualTo(2);
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmDbId()).isEqualTo("dXJuOklCRVQvYmU0ZTljZGMtNTgwMC00NDU3LWE2YzgtNDA1NjNjMDI3ZGQ5");
        assertThat(germplasmVOs.getResult().getData().get(1).getGermplasmDbId()).isEqualTo("aHR0cHM6Ly9kb2kub3JnLzEwLjE1NDU0L1NQQTBRSQ==");
    }

    @Test
    void custom_should_search_by_germplasmName(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setGermplasmName(List.of("APACHE"));
        BrapiListResponse<GermplasmV2VO> pgVo = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(pgVo).isNotNull();
        assertThat(pgVo.getResult().getData()).isNotEmpty();
        assertThat(pgVo.getResult().getData().get(0).getGermplasmName()).isEqualTo("APACHE");
    }

    @Test
    void custom_should_search_by_germplasmPUIs(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setGermplasmPUIs(List.of("27756e94-501e-41f4-8482-250c6f3527b7"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getGermplasmPUI()).isEqualTo("27756e94-501e-41f4-8482-250c6f3527b7");
    }

    @Test
    void custom_should_search_by_instituteCodes(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setInstituteCodes(List.of("PHL001"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getInstituteCode()).isEqualTo("PHL001");
    }


    void custom_should_search_by_parentDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setParentDbIds(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_progenyDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setProgenyDbIds(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_programDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setProgramDbIds(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_programNames(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setProgramNames(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }
    @Test
    void custom_should_search_by_species(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setSpecies(List.of("aestivum"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getSpecies()).isEqualTo("aestivum");
    }


    @Test
    void custom_should_search_by_studyDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setStudyDbIds(List.of("dXJuOklCRVQvc3R1ZHkvMQ=="));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getStudyDbIds()).contains("dXJuOklCRVQvc3R1ZHkvMQ==");
    }

    void custom_should_search_by_studyNames(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setStudyNames(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        //assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }
    @Test
    void custom_should_search_by_synonyms(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setSynonyms(List.of("Hsinchu 103"));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
        assertThat(germplasmVOs.getResult().getData().get(0).getSynonyms()).contains("Hsinchu 103");
    }

    void custom_should_search_by_trialDbIds(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setTrialDbIds(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
//        assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }

    void custom_should_search_by_trialNames(){
        GermplasmCriteria gCrit = new GermplasmCriteria();
        gCrit.setTrialNames(List.of(""));
        BrapiListResponse<GermplasmV2VO> germplasmVOs = germplasmDao.findGermplasmsByCriteria(gCrit);
        assertThat(germplasmVOs).isNotNull();
        assertThat(germplasmVOs.getMetadata().getPagination().getTotalCount()).isGreaterThan(0);
 //       assertThat(germplasmVOs.getSearchHits().getSearchHit(0).getContent().get()).isEqualTo("");
    }
    */
}
