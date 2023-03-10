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

    @Column(name="AMOUNT")
    public Double amount;

    @Column(name="UNIT")
    public String unit;

    @Column(name="MANUFACTURE_CODE")
    public String manufactureCode;

    @Column(name="MANUFACTURE_CODE_TYPE")
    public String manufactureCodeType;

    @Column(name="DOSAGE_FORM_CODE")
    public String dosageFormCode;

    @Column(name="DOSAGE_FORM_CODE_TYPE")
    public String dosageFormCodeType;

    @Column(name="MANUFACTURE_ITEM_CODE")
    public String manufactureItemCode;

    @Column(name="MANUFACTURE_ITEM_CODE_TYPE")
    public String manufactureItemCodeType;

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

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductLot> productLotList = new ArrayList<ProductLot>();

    public void setProductLotList(List<ProductLot> productLotList) {
        this.productLotList = productLotList;
        if(productLotList !=null) {
            for (ProductLot prod : productLotList)
            {
                prod.setOwner(this);
            }
        }
    }

}
