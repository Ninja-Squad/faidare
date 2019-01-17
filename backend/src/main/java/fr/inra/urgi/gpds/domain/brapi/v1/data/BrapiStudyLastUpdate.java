package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 *
 * @author gcornut
 *
 *
 */
public interface BrapiStudyLastUpdate extends Serializable {

	@JsonView(JSONView.BrapiFields.class)
	String getVersion();

	@JsonView(JSONView.BrapiFields.class)
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd, yyyy-MM-dd'T'HH:mm:ss'Z'")
	String getTimestamp();

}
