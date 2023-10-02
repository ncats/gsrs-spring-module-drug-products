package gov.hhs.gsrs.products.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductIngredientDTO {

    private Long id;
    private String applicantIngredName;
    private String substanceKey;
    private String substanceKeyType;
    private String basisOfStrengthSubstanceKey ;
    private String basisOfStrengthSubstanceKeyType;
    private Double average;
    private Double low;
    private Double high;
    private String manufacturer;
    private String ingredLotNo;
    private String ingredientType;
    private String unit;
    private String releaseCharacteristic;
    private String notes;
    private String grade;
    private String ingredientLocation;
    private String confidentialityCode;
    private String originalNumeratorNumber;
    private String originalNumeratorUnit;
    private String originalDenominatorNumber;
    private String originalDenominatorUnit;
    private String manufactureIngredientCatalogId;
    private String manufactureIngredientUrl;
}
