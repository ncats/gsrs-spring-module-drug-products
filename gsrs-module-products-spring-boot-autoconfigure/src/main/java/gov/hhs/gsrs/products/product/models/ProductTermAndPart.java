package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@SingleParent
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

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_NAME_ID",
referencedColumnName = "PRODUCT_NAME_ID")
    public ProductName owner;

    public void setOwner(ProductName productName) {
        this.owner = productName;
    }

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
