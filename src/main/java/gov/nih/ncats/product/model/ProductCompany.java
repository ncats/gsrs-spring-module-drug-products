package gov.nih.ncats.product.model;

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
@Table(name="SRSCID_PRODUCT_COMPANY")
public class ProductCompany extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name="prodCompanySeq", sequenceName="SRSCID_SQ_PRODUCT_COMPANY_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodCompanySeq")
    @Column(name="PRODUCT_COMPANY_ID")
    public Long id;

    @Column(name="COMPANY_NAME")
    public String companyName;

    @Column(name="COMPANY_ADDRESS")
    public String companyAddress;

    @Column(name="COMPANY_CITY")
    public String companyCity;

    @Column(name="COMPANY_STATE")
    public String companyState;

    @Column(name="COMPANY_ZIP")
    public String companyZip;

    @Column(name="COMPANY_COUNTRY")
    public String companyCountry;

    @JoinColumn(name="COMPANY_ROLE")
    public String companyRole;

    @Column(name="COMPANY_CODE")
    public String companyCode;

    @Column(name="COMPANY_CODE_TYPE")
    public String companyCodeType;

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

}
