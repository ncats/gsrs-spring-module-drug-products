package gov.nih.ncats.product.product.models;

import ix.core.models.Indexable;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="SRSCID_PRODUCT_INGREDIENT", schema = "srscid")
public class ProductIngredient extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodIngredSeq", sequenceName="SRSCID.SRSCID_SQ_PRODUCT_INGRED_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodIngredSeq")
    @Column(name="PRODUCT_INGRED_ID")
    public Long id;

    @Column(name="APPLICANT_INGRED_NAME")
    public String applicantIngredName;

    @Indexable(facet = true, name = "Substance Key")
    @Column(name="SUBSTANCE_KEY")
    public String substanceKey;

    @Column(name="SUBSTANCE_KEY_TYPE")
    public String substanceKeyType;

    @Indexable
    @Column(name="BOS_SUBSTANCE_KEY")
    public String basisOfStrengthSubstanceKey ;

    @Column(name="BOS_SUBSTANCE_KEY_TYPE")
    public String basisOfStrengthSubstanceKeyType;

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

    /*
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
    */

    /*
    @Transient
    @JsonProperty("_substanceUuid")
    public String _substanceUuid;

    @Transient
    @JsonProperty("_approvalID")
    public String _approvalID;

    @Transient
    @JsonProperty("_name")
    public String _name;

    @Transient
    @JsonProperty("_basisOfStrengthSubstanceUuid")
    public String _basisOfStrengthSubstanceUuid;

    @Transient
    @JsonProperty("_basisOfStrengthApprovalID")
    public String _basisOfStrengthApprovalID;

    @Transient
    @JsonProperty("_basisOfStrengthName")
    public String _basisOfStrengthName;
    */
}

