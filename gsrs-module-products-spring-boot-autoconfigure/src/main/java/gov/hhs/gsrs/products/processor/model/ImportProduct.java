package gov.hhs.gsrs.products.processor.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ImportProduct {
  private String productType;
  private String title;
  private String effectiveTime;
  private String setId;
  private String versionId;
  private String manufacturerCode;
  private String manufacturerName;
  private String assignedOrganizationName;
  private String representedOrganizationIdExtension;
  private String duns2;

  private String ndcCode;
  private String productCode;
  private String productName;
  private String companyName;
  private String genericName;
  private String formCode;
  private String routeCode;
  private String packagedFormCode;
  private String productStatus;
  private String marketingDateLow;
  private String fdaApprovalId;
  private String fdaApplicationType;
  private String splImprintValue;
  private String splColorValue;
  private String splShapeValue;
  private String splSizeValue;

  Map<String, ImportIngredient> ingredients = new HashMap<String, ImportIngredient>();

  public ImportIngredient getIngredient(String key) {
        return this.ingredients.get(key);
    }

  public void putIngredient(String key, ImportIngredient ingredient) {
    this.ingredients.put(key, ingredient);
  }

}
