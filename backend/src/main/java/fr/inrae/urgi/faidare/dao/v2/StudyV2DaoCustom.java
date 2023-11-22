package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import org.springframework.data.elasticsearch.core.SearchHits;


public interface StudyV2DaoCustom {

    SearchHits<StudyV2VO> findStudiesByCriteria(StudyCriteria studyCriteria);
}
