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
    @JoinColumn(name="PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    public Product owner;

    public void setOwner(Product product) {
        this.owner = product;
    }

  //  @JoinColumn(name = "PRODUCT_NAME_ID", referencedColumnName = "PRODUCT_NAME_ID")
  //  @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
  //  public List<ProductTermAndPart> productTermAndTermPartList = new ArrayList<ProductTermAndPart>();

    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    //  @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
    //  public List<ApplicationProduct> applicationProductList = new ArrayList<>();
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
