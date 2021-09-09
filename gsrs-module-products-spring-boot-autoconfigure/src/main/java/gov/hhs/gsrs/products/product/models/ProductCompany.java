package gov.hhs.gsrs.products.product.models;

import lombok.Data;

import javax.persistence.*;

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
