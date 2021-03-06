package fr.inra.urgi.faidare.api.brapi.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CallsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_Get_Page_Size() throws Exception {
        int pageSize = 3;
        mockMvc.perform(get("/brapi/v1/calls?pageSize=" + pageSize))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.metadata.status", hasSize(0)))
            .andExpect(jsonPath("$.result.data", hasSize(pageSize)));
    }

    @Test
    void should_Fail_Page_Overflow() throws Exception {
        mockMvc.perform(get("/brapi/v1/calls?pageSize=100&page=2"))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("{" +
                "\"metadata\":{" +
                    "\"pagination\":{\"pageSize\":100,\"currentPage\":2,\"totalCount\":27,\"totalPages\":1}," +
                    "\"status\":[{" +
                        "\"name\":\"Bad Request: The current page should be strictly less than the total number of pages.\"," +
                        "\"code\":\"400\"" +
                    "}]," +
                    "\"datafiles\":[]" +
                "}," +
                "\"result\":{\"data\":null}" +
            "}"
            ));
    }

    @Test
    void should_Get_All() throws Exception {
        mockMvc.perform(get("/brapi/v1/calls?pageSize=1000"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\n" +
                "  \"metadata\": {\n" +
                "    \"pagination\": {\n" +
                "      \"pageSize\": 1000,\n" +
                "      \"currentPage\": 0,\n" +
                "      \"totalCount\": 27,\n" +
                "      \"totalPages\": 1\n" +
                "    },\n" +
                "    \"status\": [],\n" +
                "    \"datafiles\": []\n" +
                "  },\n" +
                "  \"result\": {\n" +
                "    \"data\": [{\n" +
                "      \"call\": \"calls\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm-search\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"POST\", \"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm/{germplasmDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm/{germplasmDbId}/attributes\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm/{germplasmDbId}/mcpd\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm/{germplasmDbId}/pedigree\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"germplasm/{germplasmDbId}/progeny\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"locations\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"locations/{locationDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"ontologies\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"phenotypes-search\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"POST\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"programs\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"programs-search\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"POST\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"programs/{programDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies-search\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"POST\", \"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}/germplasm\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}/observationUnits\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}/observationVariables\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}/observationunits\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"studies/{studyDbId}/observationvariables\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"trials\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"trials/{trialDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"variables\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }, {\n" +
                "      \"call\": \"variables/{observationVariableDbId}\",\n" +
                "      \"datatypes\": [\"json\"],\n" +
                "      \"methods\": [\"GET\"],\n" +
                "      \"versions\": [\"1.0\", \"1.1\", \"1.2\"]\n" +
                "    }]\n" +
                "  }\n" +
                "}"));
    }


}
