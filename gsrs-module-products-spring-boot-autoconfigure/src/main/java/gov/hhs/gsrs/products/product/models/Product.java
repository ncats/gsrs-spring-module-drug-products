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

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

import java.util.Date;
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

    @Column(name="PHARMACEDICAL_DOSAGE_FORM", length=500)
    public String pharmacedicalDosageForm;

    @Column(name="ROUTE_OF_ADMINISTRATION")
    public String routeAdmin;

    @Column(name="UNIT_PRESENTATION")
    public String unitPresentation;

    @Column(name="COUNTRY_CODE", length=500)
    public String countryCode;

    @Column(name="LANGUAGE")
    public String language;

    @Column(name="SHELF_LIFE")
    public String shelfLife;

    @Column(name="STORAGE_CONDITIONS")
    public String storageConditions;

    @Column(name="NUMBER_OF_MANU_ITEM")
    public String numberOfManufactureItem;

    @Column(name="MANUFACTURER_NAME", length=500)
    public String manufacturerName;

    @Column(name="MANUFACTURER_CODE")
    public String manufacturerCode;

    @Column(name="MANUFACTURER_CODE_TYPE")
    public String manufacturerCodeType;

    /*
    @Column(name="PUBLIC_DOMAIN")
    public String publicDomain;

    @Column(name="APP_TYPE")
    public String appType;

    @Column(name="APP_NUMBER")
    public String appNumber;

    @Column(name="PRODUCT_TYPE")
    public String productType;

    @Column(name="STATUS")
    public String status;

    @Column(name="NONPROPRIETARY_NAME")
    public String nonProprietaryName;

    @Column(name="PROPRIETARY_NAME")
    public String proprietaryName;

    @Column(name="COMPOSE_PRODUCT_NAME")
    public String composeProductName;

    @Column(name="SOURCE")
    public String source;

    @Column(name="SOURCE_TYPE")
    public String sourceType;

    @Column(name="RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name="STRENGTH_CHARACTERISTIC")
    public String strengthCharacteristic;

    @Column(name="PROVENANCE")
    public String provenance;

    @Column(name="MARKETING_CATEGORY_CODE")
    public String marketingCategoryCode;

    @Column(name="MARKETING_CATEGORY_NAME")
    public String marketingCategoryName;

    @Column(name="DEASCHEDULE")
    public String deaschedule;
    */

    // get Id
    public Long getId() {
        return id;
    }

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductProvenance> productProvenances = new ArrayList<ProductProvenance>();

    public void setProductProvenances(List<ProductProvenance> productProvenances) {
        this.productProvenances = productProvenances;
        if(productProvenances != null) {
            for (ProductProvenance prod : productProvenances)
            {
                prod.setOwner(this);
            }
        }
    }

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductComponent> productManufactureItems = new ArrayList<ProductComponent>();

    public void setProductManufactureItems(List<ProductComponent> productManufactureItems) {
        this.productManufactureItems = productManufactureItems;
        if (productManufactureItems != null) {
            for (ProductComponent prod : productManufactureItems)
            {
                prod.setOwner(this);
            }
        }
    }

    /*
    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductName> productNames = new ArrayList<ProductName>();

    public void setProductNames(List<ProductName> productNames) {
        this.productNames = productNames;
        if(productNames !=null) {
            for (ProductName prod : productNames)
            {
                prod.setOwner(this);
            }
        }
    }

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCode> productCodes = new ArrayList<ProductCode>();

    public void setProductCodes(List<ProductCode> productCodes) {
        this.productCodes = productCodes;
        if(productCodes != null) {
            for (ProductCode prod : productCodes)
            {
                prod.setOwner(this);
            }
        }
    }
    */


    /*
    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCompany> productCompanies = new ArrayList<ProductCompany>();

    public void setProductCompanyList(List<ProductCompany> productCompanies) {
        this.productCompanies = productCompanies;
        if(productCompanies != null) {
            for (ProductCompany prod : productCompanies)
            {
                prod.setOwner(this);
            }
        }
    }

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductIndication> productIndications = new ArrayList<ProductIndication>();

    public void setProductIndications(List<ProductIndication> productIndications) {
        this.productIndications = productIndications;
        if (productIndications != null) {
            for (ProductIndication prod : productIndications)
            {
                prod.setOwner(this);
            }
        }
    }
    */
}
