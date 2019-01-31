package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

/**
 * @author gcornut
 */
public interface IncludedInDataCatalog {

    /**
     * URI of the data catalog this dataset is part of
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("schema:includedInDataCatalog")
    String getSourceUri();

}
