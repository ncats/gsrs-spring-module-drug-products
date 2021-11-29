package gov.hhs.gsrs.products.product.models;

import gsrs.BackupEntityProcessorListener;
import gsrs.GsrsEntityProcessorListener;
import gsrs.indexer.IndexerEntityListener;
import ix.core.models.Backup;
import ix.core.models.IndexableRoot;
import ix.core.models.Indexable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@IndexableRoot
@Backup
@Data
@Entity
@Table(name="SRSCID_PRODUCT")
public class Product extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodSeq", sequenceName="SRSCID_SQ_PRODUCT_TWO_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodSeq")
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

    public Long getId() {
        return id;
    }

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
  //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
  //  public List<ApplicationProduct> applicationProductList = new ArrayList<>();
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductName> productNameList = new ArrayList<ProductName>();

    public void setProductNameList(List<ProductName> productNameList) {
        this.productNameList = productNameList;
        if(productNameList !=null) {
            for (ProductName prod : productNameList)
            {
                prod.setOwner(this);
            }
        }
    }

   // @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
  //  @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
   // public List<ProductName> productNameList = new ArrayList<ProductName>();

  //  @LazyCollection(LazyCollectionOption.FALSE)
  //  @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
 //   @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
 //   public List<ProductCode> productCodeList = new ArrayList<ProductCode>();


    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    //  public List<ApplicationProduct> applicationProductList = new ArrayList<>();
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCode> productCodeList = new ArrayList<ProductCode>();

    public void setProductCodeList(List<ProductCode> productCodeList) {
        this.productCodeList = productCodeList;
        if(productCodeList !=null) {
            for (ProductCode prod : productCodeList)
            {
                prod.setOwner(this);
            }
        }
    }

  //  @LazyCollection(LazyCollectionOption.FALSE)
  //  @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
  //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
  //  public List<ProductCompany> productCompanyList = new ArrayList<ProductCompany>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    //  public List<ApplicationProduct> applicationProductList = new ArrayList<>();
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCompany> productCompanyList = new ArrayList<ProductCompany>();

    public void setProductCompanyList(List<ProductCompany> productCompanyList) {
        this.productCompanyList = productCompanyList;
        if(productCompanyList !=null) {
            for (ProductCompany prod : productCompanyList)
            {
                prod.setOwner(this);
            }
        }
    }

   // @LazyCollection(LazyCollectionOption.FALSE)
  //  @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
  //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
  //  public List<ProductComponent> productComponentList = new ArrayList<ProductComponent>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    //  public List<ApplicationProduct> applicationProductList = new ArrayList<>();
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductComponent> productComponentList = new ArrayList<ProductComponent>();

    public void setProductComponentList(List<ProductComponent> productComponentList) {
        this.productComponentList = productComponentList;
        if(productComponentList !=null) {
            for (ProductComponent prod : productComponentList)
            {
                prod.setOwner(this);
            }
        }
    }

}
