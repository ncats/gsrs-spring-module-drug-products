package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @Column(name="START_MARKETING_DATE")
    public String startMarketingDate;

    @Column(name="END_MARKETING_DATE")
    public String endMarketingDate;

    @Column(name="COMPANY_PRODUCT_ID")
    public String companyProductId;

    @Column(name="COMPANY_DOCUMENT_ID")
    public String companyDocumentId;

    @Column(name="PROVENANCE_DOCUMENT_ID")
    public String provenanceDocumentId;

    // Set Parent Class
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

    // Set Child class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductCompanyCode> productCompanyCodeList = new ArrayList<ProductCompanyCode>();

    public void setProductComponentList(List<ProductCompanyCode> productCompanyCodeList) {
        this.productCompanyCodeList = productCompanyCodeList;
        if(productCompanyCodeList !=null) {
            for (ProductCompanyCode prod : productCompanyCodeList)
            {
                prod.setOwner(this);
            }
        }
    }
}
