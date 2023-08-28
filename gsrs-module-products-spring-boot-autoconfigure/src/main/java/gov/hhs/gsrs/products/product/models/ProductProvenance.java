package gov.hhs.gsrs.products.product.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name = "SRSCID_PRODUCT_PROVENANCE")
public class ProductProvenance extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodProvSeq", sequenceName = "SRSCID_SQ_PRODUCT_PROV_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodProvSeq")
    @Column(name = "PRODUCT_PROVENANCE_ID")
    public Long id;

    @Column(name = "PROVENANCE")
    public String provenance;

    @Column(name = "PRODUCT_STATUS")
    public String productStatus;

    @Column(name = "PRODUCT_TYPE", length=500)
    public String productType;

    @Column(name = "APPLICATION_TYPE")
    public String applicationType;

    @Column(name = "APPLICATION_NUMBER")
    public String applicationNumber;

    @Column(name = "PUBLIC_DOMAIN")
    public String publicDomain;

    @Column(name = "IS_LISTED")
    public String isListed;

    @Column(name="JURISDICTIONS", length=500)
    public String jurisdictions;

    @Column(name="MARKETING_CATEGORY_CODE")
    public String marketingCategoryCode;

    @Column(name="MARKETING_CATEGORY_NAME", length=500)
    public String marketingCategoryName;

    @Column(name="CONTROL_SUBSTANCE_CODE")
    public String controlSubstanceCode;

    @Column(name="CONTROL_SUBSTANCE_CLASS")
    public String controlSubstanceClass;

    @Column(name="CONTROL_SUBSTANCE_SOURCE", length=500)
    public String controlSubstanceSource;

    @Column(name = "PRODUCT_URL", length=500)
    public String productUrl;

    // Set Parent Class
    @Indexable(indexed = false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    public Product owner;

    public void setOwner(Product product) {
        this.owner = product;
    }

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
    public List<ProductDocumentation> productDocumentations = new ArrayList<ProductDocumentation>();

    public void setProductDocumentations(List<ProductDocumentation> productDocumentations) {
        this.productDocumentations = productDocumentations;
        if(productDocumentations != null) {
            for (ProductDocumentation prod : productDocumentations)
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
}
