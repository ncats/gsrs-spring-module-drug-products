package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@SingleParent
@Data
@Entity
@Table(name="SRSCID_PRODUCT_INGREDIENT")
public class ProductIngredient extends ProductCommonData {

    @Id
    @SequenceGenerator(name="prodIngredSeq", sequenceName="SRSCID_SQ_PRODUCT_INGRED_ID",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodIngredSeq")
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

    @Column(name="CONFIDENTIALITY_CODE")
    public String confidentialityCode;

    @Column(name="ORIGINAL_NUMERATOR_NUMBER")
    public String originalNumeratorNumber;

    @Column(name="ORIGINAL_NUMERATOR_UNIT")
    public String originalNumeratorUnit;

    @Column(name="ORIGINAL_DENOMINATOR_NUMBER")
    public String originalDenominatorNumber;

    @Column(name="ORIGINAL_DENOMINATOR_UNIT")
    public String originalDenominatorUnit;

    @Indexable(indexed=false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="PRODUCT_LOT_ID", referencedColumnName="PRODUCT_LOT_ID")
    public ProductLot owner;

    public void setOwner(ProductLot productLot) {
        this.owner = productLot;
    }

}

