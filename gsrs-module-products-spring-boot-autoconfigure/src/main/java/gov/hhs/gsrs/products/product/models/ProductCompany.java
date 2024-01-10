package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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

    @Indexable(suggest = true, facet=true, name= "Labeler Name", sortable=true)
    @Column(name="COMPANY_NAME", length=500)
    public String companyName;

    @Column(name="COMPANY_ADDRESS", length=1000)
    public String companyAddress;

    @Column(name="COMPANY_CITY")
    public String companyCity;

    @Indexable(suggest = true, facet=true, name= "Labeler State", sortable=true)
    @Column(name="COMPANY_STATE")
    public String companyState;

    @Column(name="COMPANY_ZIP")
    public String companyZip;

    @Indexable(suggest = true, facet=true, name="Company Country", sortable = true)
    @Column(name="COMPANY_COUNTRY")
    public String companyCountry;

    @Column(name="GPS_LATITUDE")
    public String companyGpsLatitude;

    @Column(name="GPS_LONGITUDE")
    public String companyGpsLongitude;

    @Column(name="GPS_ELEVATION")
    public String companyGpsElevation;

    @Indexable(suggest = true, facet=true, name="Company Role", sortable = true)
    @JoinColumn(name="COMPANY_ROLE")
    public String companyRole;

    @Indexable(suggest = true, facet=true, name= "Labeler Public Domain", sortable=true)
    @Column(name="PUBLIC_DOMAIN")
    public String companyPublicDomain;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="START_MARKETING_DATE")
    public Date startMarketingDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="END_MARKETING_DATE")
    public Date endMarketingDate;

    @Column(name="COMPANY_PRODUCT_ID", length=500)
    public String companyProductId;

    @Column(name="COMPANY_DOCUMENT_ID", length=500)
    public String companyDocumentId;

    @Column(name="PROVENANCE_DOCUMENT_ID", length=500)
    public String provenanceDocumentId;

    // Set Parent Class
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_PROVENANCE_ID", referencedColumnName="PRODUCT_PROVENANCE_ID")
    public ProductProvenance owner;

    public void setOwner(ProductProvenance product) {
        this.owner = product;
    }

    // Set Child class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCompanyCode> productCompanyCodes = new ArrayList<ProductCompanyCode>();

    public void setProductComponentList(List<ProductCompanyCode> productCompanyCodes) {
        this.productCompanyCodes = productCompanyCodes;
        if (productCompanyCodes != null) {
            for (ProductCompanyCode prod : productCompanyCodes)
            {
                prod.setOwner(this);
            }
        }
    }
}
