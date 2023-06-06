package gov.hhs.gsrs.products.product.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.SingleParent;
import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@SingleParent
@Data
@Entity
@Table(name = "SRSCID_PRODUCT_CODE")
public class ProductCode extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodCodeSeq", sequenceName = "SRSCID_SQ_PRODUCT_CODE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodCodeSeq")
    @Column(name = "PRODUCT_CODE_ID")
    public Long id;

    @Column(name = "PRODUCT_CODE")
    public String productCode;

    @Column(name = "PRODUCT_CODE_TYPE")
    public String productCodeType;

    // Set Parent Class
    /*
    @Indexable(indexed = false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    public Product owner;

    public void setOwner(Product product) {
        this.owner = product;
    }
     */

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
