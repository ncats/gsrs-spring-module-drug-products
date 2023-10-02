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

public class ProductLotDTO {
    private Long id;
    private String lotNo;
    private String lotSize;
    private String lotType;
    private Date expiryDate;
    private Date manufactureDate;

    private List<ProductIngredientDTO> productIngredients = new ArrayList();
}
