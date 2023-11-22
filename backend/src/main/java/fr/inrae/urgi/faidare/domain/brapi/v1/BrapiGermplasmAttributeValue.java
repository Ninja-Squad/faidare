package fr.inrae.urgi.faidare.domain.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inrae.urgi.faidare.domain.JSONView;

import java.util.Date;

// FIXME JBN the BrapiGermplasmAttributeValue class has been kept to make the code compile, but needs to be reimplemented
/**
 * @author gcornut
 */
public interface BrapiGermplasmAttributeValue {
    @JsonView(JSONView.BrapiFields.class)
    String getAttributeDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getAttributeName();

    @JsonView(JSONView.BrapiFields.class)
    String getAttributeCode();

    @JsonView(JSONView.BrapiFields.class)
    String getValue();

    @JsonView(JSONView.BrapiFields.class)
    Date getDeterminedDate();
}
