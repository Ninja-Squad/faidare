package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;

/**
 * Generic Brapi study.
 *
 * No <code>@JsonDeserialize</code> is declared here because this interface is
 * not used directly (only extended).
 *
 * Extended in {@link BrapiStudySummary} and {@link BrapiStudyDetail}
 *
 * @author gcornut
 *
 *
 */
public interface BrapiTrialStudy extends Serializable {

	// Study
	@JsonView(JSONView.BrapiFields.class)
	String getStudyDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getStudyName();

	// Location
	@JsonView(JSONView.BrapiFields.class)
	String getLocationDbId();

	@JsonView(JSONView.BrapiFields.class)
	String getLocationName();

}
