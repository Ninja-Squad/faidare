package fr.inra.urgi.faidare.api.faidare.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.File;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.google.common.base.Strings;
import fr.inra.urgi.faidare.api.BadRequestException;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.api.brapi.v1.GermplasmController;
import fr.inra.urgi.faidare.domain.criteria.FaidareGermplasmPOSTShearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.service.es.GermplasmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAIDARE API", description = "Extended FAIDARE API")
@RestController
@RequestMapping(value = "/faidare/v1/germplasm")
public class GnpISGermplasmController {

    private final GermplasmService germplasmService;
    private final static Logger LOGGER = LoggerFactory.getLogger(GermplasmController.class);

    @Autowired
    public GnpISGermplasmController(GermplasmService germplasmService) {
        this.germplasmService = germplasmService;
    }

    @Operation(summary = "Search germplasm by ID or PUI")
    @GetMapping
    public GermplasmVO get(
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String pui
    ) {
        GermplasmVO germplasm = null;
        boolean hasId = !Strings.isNullOrEmpty(id);
        boolean hasPui = !Strings.isNullOrEmpty(pui);

        if (!hasId && !hasPui) {
            throw new BadRequestException("You must provide at least one identifier (PUI or ID) in the query params.");
        }

        if (hasId && !hasPui) {
            germplasm = germplasmService.getById(id);
        } else {
            GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
            criteria.setGermplasmPUI(Collections.singletonList(pui));
            if (hasId) {
                criteria.setGermplasmDbId(Collections.singletonList(id));
            }
            PaginatedList<GermplasmVO> pager = germplasmService.find(criteria);
            if (pager != null && pager.size() == 1) {
                germplasm = pager.get(0);
            }
        }

        if (germplasm == null) {
            String message = "Germplasm not found";
            if (hasId) {
                message += " for id: '" + id + "'";
            }
            if (hasPui) {
                if (hasId) message += " and";
                message += " for pui: '" + pui + "'";
            }

            throw new NotFoundException(message);
        }
        return germplasm;
    }


    /**
     * Webservice for exporting germplasm by criteria into a CSV.
     * <p>
     * See http://stackoverflow.com/questions/5673260/downloading-a-file-from-
     * spring-controllers resp.setContentType("application/csv");
     *
     * <pre>
     * resp.setContentType("application/vnd.ms-excel");
     * resp.setContentType("application/zip");
     * </pre>
     */
    @PostMapping(value = "/csv", produces = "text/csv", consumes = APPLICATION_JSON_VALUE)
    public FileSystemResource export(@RequestBody @Valid GermplasmPOSTSearchCriteria criteria, HttpServletResponse response) {

        try {
            File exportFile = germplasmService.exportCSV(criteria);
            response.setHeader("Content-Disposition", "attachment; filename=germplasm.gnpis.csv");
            return new FileSystemResource(exportFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred when exporting germplasm: " + e.getMessage() + ".", e);
        }
    }

    @PostMapping(value = "/germplasm-list-csv", produces = "text/csv", consumes = APPLICATION_JSON_VALUE)
    public FileSystemResource export(@RequestBody @Valid FaidareGermplasmPOSTShearchCriteria criteria, HttpServletResponse response) {

        long limitResult = 50000L;
        long nbResult = germplasmService.germplasmFind(criteria).getMetadata().getPagination().getTotalCount();

        if (!(nbResult > limitResult)) {
            try {
                File exportFile = germplasmService.exportListGermplasmCSV(criteria);
                response.setHeader("Content-Disposition", "attachment; filename=germplasm.gnpis.csv");
                return new FileSystemResource(exportFile);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("An error occurred when exporting germplasm: " + e.getMessage() + ".", e);
            }
        }
        return null;
    }



    @PostMapping(value = "/germplasm-mcpd-csv", produces = "text/csv", consumes = APPLICATION_JSON_VALUE)
    public FileSystemResource exportMcpd(@RequestBody @Valid FaidareGermplasmPOSTShearchCriteria criteria, HttpServletResponse response) {

        long limitResult = 50000L;
        long nbResult = germplasmService.germplasmFind(criteria).getMetadata().getPagination().getTotalCount();

        if (!(nbResult > limitResult)) {
            try {
                File exportFile = germplasmService.exportGermplasmMcpd(criteria);
                response.setHeader("Content-Disposition", "attachment; filename=germplasm.gnpis.csv");
                return new FileSystemResource(exportFile);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("An error occurred when exporting germplasm: " + e.getMessage() + ".", e);
            }
        }
        return null;
    }

    @Operation(summary = "Search list of germplasm")
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE)
    public GermplasmSearchResponse germplasmSearch(@RequestBody @Valid FaidareGermplasmPOSTShearchCriteria criteria) {
        try {
            return germplasmService.germplasmFind(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
