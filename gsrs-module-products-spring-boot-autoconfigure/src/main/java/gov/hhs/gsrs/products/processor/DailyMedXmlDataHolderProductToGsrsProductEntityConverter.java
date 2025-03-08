package gov.hhs.gsrs.products.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/*
Code originally written by Aruna Nishtala
*/
@Slf4j
public class DailyMedXmlDataHolderProductToGsrsProductEntityConverter {

    private static final  String COUNTRY_NAME ="United States of America";
    private static final String LANGUAGE ="English";
    private static final String COUNTRY_CODE ="USA";
    private static final String DAILY_MED_URL_STEM ="https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=";
    private static final String SPL_PROVENANCE = "XML_SPL";

    public Product convert(ImportProduct dailyMedProduct) {
        Product product = getProduct(dailyMedProduct);
        ProductProvenance pv = getProductProvenance(dailyMedProduct);
        product.productProvenances.add(pv);
        log.trace("done product json structure");
        return product;
    }

    private static Product getProduct(ImportProduct dailyMedProduct) {
        Product product = new Product();
        product.setManufacturerName(dailyMedProduct.getManufacturerName());
        product.setManufacturerCode(dailyMedProduct.getManufacturerCode());
        product.setManufacturerCodeType("DUNS NUMBER");
        product.setCountryCode(COUNTRY_NAME);
        product.setLanguage(LANGUAGE);
        product.setRouteAdmin(dailyMedProduct.getRouteCode());
        return product;
    }

    private static ProductProvenance getProductProvenance(ImportProduct dailyMedProduct) {
        ProductProvenance pv = new ProductProvenance();
        pv.setProvenance(SPL_PROVENANCE);
        pv.setProductStatus(dailyMedProduct.getProductStatus());
        pv.setProductType(dailyMedProduct.getProductType());
        pv.setApplicationType(dailyMedProduct.getFdaApplicationType());
        pv.setApplicationNumber(dailyMedProduct.getFdaApprovalId());
        pv.setPublicDomain("YES");
        pv.setIsListed("YES");
        pv.setJurisdictions(COUNTRY_CODE);
        pv.setProductUrl(DAILY_MED_URL_STEM + dailyMedProduct.getSetId());

        List<ProductName> productNames = pv.getProductNames();
        ProductName productName1 = new ProductName();
        productName1.setProductName(dailyMedProduct.getProductName());
        productName1.setProductNameType("product name");
        productNames.add(productName1);
        ProductName productName2 = new ProductName();
        productName2.setProductName(dailyMedProduct.getGenericName());
        productName2.setProductNameType("generic name");
        productNames.add(productName2);
        pv.setProductNames(productNames);

        List<ProductCode> productCodes = pv.getProductCodes();
        ProductCode productCode = new ProductCode();
        productCode.setProductCode(dailyMedProduct.getNdcCode());
        productCode.setProductCodeType("NDC code");
        productCodes.add(productCode);
        pv.setProductCodes(productCodes);

        List<ProductCompany> productCompanies = pv.getProductCompanies();
        ProductCompany productCompany = new ProductCompany();
        List<ProductCompanyCode> productCompanyCodes = productCompany.getProductCompanyCodes();
        ProductCompanyCode productCompanyCode= new ProductCompanyCode();
        productCompanyCode.setCompanyCode(dailyMedProduct.getManufacturerCode());
        productCompanyCode.setCompanyCodeType("DUNS NUMBER");
        productCompanyCodes.add(productCompanyCode);
        productCompany.setProductCompanyCodes(productCompanyCodes);
        productCompanies.add(productCompany);

        List<ProductDocumentation> productDocumentations = pv.getProductDocumentations();
        ProductDocumentation productDocumentation = new ProductDocumentation();
        productDocumentation.setDocumentId(dailyMedProduct.getSetId());
        productDocumentation.setDocumentType("SET ID");
        productDocumentations.add(productDocumentation);
        pv.setProductDocumentations(productDocumentations);
        Product product = new Product();
        List<ProductManufactureItem> productManufactureItems = product.getProductManufactureItems();
        ProductManufactureItem productManufactureItem = new ProductManufactureItem();
        List<ProductLot> productLots = productManufactureItem.getProductLots();
        ProductLot productLot = new ProductLot();
        List<ProductIngredient> productIngredients = productLot.getProductIngredients();
        for (Ingredient ingredient: dailyMedProduct.getIngredients().values()) {
            ProductIngredient productIngredient = new ProductIngredient();
            productIngredient.setSubstanceKey(ingredient.classCode);
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

        return pv;
    }

    public void printSerialized(Product product) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jn = objectMapper.valueToTree(product);
        try {
            log.info(objectMapper.writeValueAsString(jn));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}