package gov.hhs.gsrs.products.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.gsrs.products.processor.model.ImportIngredient;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
    private static final String KEY_TYPE_UNII = "UNII";

    public Product convert(ImportProduct dailyMedProduct) {
        Product product = getProduct(dailyMedProduct);
        ProductProvenance pv = getProductProvenance(dailyMedProduct);
        product.productProvenances.add(pv);

        List<ProductCompany> productCompanies = pv.getProductCompanies();
        ProductCompany productCompany = new ProductCompany();
        List<ProductCompanyCode> productCompanyCodes = productCompany.getProductCompanyCodes();
        ProductCompanyCode productCompanyCode= new ProductCompanyCode();
        productCompanyCode.setCompanyCode(dailyMedProduct.getManufacturerCode());
        productCompanyCode.setCompanyCodeType("DUNS NUMBER");
        productCompanyCodes.add(productCompanyCode);
        productCompany.setProductCompanyCodes(productCompanyCodes);
        productCompanies.add(productCompany);

        ProductDocumentation productDocumentation = new ProductDocumentation();
        productDocumentation.setDocumentId(dailyMedProduct.getSetId());
        productDocumentation.setDocumentType("SET ID");
        pv.getProductDocumentations().add(productDocumentation);

        ProductManufactureItem productManufactureItem = new ProductManufactureItem();
        ProductLot productLot = new ProductLot();
        for (ImportIngredient ingredient: dailyMedProduct.getIngredients().values()) {
            ProductIngredient productIngredient = getProductIngredient(ingredient);
            productLot.getProductIngredients().add(productIngredient);
        }
        productManufactureItem.getProductLots().add(productLot);
        productManufactureItem.setDosageForm(dailyMedProduct.getRouteCode());
        productManufactureItem.setCharColor(dailyMedProduct.getSplColorValue() );
        productManufactureItem.setCharShape(dailyMedProduct.getSplShapeValue());
        productManufactureItem.setCharSize(dailyMedProduct.getSplSizeValue());
        product.getProductManufactureItems().add(productManufactureItem);

        log.trace("done building product");
        return product;
    }

    private static ProductIngredient getProductIngredient(ImportIngredient ingredient) {
        ProductIngredient productIngredient = new ProductIngredient();
        productIngredient.setSubstanceKey(ingredient.getClassCode());
        productIngredient.setSubstanceKey(ingredient.getUniiCode());
        productIngredient.setSubstanceKeyType(KEY_TYPE_UNII);
        productIngredient.setOriginalNumeratorNumber(ingredient.getNumerator());
        productIngredient.setOriginalNumeratorUnit(ingredient.getNumeratorUnit());
        productIngredient.setOriginalDenominatorNumber(ingredient.getDenominator());
        productIngredient.setOriginalDenominatorUnit(ingredient.getDenominatorUnit());
        productIngredient.setSubstanceKey(ingredient.getUniiCode());
        productIngredient.setBasisOfStrengthSubstanceKey(ingredient.getUniiCode());
        productIngredient.setBasisOfStrengthSubstanceKeyType(KEY_TYPE_UNII);
        return productIngredient;
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

        log.info("done product json structure");
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