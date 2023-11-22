package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.dao.DocumentDao;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;

public interface GermplasmV2Dao extends DocumentDao<GermplasmV2VO>, GermplasmV2DaoCustom {


    GermplasmV2VO getByGermplasmDbId(String germplasmDbId);

    GermplasmV2VO getByGermplasmPUI(String germplasmPUI);

}
