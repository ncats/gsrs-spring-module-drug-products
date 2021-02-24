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
@Table(name="SRSCID_PRODUCT_INGREDIENT")
public class ProductIngredient extends AbstractGsrsEntity {

    @Id
    @SequenceGenerator(name="prodIngredSeq", sequenceName="SRSCID_SQ_PRODUCT_INGRED_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodIngredSeq")
    @Column(name="PRODUCT_INGRED_ID")
    public Long id;

    @Column(name="APPLICANT_INGRED_NAME")
    public String applicantIngredName;

    @Column(name="BDNUM")
    public String bdnum;

    @Column(name="BASIS_OF_STRENGTH")
    public String basisOfStrengthBdnum;

    @Column(name="AVERAGE")
    public Double average;

    @Column(name="LOW")
    public Double low;

    @Column(name="HIGH")
    public Double high;

    @Column(name="MANUFACTURER")
    public String manufacturer;

    @Column(name="LOT_NO")
    public String ingredLotNo;

    @Column(name="INGREDIENT_TYPE")
    public String ingredientType;

    @Column(name="UNIT")
    public String unit;

    @Column(name="RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name="NOTES")
    public String notes;

    @Column(name="GRADE")
    public String grade;

    @Column(name="INGREDIENT_LOCATION")
    public String ingredientLocation;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @CreatedDate
    @Indexable( name = "Ingredient Create Date", sortable=true)
    @Column(name = "CREATE_DATE")
    private Date creationDate;

    @JsonSerialize(using = GsrsDateSerializer.class)
    @JsonDeserialize(using = GsrsDateDeserializer.class)
    @LastModifiedDate
    @Indexable( name = "Ingredient Last Modified Date", sortable=true)
    @Column(name = "MODIFY_DATE")
    private Date lastModifiedDate;

}
