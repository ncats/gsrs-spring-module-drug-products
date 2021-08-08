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
@IdClass(ProductRouteCompositePrimaryKeyId.class)
@Table(name="ELIST_PROD_ADMIN_ROUTE_MV", schema = "srscid")
public class ProductRouteElist extends AbstractGsrsEntity {

    @Id
  //  @Column(name="PRODUCTID")
    public String productId;

    @Id
    public String routeCode;

    @Column(name="ROUTENAME")
    public String routeName;

    public ProductRouteElist () {}
}
