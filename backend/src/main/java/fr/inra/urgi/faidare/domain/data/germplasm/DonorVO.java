package fr.inra.urgi.faidare.domain.data.germplasm;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmDonor;

import java.io.Serializable;

public class DonorVO implements Serializable, BrapiGermplasmDonor {

    private static final long serialVersionUID = 7976964107440923573L;

    private String donorGermplasmPUI;
    private String donorAccessionNumber;
    private String donorInstituteCode;

    @Override
    public String getDonorGermplasmPUI() {
        return donorGermplasmPUI;
    }

    public void setDonorGermplasmPUI(String donorGermplasmPUI) {
        this.donorGermplasmPUI = donorGermplasmPUI;
    }

    @Override
    public String getDonorAccessionNumber() {
        return donorAccessionNumber;
    }

    public void setDonorAccessionNumber(String donorAccessionNumber) {
        this.donorAccessionNumber = donorAccessionNumber;
    }

    @Override
    public String getDonorInstituteCode() {
        return donorInstituteCode;
    }

    public void setDonorInstituteCode(String donorInstituteCode) {
        this.donorInstituteCode = donorInstituteCode;
    }

}
