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

public class ProductProvenanceDTO {

    private Long id;
    private String provenance;
    private String productStatus;
    private String productType;
    private String applicationType;
    private String applicationNumber;
    private String privateDomain;
    private String isListed;
    private String jurisdictions;
    private String marketingCategoryCode;
    private String marketingCategoryName;
    private String controlSubstanceCode;
    private String controlSubstanceClass;
    private String controlSubstanceSource;
    private String productUrl;

    private final List<ProductNameDTO> productNames = new ArrayList();
    private final List<ProductCodeDTO> productCodes = new ArrayList();
    private final List<ProductCompanyDTO> productCompanies = new ArrayList();
    private final List<ProductDocumentationDTO> productDocumentations = new ArrayList();
    private final List<ProductIndicationDTO> productIndications = new ArrayList();
}
