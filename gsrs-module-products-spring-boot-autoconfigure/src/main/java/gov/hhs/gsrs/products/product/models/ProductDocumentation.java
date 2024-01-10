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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
@Table(name = "SRSCID_PRODUCT_DOCUMENTATION")
public class ProductDocumentation extends ProductCommonData {

    @Id
    @SequenceGenerator(name = "prodDocSeq", sequenceName = "SRSCID_SQ_PRODUCT_DOCUMENT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodDocSeq")
    @Column(name = "PRODUCT_DOCUMENTATION_ID")
    public Long id;

    @Column(name = "DOCUMENT_ID", length=500)
    public String documentId;

    @Column(name = "DOCUMENT_TYPE", length=500)
    public String documentType;

    @Column(name = "SET_ID_VERSION")
    public String setIdVersion;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @Column(name="EFFECTIVE_TIME")
    public Date effectiveTime;

    @Column(name = "JURISDICTIONS", length=500)
    public String jurisdictions;

    // Set Parent Class
    @Indexable(indexed = false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PRODUCT_PROVENANCE_ID", referencedColumnName = "PRODUCT_PROVENANCE_ID")
    public ProductProvenance owner;

    public void setOwner(ProductProvenance product) {
        this.owner = product;
    }

}
