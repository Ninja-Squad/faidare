package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;

public interface GermplasmV2DaoCustom {
    BrapiListResponse<GermplasmV2VO> findGermplasmsByCriteria(GermplasmCriteria germplasmCriteria);

}
