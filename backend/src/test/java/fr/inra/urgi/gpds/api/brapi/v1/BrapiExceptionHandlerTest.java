package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.gpds.service.es.GermplasmService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {GermplasmController.class})
class BrapiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GermplasmService service;

    @MockBean
    private GermplasmAttributeRepository attributeRepository;

    @Test
    void should_Throw_Pagination_Max_Size_Exception() throws Exception {
        mockMvc.perform(get("/brapi/v1/germplasm?pageSize=99999")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("{\n" +
                "  \"metadata\": {\n" +
                "    \"pagination\": {\n" +
                "      \"pageSize\": 0,\n" +
                "      \"totalCount\": 0,\n" +
                "      \"totalPages\": 0,\n" +
                "      \"currentPage\": 0\n" +
                "    },\n" +
                "    \"status\": [{\n" +
                "      \"name\": \"Bad Request: Page size cannot be above 1000\",\n" +
                "      \"code\": \"400\"\n" +
                "    }],\n" +
                "    \"datafiles\": []\n" +
                "  },\n" +
                "  \"result\": null\n" +
                "}"));
    }

    @Test
    void should_Throw_Pagination_Min_Size_Exception() throws Exception {
        mockMvc.perform(get("/brapi/v1/germplasm?pageSize=-1")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("{\n" +
                "  \"metadata\": {\n" +
                "    \"pagination\": {\n" +
                "      \"pageSize\": 0,\n" +
                "      \"totalCount\": 0,\n" +
                "      \"totalPages\": 0,\n" +
                "      \"currentPage\": 0\n" +
                "    },\n" +
                "    \"status\": [{\n" +
                "      \"name\": \"Bad Request: Page size cannot be below 1\",\n" +
                "      \"code\": \"400\"\n" +
                "    }],\n" +
                "    \"datafiles\": []\n" +
                "  },\n" +
                "  \"result\": null\n" +
                "}"));
    }


    @Test
    void should_Throw_Pagination_Min_Page_Exception() throws Exception {
        mockMvc.perform(get("/brapi/v1/germplasm?page=-1")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("{\n" +
                "  \"metadata\": {\n" +
                "    \"pagination\": {\n" +
                "      \"pageSize\": 0,\n" +
                "      \"totalCount\": 0,\n" +
                "      \"totalPages\": 0,\n" +
                "      \"currentPage\": 0\n" +
                "    },\n" +
                "    \"status\": [{\n" +
                "      \"name\": \"Bad Request: Page number cannot be below 0\",\n" +
                "      \"code\": \"400\"\n" +
                "    }],\n" +
                "    \"datafiles\": []\n" +
                "  },\n" +
                "  \"result\": null\n" +
                "}"));
    }

    @Test
    void should_Throw_Invalid_Param_Value_Exception() throws Exception {
        mockMvc.perform(get("/brapi/v1/germplasm?page=foo")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("{\n" +
                "  \"metadata\": {\n" +
                "    \"pagination\": {\n" +
                "      \"pageSize\": 0,\n" +
                "      \"totalCount\": 0,\n" +
                "      \"totalPages\": 0,\n" +
                "      \"currentPage\": 0\n" +
                "    },\n" +
                "    \"status\": [{\n" +
                "      \"name\": \"Bad Request: Failed to convert property value of type 'java.lang.String' to required type 'java.lang.Long' for property 'page'; nested exception is java.lang.NumberFormatException: For input string: \\\"foo\\\"\",\n" +
                "      \"code\": \"400\"\n" +
                "    }],\n" +
                "    \"datafiles\": []\n" +
                "  },\n" +
                "  \"result\": null\n" +
                "}"));
    }

}