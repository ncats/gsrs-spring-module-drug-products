package gov.hhs.gsrs.products.product.models;

import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import gov.hhs.gsrs.products.product.services.SubstanceApiService.SubstanceKeyPair;

import ix.core.models.Indexable;
import ix.core.models.ParentReference;
import ix.core.SingleParent;
import gsrs.substances.dto.SubstanceDTO;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import java.util.Optional;

@SingleParent
@Data
@Entity
@Slf4j
@Table(name = "SRSCID_PRODUCT_INGREDIENT")
public class ProductIngredient extends ProductCommonData {

    @Transient
    @JsonIgnore
    @Autowired
    private SubstanceApiService substanceApiService;

    @Id
    @SequenceGenerator(name = "prodIngredSeq", sequenceName = "SRSCID_SQ_PRODUCT_INGRED_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "prodIngredSeq")
    @Column(name = "PRODUCT_INGRED_ID")
    public Long id;

    @Column(name = "APPLICANT_INGRED_NAME", length = 1000)
    public String applicantIngredName;

    @Indexable(facet = true, name = "Substance Key")
    @Column(name = "SUBSTANCE_KEY")
    public String substanceKey;

    @Indexable(facet = true, name = "Substance Key Type")
    @Column(name = "SUBSTANCE_KEY_TYPE")
    public String substanceKeyType;

    @Indexable(facet = true, name = "Basis of Strength Substance Key")
    @Column(name = "BOS_SUBSTANCE_KEY")
    public String basisOfStrengthSubstanceKey;

    @Column(name = "BOS_SUBSTANCE_KEY_TYPE")
    public String basisOfStrengthSubstanceKeyType;

    @Column(name = "AVERAGE")
    public Double average;

    @Column(name = "LOW")
    public Double low;

    @Column(name = "HIGH")
    public Double high;

    @Column(name = "MANUFACTURER", length = 500)
    public String manufacturer;

    @Column(name = "LOT_NO")
    public String ingredLotNo;

    @Indexable(suggest = true, facet = true, name = "Ingredient Type", sortable = true)
    @Column(name = "INGREDIENT_TYPE")
    public String ingredientType;

    @Indexable(suggest = true, facet = true, name = "Ingredient Function", sortable = true)
    @Column(name = "INGREDIENT_FUNCTION")
    public String ingredientFunction;

    @Column(name = "UNIT")
    public String unit;

    @Column(name = "RELEASE_CHARACTERISTIC")
    public String releaseCharacteristic;

    @Column(name = "NOTES", length = 4000)
    public String notes;

    @Column(name = "GRADE")
    public String grade;

    @Column(name = "INGREDIENT_LOCATION")
    public String ingredientLocation;

    @Column(name = "INGRED_LOCATION_TEXT")
    public String ingredientLocationText;

    @Column(name = "CONFIDENTIALITY_CODE")
    public String confidentialityCode;

    @Column(name = "ORIGINAL_NUMERATOR_NUMBER")
    public String originalNumeratorNumber;

    @Column(name = "ORIGINAL_NUMERATOR_UNIT")
    public String originalNumeratorUnit;

    @Column(name = "ORIGINAL_DENOMINATOR_NUMBER")
    public String originalDenominatorNumber;

    @Column(name = "ORIGINAL_DENOMINATOR_UNIT")
    public String originalDenominatorUnit;

    @Column(name = "MANU_INGRED_CATALOG_NUM")
    public String manufactureIngredientCatalogId;

    @Column(name = "MANU_INGREDIENT_URL", length = 500)
    public String manufactureIngredientUrl;

    // Set Parent Class
    @Indexable(indexed = false)
    @ParentReference
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PRODUCT_LOT_ID", referencedColumnName = "PRODUCT_LOT_ID")
    public ProductLot owner;

    public void setOwner(ProductLot productLot) {
        this.owner = productLot;
    }

    @JsonIgnore
    private void resolveSubstance() {
        // Read the Substance Key Type from the backend config
        // Read the Substance Key Type from the JSON
        // If backend key type is same as JSON key type, Do not need to call Substance Reslover, then save as is in the JSON.
        try {

            if (substanceApiService == null) {
                AutowireHelper.getInstance().autowire(this);
            }

            // Get SubstanceKeyType from backend config
            String substanceKeyTypeFromConfig = substanceApiService.getSubstanceKeyTypeFromConfig();
            if (substanceKeyTypeFromConfig != null) {
                // if JSON Substance Key Type is not null
                if (this.substanceKeyType != null) {

                    // Substance Key Type in (backend config) and in (JSON) are NOT same
                    if (!substanceKeyTypeFromConfig.equalsIgnoreCase(this.substanceKeyType)) {
                        /* IMPORTANT NOTE:
                        ONLY save the substance key and subtsance key Type into the database,
                        which is in the BACKEND CONFIG.

                        Call Substance Resolver to get the substance object based on the substance key type
                        in the JSON, and convert it to backend config Key and Key Type and save into the database.
                        */
                        // Convert FROM JSON Substance Key and Substance Key Type TO backend config Substance Key and Substance Key Type
                        SubstanceKeyPair subKeyPair = substanceApiService.convertSubstanceKeyBySubstanceKeyResolver(this.substanceKey, this.substanceKeyType);

                        if (subKeyPair != null) {
                            // Basis of Strength is same as Subtance Key and Substance Key Type
                            if ((this.basisOfStrengthSubstanceKey != null) && this.basisOfStrengthSubstanceKey.equalsIgnoreCase(this.substanceKey)) {
                                if ((this.basisOfStrengthSubstanceKeyType != null) && this.basisOfStrengthSubstanceKeyType.equalsIgnoreCase(this.substanceKeyType)) {
                                    this.basisOfStrengthSubstanceKey = subKeyPair.substanceKey;
                                    this.basisOfStrengthSubstanceKeyType = subKeyPair.substanceKeyType;
                                }
                            }

                            // Substance Key and Substance Key Type
                            this.substanceKey = subKeyPair.substanceKey;
                            this.substanceKeyType = subKeyPair.substanceKeyType;
                        }

                    } else {
                        // if Substance Key Type in backend config is same as in JSON
                    }
                }
            } else {
               log.warn("Could not read backend config substance.linking.keyType.productKeyType in Product module");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PrePersist
    public void prePersist() {
        resolveSubstance();
    }

    @PreUpdate
    public void preUpdate() {
        resolveSubstance();
    }
}

