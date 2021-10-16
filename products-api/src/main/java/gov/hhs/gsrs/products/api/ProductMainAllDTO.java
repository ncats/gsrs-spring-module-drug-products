package gov.hhs.gsrs.products.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductMainAllDTO {

    public String productId;
    public String productNDC;
    public String nonProprietaryName;
    public String productType;
    public String status;
    public String marketingCategoryName;
    public String isListed;
    public String routeName;
    public String appTypeNumber;
    public String appType;
    public String appNumber;
    public String country;
    public String countryWithoutCode;
    public String labelerNDC;
    public String unitPresentation;
    public String source;
    public String sourceType;
    public String fromtable;
    public String provenance;

    private List<ProductNameAllDTO> productNameAllList = new ArrayList<>();
    private List<ProductCompanyAllDTO> productCompanyAllList = new ArrayList<>();
    private List<ProductIngredientAllDTO> productIngredientAllList = new ArrayList<>();
}

