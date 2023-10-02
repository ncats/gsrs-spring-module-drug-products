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
public class ProductDTO {

    private Long id;
    private String pharmacedicalDosageForm;
    private String routeAdmin;
    private String unitPresentation;
    private String countryCode;
    private String language;
    private String shelfLife;
    private String storageConditions;
    private String numberOfManufactureItem;
    private String manufacturerName;
    private String manufacturerCode;
    private String manufacturerCodeType;

    private List<ProductProvenanceDTO> productProvenances = new ArrayList();
    private List<ProductManufactureItemDTO> productManufactureItems = new ArrayList();
}

