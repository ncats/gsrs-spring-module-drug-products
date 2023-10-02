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

public class ProductManufacturerDTO {
    private Long id;
    private String manufacturerName;
    private String manufacturerRole;
    private String manufacturerCode;
    private String manufacturerCodeType;
    private String manufacturedItemCode;
    private String manufacturedItemCodeType;
}
