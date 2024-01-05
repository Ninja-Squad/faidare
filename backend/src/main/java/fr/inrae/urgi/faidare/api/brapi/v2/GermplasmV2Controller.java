package fr.inrae.urgi.faidare.api.brapi.v2;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inrae.urgi.faidare.dao.v2.CollectionV2Dao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmCriteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmMcpdDao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Breeding API", description = "BrAPI endpoint")
@RestController
@RequestMapping({"/brapi/v2"})
public class GermplasmV2Controller {

    private final GermplasmV2Dao germplasmDao;

    private final CollectionV2Dao collectionDao;

    private final GermplasmMcpdDao germplasmMcpdDao;

    public GermplasmV2Controller(GermplasmV2Dao germplasmDao, CollectionV2Dao collectionDao, GermplasmMcpdDao germplasmMcpdDao) {
        this.germplasmDao = germplasmDao;
        this.collectionDao = collectionDao;
        this.germplasmMcpdDao = germplasmMcpdDao;
    }




    @Value("classpath:serverinfo.json")
    Resource serverInfoFile;

    @GetMapping("/serverinfo")
    public @ResponseBody JsonNode serverinfo() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(serverInfoFile.getInputStream());
        //JsonNode response = mapper.readTree(serverInfoFile.getFile());
        return response;
    }

    @GetMapping("/germplasm")
    public BrapiListResponse<GermplasmV2VO> germplasm(
        @RequestParam MultiValueMap<String, String> parameters){
        //TODO: this could be replaced with  @RequestParam GermplasmCriteria gCrit) to be adjusted/tested
        GermplasmCriteria gCrit = new GermplasmCriteria();
        if (parameters.get("accessionNumber") != null ){ gCrit.setAccessionNumber(parameters.get("accessionNumber")); }
        if (parameters.get("binomialName") != null ){ gCrit.setAccessionNumber(parameters.get("binomialName")); }
        if (parameters.get("genus") != null ){ gCrit.setAccessionNumber(parameters.get("genus")); }
        if (parameters.get("species") != null ){ gCrit.setAccessionNumber(parameters.get("species")); }
        if (parameters.get("synonym") != null ){ gCrit.setAccessionNumber(parameters.get("synonym")); }
        if (parameters.get("parentDbId") != null ){ gCrit.setAccessionNumber(parameters.get("parentDbId")); }
        if (parameters.get("progenyDbIb") != null ){ gCrit.setAccessionNumber(parameters.get("progenyDbIb")); }
        if (parameters.get("commonCropName") != null ){ gCrit.setAccessionNumber(parameters.get("commonCropName")); }
        if (parameters.get("programDbId") != null ){ gCrit.setAccessionNumber(parameters.get("programDbId")); }
        if (parameters.get("trialDbId") != null ){ gCrit.setAccessionNumber(parameters.get("trialDbId")); }
        if (parameters.get("studyDbId") != null ){ gCrit.setAccessionNumber(parameters.get("studyDbId")); }
        if (parameters.get("germplasmDbId") != null ){ gCrit.setAccessionNumber(parameters.get("germplasmDbId")); }
        if (parameters.get("germplasmName") != null ){ gCrit.setAccessionNumber(parameters.get("germplasmName")); }
        if (parameters.get("germplasmPUI") != null ){ gCrit.setAccessionNumber(parameters.get("germplasmPUI")); }
        if (parameters.get("externalReferenceID") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceID")); }
        if (parameters.get("externalReferenceId") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceId")); }
        if (parameters.get("externalReferenceSource") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceSource")); }

        if (parameters.get("page") != null ){ gCrit.setPage(Integer.valueOf(parameters.get("page").get(0))); }
        if (parameters.get("pageSize") != null ){ gCrit.setPageSize(Integer.valueOf(parameters.get("pageSize").get(0))); }
        BrapiListResponse<GermplasmV2VO> germplasmV2VOs = germplasmDao.findGermplasmsByCriteria(gCrit);

        return germplasmV2VOs;
    }

    @PostMapping(value = "/search/germplasm", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<GermplasmV2VO> searchGermplasm(@RequestBody GermplasmCriteria gCrit){

        return germplasmDao.findGermplasmsByCriteria(gCrit);
    }

    @GetMapping("/germplasm/{germplasmDbId}")
    public BrapiSingleResponse<GermplasmV2VO> byGermplasmDbId(@PathVariable String germplasmDbId) throws Exception {

        GermplasmV2VO gV2Vo = germplasmDao.getByGermplasmDbId(germplasmDbId);
        return BrapiSingleResponse.brapiResponseOf(gV2Vo, Pageable.ofSize(1));
        //return gV2Vo;
    }



    @GetMapping("/collection")
    public BrapiListResponse<CollPopVO> getCollections(){
        return collectionDao.getAllCollections();

    }
    /*
    @GetMapping("/germplasmAttribute")
    public List<SearchHit<GermplasmAttributeV2VO>> germplasmAttribute(@RequestParam(value = "attributeValueDbId", defaultValue = "") String attributeValueDbId,
                               @RequestParam(value = "attributeDbId", defaultValue = "") String attributeDbId,
                               @RequestParam(value = "attributeName", defaultValue = "") String attributeName,
                               @RequestParam(value = "commonCropName", defaultValue = "") String commonCropName,
                               @RequestParam(value = "programDbId", defaultValue = "") String programDbId,
                               @RequestParam(value = "germplasmDbId", defaultValue = "") String germplasmDbId,
                               @RequestParam(value = "externalReferenceID", defaultValue = "") String externalReferenceID,
                               @RequestParam(value = "externalReferenceId", defaultValue = "") String externalReferenceId,
                               @RequestParam(value = "externalReferenceSource", defaultValue = "") String externalReferenceSource,
                               @RequestParam MultiValueMap<String, String> parameters){

        GermplasmCriteria gCrit = new GermplasmCriteria();
        if (parameters.get("attributeValueDbId") != null ){ gCrit.setAccessionNumber(parameters.get("attributeValueDbId").get(0)); }
        if (parameters.get("attributeDbId") != null ){ gCrit.setAccessionNumber(parameters.get("attributeDbId").get(0)); }
        if (parameters.get("attributeName") != null ){ gCrit.setAccessionNumber(parameters.get("attributeName").get(0)); }
        if (parameters.get("commonCropName") != null ){ gCrit.setAccessionNumber(parameters.get("commonCropName").get(0)); }
        if (parameters.get("programDbId") != null ){ gCrit.setAccessionNumber(parameters.get("programDbId").get(0)); }
        if (parameters.get("germplasmDbId") != null ){ gCrit.setAccessionNumber(parameters.get("germplasmDbId").get(0)); }
        if (parameters.get("externalReferenceID") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceID").get(0)); }
        if (parameters.get("externalReferenceId") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceId").get(0)); }
        if (parameters.get("externalReferenceSource") != null ){ gCrit.setAccessionNumber(parameters.get("externalReferenceSource").get(0)); }

        SearchPage<GermplasmAttributeV2VO> germplasmAttribute = germplasmDao.findGermplasmsByCriteria(gCrit);

        return germplasmAttribute.getContent();
    }
    */
}
