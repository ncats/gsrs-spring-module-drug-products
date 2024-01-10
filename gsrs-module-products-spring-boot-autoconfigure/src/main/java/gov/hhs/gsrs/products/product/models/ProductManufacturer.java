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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "SRSCID_PRODUCT_MANUFACTURER")
public class ProductManufacturer extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodManuSeq", sequenceName = "SRSCID_SQ_PRODUCT_MANUFACT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodManuSeq")
    @Column(name = "PRODUCT_MANUFACTURER_ID")
    public Long id;

    @Indexable(suggest = true, facet=true, name= "Manufacturer Name", sortable = true)
    @Column(name = "MANUFACTURER_NAME", length=500)
    public String manufacturerName;

    @Indexable(suggest = true, facet=true, name= "Manufacturer Role", sortable = true)
    @Column(name = "MANUFACTURER_ROLE", length=500)
    public String manufacturerRole;

    @Column(name = "MANUFACTURER_CODE", length=500)
    public String manufacturerCode;

    @Column(name = "MANUFACTURER_CODE_TYPE", length=500)
    public String manufacturerCodeType;

    @Column(name = "MANUFACTURED_ITEM_CODE", length=500)
    public String manufacturedItemCode;

    @Column(name = "MANUFACTURED_ITEM_CODE_TYPE", length=500)
    public String manufacturedItemCodeType;

    // Set Parent Class
    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_COMPONENT_ID", referencedColumnName = "PRODUCT_COMPONENT_ID")
    public ProductManufactureItem owner;

    public void setOwner(ProductManufactureItem product) {
        this.owner = product;
    }

}
