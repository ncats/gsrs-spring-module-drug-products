package gov.nih.ncats.product.product.models;

import lombok.Data;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT", schema = "srscid")
public class Product extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodSeq", sequenceName="SRSCID.SRSCID_SQ_PRODUCT_TWO_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodSeq")
    @Column(name="PRODUCT_ID")
    public Long id;

    @Column(name="SOURCE")
    public String source;

    @Column(name="PUBLIC_DOMAIN")
    public String publicDomain;

    @Column(name="APP_TYPE")
    public String appType;

    @Column(name="APP_NUMBER")
    public String appNumber;

    @Column(name="PRODUCT_TYPE")
    public String productType;

    @Column(name="SOURCE_TYPE")
    public String sourceType;

    @Column(name="UNIT_PRESENTATION")
    public String unitPresentation;

    @Column(name="ROUTE_OF_ADMINISTRATION")
    public String routeAdmin;

    @Column(name="STATUS")
    public String status;

    @Column(name="NONPROPRIETARY_NAME")
    public String nonProprietaryName;

    @Column(name="PROPRIETARY_NAME")
    public String proprietaryName;

    @Column(name="PHARMACEDICAL_DOSAGE_FORM")
    public String pharmacedicalDosageForm;

    @Column(name="COMPOSE_PRODUCT_NAME")
    public String composeProductName;

    @Column(name="RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name="STRENGTH_CHARACTERISTIC")
    public String strengthCharacteristic;

    @Column(name="COUNTRY_CODE")
    public String countryCode;

    @Column(name="LANGUAGE")
    public String language;

    @Column(name="PROVENANCE")
    public String provenance;

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

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ProductName> productNameList = new ArrayList<ProductName>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductCode> productCodeList = new ArrayList<ProductCode>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductCompany> productCompanyList = new ArrayList<ProductCompany>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductComponent> productComponentList = new ArrayList<ProductComponent>();

    public Long getId() {
        return id;
    }
}
