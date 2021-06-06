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
@Table(name="ELIST_PROD_ACTIVE_INGRED_MV", schema = "srscid")
public class ProductActiveElist extends AbstractGsrsEntity {

    @Id
    @Column(name="PRODUCTID")
    public String productId;

    @Column(name="SUBSTANCEUNII")
    public String unii;

    @Column(name="SUBSTANCENAME")
    public String name;

    @Column(name="ACTIVEMOIETY_1_NAME")
    public String activeMoietyName;

    @Column(name="STRENGTHNUMBER")
    public String strengthNumber;

    @Column(name="STRENGTHNUMERATORUNIT")
    public String strengthNumeratorUnit;

    @Transient
    public String substanceId;

    @Transient
    public String structure;

    @Transient
    public String substanceType;

    public ProductActiveElist () {}
}
