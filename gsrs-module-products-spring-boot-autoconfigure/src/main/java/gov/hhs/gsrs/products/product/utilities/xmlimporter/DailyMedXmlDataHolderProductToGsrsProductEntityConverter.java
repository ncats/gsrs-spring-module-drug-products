package gov.hhs.gsrs.products.product.utilities.xmlimporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gov.hhs.gsrs.products.product.models.*;

import java.util.List;

public class DailyMedXmlDataHolderProductToGsrsProductEntityConverter {

    public gov.hhs.gsrs.products.product.models.Product convert(gov.hhs.gsrs.products.product.utilities.xmlimporter.Product dailyMedProduct) {
        gov.hhs.gsrs.products.product.models.Product product = new gov.hhs.gsrs.products.product.models.Product();
        product.setManufacturerName(dailyMedProduct.manufacturerName);
        product.setManufacturerCode(dailyMedProduct.manufacturerCode);
        product.setManufacturerCodeType("DUNS NUMBER");
        product.setCountryCode("UNITED STATES OF AMERICA");
        product.setLanguage("ENGLISH");
        product.setRouteAdmin(dailyMedProduct.routeCode);
        ProductProvenance pv = new ProductProvenance();
        pv.setProvenance("XML_SPL");
        pv.setProductStatus(dailyMedProduct.productStatus);
        pv.setProductType(dailyMedProduct.productType);
        pv.setApplicationType(dailyMedProduct.fdaApplicationType);
        pv.setApplicationNumber(dailyMedProduct.fdaApprovalId);
        pv.setPublicDomain("YES");
        pv.setIsListed("YES");
        pv.setJurisdictions("USA");
        pv.setProductUrl("https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid="+ dailyMedProduct.setId);

        List<ProductName> productNames = pv.getProductNames();
        ProductName productName1 = new ProductName();
        productName1.setProductName(dailyMedProduct.productName);
        productName1.setProductNameType("product name");
        productNames.add(productName1);
        ProductName productName2 = new ProductName();
        productName2.setProductName(dailyMedProduct.genericName);
        productName2.setProductNameType("generic name");
        productNames.add(productName2);
        pv.setProductNames(productNames);

        List<ProductCode> productCodes = pv.getProductCodes();
        ProductCode productCode = new ProductCode();
        productCode.setProductCode(dailyMedProduct.productCode);
        productCode.setProductCodeType("NDC code");
        productCodes.add(productCode);
        pv.setProductCodes(productCodes);

        List<ProductCompany> productCompanies = pv.getProductCompanies();
        ProductCompany productCompany = new ProductCompany();
        List<ProductCompanyCode> productCompanyCodes = productCompany.getProductCompanyCodes();
        ProductCompanyCode productCompanyCode= new ProductCompanyCode();
        productCompanyCode.setCompanyCode(dailyMedProduct.manufacturerCode);
        productCompanyCode.setCompanyCodeType("DUNS NUMBER");
        productCompanyCodes.add(productCompanyCode);
        productCompany.setProductCompanyCodes(productCompanyCodes);
        productCompanies.add(productCompany);







        List<ProductDocumentation> productDocumentations = pv.getProductDocumentations();
        ProductDocumentation productDocumentation = new ProductDocumentation();
        productDocumentation.setDocumentId(dailyMedProduct.setId);
        productDocumentation.setDocumentType("SET ID");
        productDocumentations.add(productDocumentation);
        pv.setProductDocumentations(productDocumentations);
        List<ProductManufactureItem> productManufactureItems = product.getProductManufactureItems();
        ProductManufactureItem productManufactureItem = new ProductManufactureItem();
        List<ProductLot> productLots = productManufactureItem.getProductLots();
        ProductLot productLot = new ProductLot();
        List<ProductIngredient> productIngredients = productLot.getProductIngredients();
        for (Ingredient ingredient: dailyMedProduct.ingredients.values()) {
            ProductIngredient productIngredient = new ProductIngredient();
            productIngredient.setApplicantIngredName(ingredient.substanceName);
            productIngredient.setIngredientType(ingredient.classCode);
            productIngredient.setSubstanceKey(ingredient.uniiCode);
            productIngredient.setSubstanceKeyType("UNII");
            productIngredient.setOriginalNumeratorNumber(ingredient.numerator);
            productIngredient.setOriginalNumeratorUnit(ingredient.numeratorUnit);
            productIngredient.setOriginalDenominatorNumber(ingredient.denominator);
            productIngredient.setOriginalDenominatorUnit(ingredient.denominatorUnit);
            productIngredient.setSubstanceKey(ingredient.uniiCode);
            productIngredient.setBasisOfStrengthSubstanceKey(ingredient.uniiCode);
            productIngredient.setBasisOfStrengthSubstanceKeyType("UNII");
            productIngredients.add(productIngredient);

            System.out.println("blahblah or something");
        }
        productLots.add(productLot);
        productManufactureItem.setProductLots(productLots);
        productManufactureItem.setDosageForm(dailyMedProduct.routeCode);
        productManufactureItem.setCharColor(dailyMedProduct.splColorValue);
        productManufactureItem.setCharShape(dailyMedProduct.splShapeValue);
        productManufactureItem.setCharSize(dailyMedProduct.splSizeValue);
        productManufactureItems.add(productManufactureItem);

        product.productProvenances.add(pv);
        System.out.println("done product json structure");
        return product;
    }

    public String asSerialized(gov.hhs.gsrs.products.product.models.Product product, boolean pretty) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jn = objectMapper.valueToTree(product);
        try {
            if (pretty) {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            return objectMapper.writeValueAsString((JsonNode) jn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //return "";
        }
        return "";

    }
}