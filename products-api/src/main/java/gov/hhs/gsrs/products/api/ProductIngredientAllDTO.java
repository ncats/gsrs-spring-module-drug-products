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
public class ProductIngredientAllDTO {

    private String id;
    private String applicationId;
    private String substanceKey;
    private String basisOfStrengthSubstanceKey;
    private String substanceName;
    private String substanceApprovalId;
    private String substanceUuid;
    private String ingredientType;
    private String activeMoietyName;
    private String activeMoietyUnii;
    private String strengthNumber;
    private String strengthNumeratorUnit;
    private String dosageFormName;
    private String fromTable;
}
