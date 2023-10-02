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

public class ProductCompanyDTO {
    private Long id;
    private String companyName;
    private String companyAddress;
    private String companyCity;
    private String companyState;
    private String companyZip;
    private String companyCountry;
    private String companyGpsLatitude;
    private String companyGpsLongitude;
    private String companyGpsElevation;
    private String companyRole;
    private String companyPublicDomain;
    private Date startMarketingDate;
    private Date endMarketingDate;
    private String companyProductId;
    private String companyDocumentId;
    private String provenanceDocumentId;

    private List<ProductCompanyCodeDTO> productCompanyCodes = new ArrayList();

}
