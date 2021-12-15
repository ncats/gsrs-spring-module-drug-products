package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="EXPIRY_DATE")
    public Date expiryDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="MANUFACTURE_DATE")
    public Date manufactureDate;

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

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_COMPONENT_ID")
    public ProductComponent owner;

    public void setOwner(ProductComponent productComponent) {
        this.owner = productComponent;
    }

   // @JoinColumn(name = "PRODUCT_LOT_ID", referencedColumnName = "PRODUCT_LOT_ID")
   // @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
   // public List<ProductIngredient> productIngredientList = new ArrayList<ProductIngredient>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductIngredient> productIngredientList = new ArrayList<ProductIngredient>();

    public void setProductIngredientList(List<ProductIngredient> productIngredientList) {
        this.productIngredientList = productIngredientList;
        if(productIngredientList !=null) {
            for (ProductIngredient prod : productIngredientList)
            {
                prod.setOwner(this);
            }
        }
    }
}
