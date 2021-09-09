package gov.hhs.gsrs.products.productall.models;

import gsrs.GsrsEntityProcessorListener;
import gsrs.model.AbstractGsrsEntity;
import gsrs.model.AbstractGsrsManualDirtyEntity;
import ix.core.models.Indexable;
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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_ALL_TWO_MV")
public class ProductMainAll extends AbstractGsrsEntity {

    @Id
    @Column(name="PRODUCTID")
    public String productId;

    @Indexable(facet=true, name= "Product ID", sortable = true)
    @Column(name="PRODUCTNDC")
    public String productNDC;

    @Indexable(facet=true, name= "Nonproprietary Name")
    @Column(name="nonproprietaryname")
    public String nonProprietaryName;

    @Indexable(facet=true, name="Product Type")
    @Column(name="PRODUCTTYPE")
    public String productType;

    @Indexable(facet=true, name="Status", sortable = true)
    @Column(name="STATUS")
    public String status;

    @Indexable(facet=true, name="Marketing Category Name")
    @Column(name="MARKETINGCATEGORYNAME")
    public String marketingCategoryName;

    @Indexable(facet=true, name="Is Listed")
    @Column(name="ISLISTED")
    public String isListed;

    @Indexable(facet=true, name="Route of Admin")
    @Column(name="ROUTENAME")
    public String routeName;

    @Indexable(facet=true, name="Application Type Number")
    @Column(name="APPLICATIONNUMBER")
    public String appTypeNumber;

    @Indexable(facet=true, name="Application Type")
    @Column(name="APPTYPE")
    public String appType;

    @Indexable(facet=true, name="Application Number")
    @Column(name="APPNUMBER")
    public String appNumber;

    @Column(name="COUNTRY")
    public String country;

   // @Indexable(facet=true, name="Country")
    @Column(name="COUNTRYWITHOUTCODE")
    public String countryWithoutCode;

    @Column(name="LABELERNDC")
    public String labelerNDC;

    @Indexable(facet=true, name="Unit Presentation")
    @Column(name="UNITPRESENTATION")
    public String unitPresentation;

    @Indexable(facet=true, name="Source")
    @Column(name="SOURCE")
    public String source;

    @Indexable(facet=true, name="Source Type")
    @Column(name="SOURCE_TYPE")
    public String sourceType;

    @Indexable(facet=true, name="From Table")
    @Column(name="FROMTABLE")
    public String fromtable;

    @Indexable(facet=true, name="Provenance")
    @Column(name="PROVENANCE")
    public String provenance;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Indexable( name = "Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @Indexable( name = "Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

    @JsonIgnore
    @Indexable(facet=true, name="Deprecated")
    public String getDeprecated(){
        return "Not Deprecated";
    }

    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ProductNameAll> productNameAllList = new ArrayList<ProductNameAll>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ProductCompanyAll> productCompanyAllList = new ArrayList<ProductCompanyAll>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PRODUCTID", referencedColumnName = "PRODUCTID")
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    public List<ProductIngredientAll> productIngredientAllList = new ArrayList<ProductIngredientAll>();

    public ProductMainAll () {}
}
