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
@Table(name="SRSCID_PRODUCT_COMPONENT", schema = "srscid")
public class ProductComponent extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name="prodCompSeq", sequenceName="SRSCID.SRSCID_SQ_PRODUCT_COMPONENT_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodCompSeq")
    @Column(name="PRODUCT_COMPONENT_ID")
    public Long id;

    @Column(name="CHAR_SIZE")
    public String charSize;

    @Column(name="CHAR_IMPRINTTEXT")
    public String charImprintText;

    @Column(name="CHAR_COLOR")
    public String charColor;

    @Column(name="CHAR_FLAVOR")
    public String charFlavor;

    @Column(name="CHAR_SHAPE")
    public String charShape;

    @Column(name="CHAR_NUM_FRAGMENTS")
    public String charNumFragments;

    @Column(name="DOSAGE_FORM")
    public String dosageForm;

    @Column(name="AMOUNT")
    public Double amount;

    @Column(name="UNIT")
    public String unit;

    @Column(name="MANUFACTURE_CODE")
    public String manufactureCode;

    @Column(name="MANUFACTURE_CODE_TYPE")
    public String manufactureCodeType;


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

    @JoinColumn(name = "PRODUCT_COMPONENT_ID", referencedColumnName = "PRODUCT_COMPONENT_ID")
    @OneToMany(cascade = CascadeType.ALL)
    public List<ProductLot> productLotList = new ArrayList<ProductLot>();

}
