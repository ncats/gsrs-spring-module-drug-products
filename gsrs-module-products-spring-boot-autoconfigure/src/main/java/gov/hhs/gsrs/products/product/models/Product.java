package gov.hhs.gsrs.products.product.models;

import gsrs.BackupEntityProcessorListener;
import gsrs.GsrsEntityProcessorListener;
import gsrs.indexer.IndexerEntityListener;
import ix.core.models.Backup;
import ix.core.models.IndexableRoot;
import ix.core.models.Indexable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Column(name="PHARMACEUTICAL_DOSAGE_FORM", length=500)
    public String pharmaceuticalDosageForm;

    @Indexable(suggest = true, facet=true, name="Route of Administration")
    @Column(name="ROUTE_OF_ADMINISTRATION")
    public String routeAdmin;

    @Indexable(facet=true, name="Unit Presentation")
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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="EFFECTIVE_DATE")
    public Date effectiveDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="END_DATE")
    public Date endDate;

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated(){
        return "Not Deprecated";
    }

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
    public List<ProductManufactureItem> productManufactureItems = new ArrayList<ProductManufactureItem>();

    public void setProductManufactureItems(List<ProductManufactureItem> productManufactureItems) {
        this.productManufactureItems = productManufactureItems;
        if (productManufactureItems != null) {
            for (ProductManufactureItem prod : productManufactureItems)
            {
                prod.setOwner(this);
            }
        }
    }

}
