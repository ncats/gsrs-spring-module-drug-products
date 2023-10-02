package gov.hhs.gsrs.products.product.models;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Column(name="APPLICANT_INGRED_NAME", length=1000)
    public String applicantIngredName;

    @Indexable(facet = true, name = "Substance Key")
    @Column(name="SUBSTANCE_KEY")
    public String substanceKey;

    @Indexable(facet = true, name = "Substance Key")
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

    @Column(name="MANUFACTURER", length=500)
    public String manufacturer;

    @Column(name="LOT_NO")
    public String ingredLotNo;

    @Indexable(suggest = true, facet=true, name= "Ingredient Type", sortable = true)
    @Column(name="INGREDIENT_TYPE")
    public String ingredientType;

    @Column(name="UNIT")
    public String unit;

    @Column(name="RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name="NOTES", length=4000)
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

    @Column(name="MANU_INGRED_CATALOG_NUM")
    public String manufactureIngredientCatalogId;

    @Column(name="MANU_INGREDIENT_URL", length=500)
    public String manufactureIngredientUrl;

    // Set Parent Class
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

