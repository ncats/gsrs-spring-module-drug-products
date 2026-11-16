package gov.hhs.gsrs.products.product.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@SingleParent
@Data
@Entity
@Table(name = "SRSCID_PRODUCT_CODE")
public class ProductCode extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodCodeSeq", sequenceName = "SRSCID_SQ_PRODUCT_CODE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodCodeSeq")
    @Column(name = "PRODUCT_CODE_ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Product ID", sortable = true)
    @Column(name = "PRODUCT_CODE")
    public String productCode;

    @Indexable(suggest = true, facet=true, name= "Product Code Type", sortable = true)
    @Column(name = "PRODUCT_CODE_TYPE")
    public String productCodeType;

    // Set Parent Class
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_PROVENANCE_ID", referencedColumnName = "PRODUCT_PROVENANCE_ID")
    public ProductProvenance owner;

    public void setOwner(ProductProvenance product) {
        this.owner = product;
    }

}
