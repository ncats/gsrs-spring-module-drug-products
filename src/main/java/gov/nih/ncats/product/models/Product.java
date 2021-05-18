package gov.nih.ncats.product.models;

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

import javax.persistence.EntityListeners;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name="SRSCID_PRODUCT", schema = "srscid")
public class Product extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name="prodSeq", sequenceName="SRSCID.SRSCID_SQ_PRODUCT_TWO_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodSeq")
    @Column(name="PRODUCT_ID")
    public Long id;

    @Column(name="SOURCE")
    public String source;

    @Column(name="PUBLIC_DOMAIN")
    public String publicDomain;

    @Column(name="APP_TYPE")
    public String appType;

    @Column(name="APP_NUMBER")
    public String appNumber;

    @Column(name="PRODUCT_TYPE")
    public String productType;

    @Column(name="SOURCE_TYPE")
    public String sourceType;

    @Column(name="UNIT_PRESENTATION")
    public String unitPresentation;

    @Column(name="ROUTE_OF_ADMINISTRATION")
    public String routeAdmin;

    @Column(name="STATUS")
    public String status;

    @Column(name="NONPROPRIETARY_NAME")
    public String nonProprietaryName;

    @Column(name="PROPRIETARY_NAME")
    public String proprietaryName;

    @Column(name="PHARMACEDICAL_DOSAGE_FORM")
    public String pharmacedicalDosageForm;

    @Column(name="COMPOSE_PRODUCT_NAME")
    public String composeProductName;

    @Column(name="RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name="STRENGTH_CHARACTERISTIC")
    public String strengthCharacteristic;

    @Column(name="COUNTRY_CODE")
    public String countryCode;

    @Column(name="LANGUAGE")
    public String language;

    @Column(name="PROVENANCE")
    public String provenance;

  //  @Version
  //  public Long internalVersion;

    @Column(name = "CREATED_BY")
    public String createdBy;

    @Column(name = "MODIFIED_BY")
    public String modifiedBy;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<ProductName> productNameList = new ArrayList<ProductName>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductCode> productCodeList = new ArrayList<ProductCode>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductCompany> productCompanyList = new ArrayList<ProductCompany>();

    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductComponent> productComponentList = new ArrayList<ProductComponent>();

    public Long getId() {
        return id;
    }
}
