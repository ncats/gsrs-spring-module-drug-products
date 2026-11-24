package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_PRODUCT_APPLICATION")
public class ProductApplication extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodAppSeq", sequenceName="SRSCID_SQ_PRODUCT_APPL_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodAppSeq")
    @Column(name="PRODUCT_APPLICATION_ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Product Application Type", sortable = true)
    @Column(name="APPLICATION_TYPE")
    public String applicationType;

    @Indexable(suggest = true, facet=true, name= "Product Application Number", sortable = true)
    @Column(name="APPLICATION_NUMBER")
    public String applicationNumber;

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
