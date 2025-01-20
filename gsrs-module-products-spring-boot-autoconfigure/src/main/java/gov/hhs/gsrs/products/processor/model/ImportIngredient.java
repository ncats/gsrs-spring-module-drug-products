package gov.hhs.gsrs.products.processor.model;

import lombok.Data;

@Data
public class ImportIngredient {
  private String substanceName;
  private String classCode;
  private String uniiCode;
  private String uuid;
  private String numerator;
  private String numeratorUnit;
  private String denominator;
  private String denominatorUnit;
  private String basisOfStrengthCode;
  private String basisOfStrengthName;

}
