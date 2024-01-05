package fr.inrae.urgi.faidare.domain;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(
    indexName = "faidare_germplasm_dev-group0",
    //indexName = "faidare_germplasm_beta-group0",
    createIndex = false
)
public class CollPopVO {

    private Integer germplasmCount;

    private PuiNameValueVO germplasmRef;

    private String id;

    private String name;

    private String type;

    public CollPopVO(String name, String id) {
        this.name = name;
        this.id = id;
    }


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
