package gov.hhs.gsrs.products.product;

import ix.core.FieldNameDecorator;

import java.util.HashMap;
import java.util.Map;

public class ProductFieldNameDecorator implements FieldNameDecorator {

    private static final Map<String, String> displayNames;

    static {
        Map<String, String> m = new HashMap<>();

        m.put("root_creationDate", "Record Create Date");
        m.put("root_lastModifiedDate", "Record Last Edited");
        m.put("root_productContainer", "Product Container");
        m.put("root_routeAdmin", "Route of Administration");
        m.put("root_unitPresentation", "Unit Presentation");
        m.put("root_language", "Language");
        m.put("root_shelfLife", "Shelf Life");
        m.put("root_storageConditions", "Storage Conditions");
        m.put("root_manufacturerName", "Product Manufacturer Name");
        m.put("root_manufacturerCode", "Product Manufacturer Code");
        m.put("root_manufacturerCodeType", "Product Manufacturer Code Type");
        m.put("root_countryCode", "Product Country Code");

        m.put("root_productProvenances_provenance", "Provenance");
        m.put("root_productProvenances_productStatus", "Product Status");
        m.put("root_productProvenances_productType", "Product Type");
        m.put("root_productProvenances_applicationType", "Application Type");
        m.put("root_productProvenances_applicationNumber", "Application Number");
        m.put("root_productProvenances_publicDomain", "Public Domain");
        m.put("root_productProvenances_isListed", "Is Listed");
        m.put("root_productProvenances_marketingCategoryCode", "Marketing Category Code");
        m.put("root_productProvenances_marketingCategoryName", "Marketing Category Name");
        m.put("root_productProvenances_controlSubstanceClass", "Control Substance Class");
        m.put("root_productProvenances_controlSubstanceSource", "Control Substance Source");
        m.put("root_productProvenances_controlSubstanceCode", "Control Substance Code");

        m.put("root_productProvenances_productNames_productName", "Product Name");
        m.put("root_productProvenances_productNames_productNameType", "Product Name Type");
        m.put("root_productProvenances_productNames_displayName", "Product Is Display Name");

        m.put("root_productProvenances_productCodes_productCode", "Product ID");
        m.put("root_productProvenances_productCodes_productCodeType", "Product Code Type");

        m.put("root_productProvenances_productCompanies_companyName", "Labeler Name");
        m.put("root_productProvenances_productCompanies_companyAddress", "Labeler Address");
        m.put("root_productProvenances_productCompanies_companyCity", "Labeler City");
        m.put("root_productProvenances_productCompanies_companyState", "Labeler State");
        m.put("root_productProvenances_productCompanies_companyZip", "Labeler Zipcode");
        m.put("root_productProvenances_productCompanies_companyCountry", "Company Country");
        m.put("root_productProvenances_productCompanies_companyRole", "Company Role");
        m.put("root_productProvenances_productCompanies_companyPublicDomain", "Labeler Public Domain");
        m.put("root_productProvenances_productCompanies_companyGpsLatitude", "Labeler GPS Latitude");
        m.put("root_productProvenances_productCompanies_companyGpsLongitude", "Labeler GPS Longitude");
        m.put("root_productProvenances_productCompanies_companyGpsElevation", "Labeler GPS Elevation");

        m.put("root_productProvenances_productCompanies_productCompanyCodes_companyCode", "Labeler Code");
        m.put("root_productProvenances_productCompanies_productCompanyCodes_productCodeType", "Product Code Type");

        m.put("root_productProvenances_productIndications_indication", "Indication");

        m.put("root_productManufactureItems_dosageForm", "Dosage Form Name");
        m.put("root_productManufactureItems_charColor", "Manufacture Item Color");
        m.put("root_productManufactureItems_charShape", "Manufacture Item Shape");
        m.put("root_productManufactureItems_unit", "Manufacture Item Unit");

        m.put("root_productManufactureItems_productManufacturers_manufacturerName", "Manufacturer Name");
        m.put("root_productManufactureItems_productManufacturers_manufacturerRole", "Manufacturer Role");
        m.put("root_productManufactureItems_productManufacturers_manufacturerCode", "Manufacturer Code");
        m.put("root_productManufactureItems_productManufacturers_manufacturerCodeType", "Manufacturer Code Type");
        m.put("root_productManufactureItems_productManufacturers_manufacturedItemCode", "Manufactured Item Code");
        m.put("root_productManufactureItems_productManufacturers_manufacturedItemCodeType", "Manufactured Item Code Type");

        m.put("root_productManufactureItems_productLots_productIngredients_applicantIngredName", "Manufacturer Role");
        m.put("root_productManufactureItems_productLots_productIngredients_substanceKey", "Substance Key");
        m.put("root_productManufactureItems_productLots_productIngredients_substanceKeyType", "Substance Key Type");
        m.put("root_productManufactureItems_productLots_productIngredients_ingredientType", "Ingredient Type");
        m.put("root_productManufactureItems_productLots_productIngredients_ingredientFunction", "Ingredient Function");
        m.put("root_productManufactureItems_productLots_productIngredients_ingredientLocation", "Ingredient Location");
        m.put("root_productManufactureItems_productLots_productIngredients_unit", "Ingredient Average Unit");
        m.put("root_productManufactureItems_productLots_productIngredients_originalNumeratorUnit", "Original Numerator Unit");
        m.put("root_productManufactureItems_productLots_productIngredients_originalDenominatorUnit", "Original Denominator Unit");

        displayNames = m;
    }

    @Override
    public String getDisplayName(String field) {
        String fdisp = displayNames.get(field);
        if (fdisp == null) {
            return field;
        }
        return fdisp;
    }

}
