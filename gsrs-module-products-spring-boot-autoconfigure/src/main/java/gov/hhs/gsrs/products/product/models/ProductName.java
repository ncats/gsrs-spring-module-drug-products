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
@Table(name="SRSCID_PRODUCT_NAME")
public class ProductName extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodNameSeq", sequenceName="SRSCID_SQ_PRODUCT_NAME_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodNameSeq")
    @Column(name="PRODUCT_NAME_ID")
    public Long id;

    @Column(name="PRODUCT_NAME")
    public String productName;

    @Column(name="PRODUCT_NAME_TYPE")
    public String productNameType;

    // Set Parent Class
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

    // Set Child Class
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    public List<ProductTermAndPart> productTermAndTermPartList = new ArrayList<ProductTermAndPart>();

    public void setProductTermAndTermPartList(List<ProductTermAndPart> productTermAndTermPartList) {
        this.productTermAndTermPartList = productTermAndTermPartList;
        if(productTermAndTermPartList !=null) {
            for (ProductTermAndPart prod : productTermAndTermPartList)
            {
                prod.setOwner(this);
            }
        }
    }

}
