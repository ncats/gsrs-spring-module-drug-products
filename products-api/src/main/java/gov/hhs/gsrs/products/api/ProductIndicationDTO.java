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

public class ProductIndicationDTO {
    private Long id;
    private String indication;
    private String indicationText;
    private String indicationCode;
    private String indicationCodeType;
    private String indicationGroup;
    private String indicationSource;
    private String indicationSourceType;
    private String indicationSourceUrl;
}
