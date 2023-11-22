package fr.inrae.urgi.faidare.domain;

public class CollPopVO {

    private Integer germplasmCount;

    private PuiNameValueVO germplasmRef;

    private Long id;

    private String name;

    private String type;


    public Integer getGermplasmCount() {
        return germplasmCount;
    }

    public void setGermplasmCount(Integer germplasmCount) {
        this.germplasmCount = germplasmCount;
    }

    public PuiNameValueVO getGermplasmRef() {
        return germplasmRef;
    }

    public void setGermplasmRef(PuiNameValueVO germplasmRef) {
        this.germplasmRef = germplasmRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
