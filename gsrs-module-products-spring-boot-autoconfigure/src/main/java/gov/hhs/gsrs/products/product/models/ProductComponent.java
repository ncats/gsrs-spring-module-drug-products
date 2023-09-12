package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_PRODUCT_COMPONENT")
public class ProductComponent extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodCompSeq", sequenceName="SRSCID_SQ_PRODUCT_COMPONENT_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodCompSeq")
    @Column(name="PRODUCT_COMPONENT_ID")
    public Long id;

    @Column(name="CHAR_SIZE")
    public String charSize;

    @Column(name="CHAR_IMPRINTTEXT")
    public String charImprintText;

    @Column(name="CHAR_COLOR")
    public String charColor;

    @Column(name="CHAR_FLAVOR")
    public String charFlavor;

    @Column(name="CHAR_SHAPE")
    public String charShape;

    @Column(name="CHAR_NUM_FRAGMENTS")
    public String charNumFragments;

    @Column(name="DOSAGE_FORM")
    public String dosageForm;

    @Column(name="DOSAGE_FORM_CODE")
    public String dosageFormCode;

    @Column(name="DOSAGE_FORM_CODE_TYPE")
    public String dosageFormCodeType;

    @Column(name="DOSAGE_FORM_NOTE")
    public String dosageFormNote;

    @Column(name="COMPOSITION_NOTE")
    public String compositionNote;

    @Column(name="ROUTE_OF_ADMINISTRATION")
    public String routeOfAdministration;

    @Column(name="AMOUNT")
    public Double amount;

    @Column(name="UNIT")
    public String unit;

    @Column(name="PROVENANCE_MANUFACTURE_ITEM_ID", length=500)
    public String provenanceManufactureItemId;

    // Set Parent class
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    public Product owner;

    public void setOwner(Product product) {
        this.owner = product;
    }

    // Set Child class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductManufacturer> productManufacturers = new ArrayList<ProductManufacturer>();

    public void setProductManufacturers(List<ProductManufacturer> productManufacturers) {
        this.productManufacturers = productManufacturers;
        if (productManufacturers != null) {
            for (ProductManufacturer prod : productManufacturers)
            {
                prod.setOwner(this);
            }
        }
    }

    // Set Child class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductLot> productLots = new ArrayList<ProductLot>();

    public void setProductLots(List<ProductLot> productLots) {
        this.productLots = productLots;
        if (productLots != null) {
            for (ProductLot prod : productLots)
            {
                prod.setOwner(this);
            }
        }
    }

}
