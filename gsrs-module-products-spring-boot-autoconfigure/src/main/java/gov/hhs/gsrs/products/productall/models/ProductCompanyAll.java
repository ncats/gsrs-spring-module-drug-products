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
@Table(name="SRSCID_PRODUCT_CMPY_ALL_TWO_MV")
public class ProductCompanyAll extends AbstractGsrsEntity {

    @Id
    @Column(name="ID")
    public String id;

    @Column(name="PRODUCTID")
    public String productId;

    @Indexable(facet=true, name= "Labeler Name", sortable=true)
    @Column(name="LABELERNAME")
    public String labelerName;

    @Indexable(facet=true, name= "Labeler DUNS Number")
    @Column(name="LABELERDUNS")
    public String labelerDuns;

    @Indexable(facet=true, name= "FEI Number")
    @Column(name="FEINUMBER")
    public String feiNumber;

    @Column(name="NDC_LABELER_CODE")
    public String ndcLabelerCode;

    @Column(name="ADDRESS")
    public String address;

    @Indexable(facet=true, name= "City")
    @Column(name="CITY")
    public String city;

    @Indexable(facet=true, name= "State")
    @Column(name="STATE")
    public String state;

    @Column(name="ZIP")
    public String zip;

    @Column(name="COUNTRY")
    public String country;

    @Indexable(facet=true, name="Company Country", sortable = true)
    @Column(name="COUNTRYWITHOUTCODE")
    public String countryWithoutCode;

    /*
    @Indexable(facet=true, name= "Registrant Name")
    @Column(name="REGISTRANTNAME")
    public String registrantName;

    @Indexable(facet=true, name= "Registrant DUNS")
    @Column(name="REGISTRANTDUNS")
    public String registrantDuns;
    */

    public ProductCompanyAll () {}
}
