package gov.nih.ncats.product.productelist.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
import ix.core.models.IxModel;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
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

@Data
@Entity
@Table(name="ELIST_PROD_CHARACT_MV", schema = "srscid")
public class ProductCharacterElist extends AbstractGsrsEntity {

    @Id
    @Column(name="PRODUCTID")
    public String productId;

    @Column(name="FLAVORNAME")
    public String flavorName;

    @Column(name="COLORNAME")
    public String colorName;

    @Column(name="SHAPENAME")
    public String shapeName;

    @Column(name="NUMBEROFFRAGMENTS")
    public String numberOfFragments;

    @Column(name="SIZE_MM")
    public String sizeMm;

    @Column(name="IMPRINTTEXT")
    public String imprintText;

    public ProductCharacterElist () {}
}
