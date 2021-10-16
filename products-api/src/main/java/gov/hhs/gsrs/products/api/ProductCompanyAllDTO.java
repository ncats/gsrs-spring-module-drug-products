package gov.hhs.gsrs.products.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProductCompanyAllDTO {

    private String id;
    private String productId;
    private String labelerName;
    private String labelerDuns;
    private String feiNumber;
    private String ndcLabelerCode;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String countryWithoutCode;

}
