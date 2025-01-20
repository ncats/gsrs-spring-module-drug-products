package gov.hhs.gsrs.products.processor.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DailyMedXmlFileDataHolder {
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

    private Map<String, ImportProduct> products = new HashMap<>();
    public ImportProduct getProduct(String key) {
        return this.products.get(key);
    }

    public void putProduct(String key, ImportProduct product) {
        this.products.put(key, product);
    }

}
