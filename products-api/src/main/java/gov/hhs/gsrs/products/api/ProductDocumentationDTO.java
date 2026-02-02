package gov.hhs.gsrs.products.api;

import com.fasterxml.jackson.annotation.JsonFormat;
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

public class ProductDocumentationDTO {
    private Long id;
    private String documentId;
    private String documentType;
    private String setIdVersion;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    private Date effectiveTime;
    private String jurisdictions;
}
