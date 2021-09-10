package gov.hhs.gsrs.products.productelist.models;

import gov.hhs.gsrs.products.productall.models.ProductNameAll;
import gov.hhs.gsrs.products.productelist.models.*;

import gsrs.BackupEntityProcessorListener;
import gsrs.GsrsEntityProcessorListener;
import gsrs.indexer.IndexerEntityListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Backup;
import ix.core.models.Indexable;
import ix.core.models.IndexableRoot;
import ix.core.models.IxModel;
import ix.core.search.text.TextIndexerEntityListener;
import ix.ginas.models.serialization.GsrsDateDeserializer;
import ix.ginas.models.serialization.GsrsDateSerializer;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@EntityListeners({AuditingEntityListener.class, GsrsEntityProcessorListener.class, IndexerEntityListener.class, BackupEntityProcessorListener.class})
@Backup
@IndexableRoot
@Data
@Entity
@Table(name="ELIST_PRODUCT_MV")
public class ProductElist implements Serializable {

    @Id
    @Column(name="PRODUCTID")
    public String productId;

    @Column(name="IS_LISTED")
    public String isListed;

    @Column(name="STARTMARKETINGDATE")
    public String startmarketingdate;

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

    @Column(name="ENDMARKETINGDATE")
    public String endMarketingDate;

    @Column(name="DOCUMENTID")
    public String documentId;

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ProductActiveElist> prodActiveElistList = new ArrayList<ProductActiveElist>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ProductInactiveElist> prodInactiveElistList = new ArrayList<ProductInactiveElist>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ProductRouteElist> prodRouteElistList = new ArrayList<ProductRouteElist>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ProductEstablishmentElist> prodEstablishmentElistList = new ArrayList<ProductEstablishmentElist>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public ProductCharacterElist prodCharElist;

    public ProductElist () {}
}
