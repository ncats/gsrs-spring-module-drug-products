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
@Table(name = "SRSCID_PRODUCT_INDICATION")
public class ProductIndication extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodIndSeq", sequenceName = "SRSCID_SQ_PRODUCT_INDICAT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodIndSeq")
    @Column(name = "PRODUCT_INDICATION_ID")
    public Long id;

    @Column(name = "INDICATION", length=4000)
    public String indication;

    @Column(name = "INDICATION_TEXT", length=4000)
    public String indicationText;

    @Column(name = "INDICATION_CODE")
    public String indicationCode;

    @Column(name = "INDICATION_CODE_TYPE")
    public String indicationCodeType;

    @Column(name = "INDICATION_GROUP")
    public String indicationGroup;

    @Column(name = "INDICATION_SOURCE", length=500)
    public String indicationSource;

    @Column(name = "INDICATION_SOURCE_TYPE")
    public String indicationSourceType;

    @Column(name = "INDICATION_SOURCE_URL", length=500)
    public String indicationSourceUrl;

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
