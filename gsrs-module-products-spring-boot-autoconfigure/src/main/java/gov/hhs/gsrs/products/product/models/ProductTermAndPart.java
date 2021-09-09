package gov.hhs.gsrs.products.product.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_TERM_PART")
public class ProductTermAndPart extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodTermSeq", sequenceName="SRSCID_SQ_PRODUCT_TERM_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodTermSeq")
    @Column(name="PRODUCT_TERM_ID")
    public Long id;

    @Column(name="PRODUCT_TERM")
    public String productTerm;

    @Column(name="PRODUCT_TERM_PART")
    public String productTermPart;

    /*
    @Version
    public Long internalVersion;

    @Column(name = "CREATED_BY")
    public String createdBy;

    @Column(name = "MODIFIED_BY")
    public String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;
    */
}
