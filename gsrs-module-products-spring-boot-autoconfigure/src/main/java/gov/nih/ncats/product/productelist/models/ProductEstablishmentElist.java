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
@Table(name="SRSCID_ELIST_ESTABLISHMENT_V", schema = "srscid")
public class ProductEstablishmentElist extends AbstractGsrsEntity {

    @Id
    @Column(name="ESTABLISHMENT_ID")
    public String id;

    @Column(name="PRODUCTID")
    public String productId;

    @Column(name="FEI_NUMBER")
    public String feiNumber;

    @Column(name="DUNS_NUMBER")
    public String dunsNumber;

    @Column(name="NDC_LABELER_CODE")
    public String ndcLabelerCode;

    @Column(name="FIRM_NAME")
    public String firmName;

    @Column(name="ADDRESS")
    public String address;

    @Column(name="CITY")
    public String city;

    @Column(name="STATE")
    public String state;

    @Column(name="ZIP")
    public String zip;

    @Column(name="COUNTRY")
    public String country;

    @Column(name="COUNTRY_CODE")
    public String countryCode;

    /*
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name="ELIST_ESTABLISHMENT_TYPE")
    */

    /*
    @JoinTable(
            name = "ELIST_ESTABLISHMENT_TYPE",
            joinColumns = {@JoinColumn(name = "ESTABLISHMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "BUSINESS_OPERATION_ID")}
    )
     */
  //  public List<ProductBusinessOperationElist> businessOperationElistArrayList = new ArrayList<>();
    /*

    */

    public ProductEstablishmentElist () {}
}
