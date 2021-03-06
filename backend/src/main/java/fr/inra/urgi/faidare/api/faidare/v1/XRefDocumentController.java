package fr.inra.urgi.faidare.api.faidare.v1;

import java.util.List;

import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Imported and adapted from unified-interface legacy
 */
@Tag(name = "FAIDARE API", description = "Extended FAIDARE API")
@RestController
public class XRefDocumentController {

    private final XRefDocumentRepository repository;

    @Autowired
    public XRefDocumentController(XRefDocumentRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Find xref documents")
    @GetMapping(value = "/faidare/v1/xref/documentbyfulltextid")
    public PaginatedList<XRefDocumentVO> documentByFullTextId(
        @RequestParam(required = false, value = "entryType") String entryType,
        @RequestParam(required = false) List<String> linkedResourcesID
    ) {
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setEntryType(entryType);
        criteria.setLinkedResourcesID(linkedResourcesID);
	return repository.find(criteria);
    }

}
