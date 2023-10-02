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
@Table(name="SRSCID_PRODUCT_LOT")
public class ProductLot extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodLotSeq", sequenceName="SRSCID_SQ_PRODUCT_LOT_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodLotSeq")
    @Column(name="PRODUCT_LOT_ID")
    public Long id;

    @Column(name="LOT_NO")
    public String lotNo;

    @Column(name="LOT_SIZE")
    public String lotSize;

    @Column(name="LOT_TYPE")
    public String lotType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="EXPIRY_DATE")
    public Date expiryDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="MANUFACTURE_DATE")
    public Date manufactureDate;

    // Set Parent Class
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_COMPONENT_ID", referencedColumnName="PRODUCT_COMPONENT_ID")
    public ProductManufactureItem owner;

    public void setOwner(ProductManufactureItem productComponent) {
        this.owner = productComponent;
    }

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductIngredient> productIngredients = new ArrayList<ProductIngredient>();

    public void setProductIngredientList(List<ProductIngredient> productIngredients) {
        this.productIngredients = productIngredients;
        if (productIngredients != null) {
            for (ProductIngredient prod : productIngredients)
            {
                prod.setOwner(this);
            }
        }
    }
}
