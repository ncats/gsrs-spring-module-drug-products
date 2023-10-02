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

public class ProductManufactureItemDTO {

    private Long id;
    private String charSize;
    private String charImprintText;
    private String charColor;
    private String charFlavor;
    private String charShape;
    private String charNumFragments;
    private String dosageForm;
    private String dosageFormCode;
    private String dosageFormCodeType;
    private String dosageFormNote;
    private String compositionNote;
    private String routeOfAdministration;
    private Double amount;
    private String unit;
    private String provenanceManufactureItemId;

    private List<ProductLotDTO> productLots = new ArrayList();
    private List<ProductManufacturerDTO> productManufacturers = new ArrayList();
}
