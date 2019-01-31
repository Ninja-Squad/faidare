package fr.inra.urgi.gpds.domain.jsonld.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 */
public interface HasType {
    /**
     * rdf:type
     */
    @JsonView(JSONView.JSONLDFields.class)
    @JsonProperty("@type")
    List<String> getType();
}
