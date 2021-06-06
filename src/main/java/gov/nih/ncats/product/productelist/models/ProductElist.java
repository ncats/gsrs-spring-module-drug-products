package gov.nih.ncats.product.productelist.models;

import gov.nih.ncats.product.productall.models.ProductNameAll;
import gov.nih.ncats.product.productelist.models.*;

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
@Table(name="ELIST_PRODUCT_MV", schema = "srscid")
public class ProductElist extends AbstractGsrsEntity {

    @Id
    @Column(name="PRODUCTID")
    public String productId;

    @Column(name="PRODUCTNDC")
    public String productNDC;

    @Column(name="PROPRIETARYNAME")
    public String productName;

    @Column(name="NONPROPRIETARYNAME")
    public String nonProprietaryName;

    @Column(name="LABELERNAME")
    public String labelerName;

    @Column(name="LABELERDUNS")
    public String labelerDuns;

    @Column(name="REGISTRANTDUNS")
    public String registrantDuns;

    @Column(name="APPLICATIONNUMBER")
    public String applicationNumber;

    @Column(name="MARKETINGCATEGORYCODE")
    public String marketingCategoryCode;

    @Column(name="MARKETINGCATEGORYNAME")
    public String marketingCategoryName;

    @Column(name="DOSAGEFORMNAME")
    public String dosageFormName;

    @Column(name="PRODUCTTYPENAME")
    public String productTypeName;

    @Column(name="PROPRIETARYNAMESUFFIX")
    public String proprietaryNameSuffix;

    @Column(name="MARKETINGSTATUS")
    public String marketingStatus;

    @Column(name="STARTMARKETINGDATE")
    public String startMarketingDate;

    @Column(name="ENDMARKETINGDATE")
    public String endMarketingDate;

    @Column(name="IS_LISTED")
    public String isListed;

    @Column(name="DOCUMENTID")
    public String documentId;

    @Transient
    public String appType;

    @Transient
    public String appNumber;

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductActiveElist> prodActiveElistList = new ArrayList<ProductActiveElist>();

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductInactiveElist> prodInactiveElistList = new ArrayList<ProductInactiveElist>();

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductRouteElist> prodRouteElistList = new ArrayList<ProductRouteElist>();

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductEstablishmentElist> prodEstablishmentElistList = new ArrayList<ProductEstablishmentElist>();

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToOne(cascade = CascadeType.ALL)
    public ProductCharacterElist prodCharElist;

    public ProductElist () {}
}
