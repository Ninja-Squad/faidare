package fr.inrae.urgi.faidare.api.brapi.v1;

/**
 * Unit tests for {@link GermplasmController}
 *
 * @author Cpommier
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.inrae.urgi.faidare.Application;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GermplasmV1ControllerTest {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;

    TestRestTemplate testRestTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + contextPath + uri;
    }

    //TODO: maybe move in a fixture class
    //NB: onlyt part of the actual data are copied, especially for the site and panels ....
    // Use LENIET JSONAssert
    private String APACHE_GERMPLASM = """
            {
            "genus": "Triticum",
                 "panel": [
                     {
                         "id": 8,
                         "name": "SMALL_GRAIN_CEREALS_NETWORK_COL",
                         "germplasmCount": 1728
                     },
                     {
                         "id": 11,
                         "name": "BREEDWHEAT_PANEL",
                         "germplasmCount": 3047
                     }
                 ],
                 "source": "INRAE-URGI",
                 "groupId": 0,
                 "species": "aestivum",
                 "subtaxa": "subsp. aestivum",
                 "mlsStatus": "0",
                 "collection": [
                     {
                         "id": 128,
                         "name": "SMALL_GRAIN_CEREALS_NETWORK_COL",
                         "type": null,
                         "germplasmCount": 1410
                     }
                 ],
                 "studyDbIds": [
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNF9TZXRCMg==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNF9URUNI",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNV9TZXRBMg==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTRfU2V0QjI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTRfVEVDSA==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTVfU2V0QTI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTRfU2V0QjI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTRfVEVDSA==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTVfU2V0QTI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE0X1NldEIy",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE0X1RFQ0g=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE1X1NldEEy",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTRfU2V0QjI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTRfVEVDSA==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTVfU2V0QTI=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX09yZ2V2YWxfMjAxNF9TZXRCMg==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX09yZ2V2YWxfMjAxNV9TZXRBMg==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1NldEIy",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1NldEIyX1BJRVRJTi1WRVJTRQ==",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1RFQ0g=",
                     "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE1X1NldEEy"
                 ],
                 "genusSpecies": "Triticum aestivum",
                 "germplasmPUI": "https://doi.org/10.15454/T1DLTW",
                 "germplasmDbId": "dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzYxMDM5",
                 "germplasmName": "AO14022",
                 "instituteCode": "FRA040",
                 "instituteName": "GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales",
                 "accessionNames": [
                     "AO14022"
                 ],
                 "commonCropName": "Wheat",
                 "accessionNumber": "AO14022",
                 "evaluationSites": [
                     {
                         "siteId": 1994,
                         "latitude": 45.773,
                         "siteName": "Clermont-Ferrand",
                         "siteType": "Breeding and Evaluation site",
                         "longitude": 3.144
                     },
                     {
                         "siteId": 32824,
                         "latitude": 46.4,
                         "siteName": "Lusignan",
                         "siteType": "Breeding and Evaluation site",
                         "longitude": 0.07
                     },
                     {
                         "siteId": 33428,
                         "latitude": 49.87857,
                         "siteName": "Estrées-Mons",
                         "siteType": "Evaluation site",
                         "longitude": 3.007548
                     },
                     {
                         "siteId": 33818,
                         "latitude": 47.277,
                         "siteName": "Dijon",
                         "siteType": "Collecting and Evaluation site",
                         "longitude": 5.094
                     },
                     {
                         "siteId": 33985,
                         "latitude": 48.106,
                         "siteName": "Rennes",
                         "siteType": "Collecting and Evaluation site",
                         "longitude": -1.791
                     },
                     {
                         "siteId": 34064,
                         "latitude": 48.711,
                         "siteName": "Le Moulon",
                         "siteType": "Evaluation site",
                         "longitude": 2.16
                     },
                     {
                         "siteId": 34065,
                         "latitude": 48.838,
                         "siteName": "Orgeval",
                         "siteType": "Evaluation site",
                         "longitude": 1.953
                     }
                 ],
                 "holdingInstitute": {
                     "acronym": "INRAE_UMR_GDEC",
                     "address": "5 Chemin de Beaulieu, 63039 CLERMONT-FERRAND Cedex 2, France",
                     "webSite": "https://www6.clermont.inrae.fr/umr1095",
                     "organisation": "INRAE",
                     "instituteCode": "FRA040",
                     "instituteName": "GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales",
                     "instituteType": "Public-sector research organization"
                 },
                 "taxonCommonNames": [
                     "Blé tendre",
                     "Bread wheat",
                     "Soft wheat"
                 ],
                 "defaultDisplayName": "AO14022",
                 "genusSpeciesSubtaxa": "Triticum aestivum subsp. aestivum",
                 "safetyDuplicateInstitutes": {
                     "instituteCode": "FRA040",
                     "instituteName": "GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales"
                 },
                 "schema:identifier": "61039",
                 "germplasmURI": "urn:INRAE-URGI/germplasm/61039",
                 "studyURIs": [
                     "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Dijon_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Dijon_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Dijon_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Le_Moulon_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Le_Moulon_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Le_Moulon_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Lusignan_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Lusignan_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Lusignan_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Orgeval_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Orgeval_2015_SetA2",
                     "urn:INRAE-URGI/study/BTH_Rennes_2014_SetB2",
                     "urn:INRAE-URGI/study/BTH_Rennes_2014_SetB2_PIETIN-VERSE",
                     "urn:INRAE-URGI/study/BTH_Rennes_2014_TECH",
                     "urn:INRAE-URGI/study/BTH_Rennes_2015_SetA2"
                 ],
                 "node": "INRAE-URGI",
                 "databaseName": "brapi@INRAE-URGI",
                 "schema:includedInDataCatalog": "https://urgi.versailles.inrae.fr/gnpis",
                 "schema:name": "AO14022",
                 "@id": "urn:INRAE-URGI/germplasm/61039",
                 "@type": "germplasm"
           }
           """;

    @Test
    void should_call_calls() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/calls"),
                HttpMethod.GET, entity, String.class);
            assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode serverInfoFileJson = mapper.readTree(ResourceUtils.getFile("classpath:calls.json"));
        JSONAssert.assertEquals(
                serverInfoFileJson.toString(),
                response.getBody(), false);


    }


    @Test
    void should_get_germplasm_by_germplasmDbId() throws Exception {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm/dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzI3ODA3"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.accessionNumber");
        assertThat(accNumber).isEqualTo("36785");
    }

//    @Test
//    void should_get_germplasm_by_germplasmDbId_full_check() throws Exception {
//
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = testRestTemplate.exchange(
//                createURLWithPort("/brapi/v1/germplasm/dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzYxMDM5"),
//                HttpMethod.GET, entity, String.class);
//        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
//        String accNumber = JsonPath.parse(response.getBody()).read("$.result.accessionNumber");
//        assertThat(accNumber).isEqualTo("AO14022");
//        JSONObject actualgermplasmJson = new JSONObject(response.getBody()).getJSONObject("result");
//
//        assertThat(actualgermplasmJson).isNotNull();
//
//        JSONAssert.assertEquals(APACHE_GERMPLASM, actualgermplasmJson, JSONCompareMode.LENIENT);
//
//    }

    /*@Test
    void should_get_germplasm_by_accessionNumber_by_page_O_pageSize_1() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?accessionNumber=IRGC53931&page=0&pageSize=1"),
                HttpMethod.GET, entity, String.class);
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("IRGC53931");
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(1);
    }


    @Test
    void should_get_germplasm_by_accessionNumber() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?accessionNumber=IRGC53931"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        String accNumber = JsonPath.parse(response.getBody()).read("$.result.data.[0].accessionNumber");
        assertThat(accNumber).isEqualTo("IRGC53931");
    }*/

    @Test
    void should_search_germplasm_by_dbids() throws URISyntaxException {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("{\"germplasmDbIds\": [\"dXJuOklCRVQvYmU0ZTljZGMtNTgwMC00NDU3LWE2YzgtNDA1NjNjMDI3ZGQ5\"]}", headers);

        URI uri = new URI(createURLWithPort("/brapi/v2/search/germplasm"));
        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void should_get_all_germplasm_first_page(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(10);
        Integer page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        assertThat(page).isEqualTo(0);
    }

    @Test
    void should_get_all_germplasm_2nd_page(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/brapi/v1/germplasm?page=1"),
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Integer pageSize = JsonPath.parse(response.getBody()).read("$.metadata.pagination.pageSize");
        assertThat(pageSize).isEqualTo(10);
        Integer page = JsonPath.parse(response.getBody()).read("$.metadata.pagination.currentPage");
        assertThat(page).isEqualTo(1);
    }

    @Test
    void germplasmAttribute() {
    }
}
