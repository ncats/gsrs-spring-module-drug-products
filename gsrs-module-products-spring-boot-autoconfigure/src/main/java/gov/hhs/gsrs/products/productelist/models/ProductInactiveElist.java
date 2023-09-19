package gov.hhs.gsrs.products.productelist.models;

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
@IdClass(ProductInactiveCompositePrimaryKeyId.class)
@Table(name="ELIST_PROD_INACTIVE_INGRED_MV")
public class ProductInactiveElist extends AbstractGsrsEntity {

    @Id
    public String productId;

    @Id
    public String documentId;

    @Id
    public String name;

    @Column(name="STRENGTHNUMBER", length = 50)
    public String strengthNumber;

    @Column(name="STRENGTHDENOMINATORUNIT", length = 50)
    public String strengthDenominatorUnit;

    @Column(name="STRENGTHNUMERATORUNIT", length = 50)
    public String strengthNumeratorUnit;

    @Column(name="SUBSTANCEUNII", length = 100)
    public String unii;

    public ProductInactiveElist() {}
}
