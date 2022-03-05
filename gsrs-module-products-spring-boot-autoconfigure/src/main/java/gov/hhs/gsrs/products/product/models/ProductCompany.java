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
@Table(name="SRSCID_PRODUCT_COMPANY")
public class ProductCompany extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodCompanySeq", sequenceName="SRSCID_SQ_PRODUCT_COMPANY_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodCompanySeq")
    @Column(name="PRODUCT_COMPANY_ID")
    public Long id;

    @Column(name="COMPANY_NAME")
    public String companyName;

    @Column(name="COMPANY_ADDRESS")
    public String companyAddress;

    @Column(name="COMPANY_CITY")
    public String companyCity;

    @Column(name="COMPANY_STATE")
    public String companyState;

    @Column(name="COMPANY_ZIP")
    public String companyZip;

    @Column(name="COMPANY_COUNTRY")
    public String companyCountry;

    @JoinColumn(name="COMPANY_ROLE")
    public String companyRole;

    @Column(name="COMPANY_CODE")
    public String companyCode;

    @Column(name="COMPANY_CODE_TYPE")
    public String companyCodeType;

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID")
    public Product owner;

    public void setOwner(Product product) {
        this.owner = product;
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
