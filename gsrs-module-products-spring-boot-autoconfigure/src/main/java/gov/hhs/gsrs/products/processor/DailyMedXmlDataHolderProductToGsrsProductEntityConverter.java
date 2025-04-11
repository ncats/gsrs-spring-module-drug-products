package gov.hhs.gsrs.products.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.gsrs.products.processor.model.ImportIngredient;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.*;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Code originally written by Aruna Nishtala
*/
@Slf4j
public class DailyMedXmlDataHolderProductToGsrsProductEntityConverter {

    private static final  String COUNTRY_NAME ="United States of America";
    private static final String DEFAULT_LANGUAGE ="English";
    private static final String COUNTRY_CODE ="USA";
    private static final String DAILY_MED_URL_STEM ="https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=";
    private static final String SPL_PROVENANCE = "XML_SPL";
    private static final String KEY_TYPE_UNII = SubstanceApiService.SUBSTANCE_KEY_TYPE_APPROVAL_ID;
    private static final String CODE_TYPE = "DUNS NUMBER";
    private static final String PRODUCT_NAME_TYPE = "product name";
    private static final String PRODUCT_GENERIC_NAME_TYPE = "generic name";
    private static final String PRODUCT_DRUG_CODE = "NDC code";
    private static final String STANDARD_COMPANY_CODE_TYPE ="DUNS NUMBER";
    private static final String STANDARD_PRODUCT_DOCUMENTATION_TYPE ="SET ID";

    public Product convert(ImportProduct dailyMedProduct) {
        Product product = getProduct(dailyMedProduct);
        log.trace("about to call getProductProvenance");
        ProductProvenance pv = getProductProvenance(dailyMedProduct);
        pv.setProductCompanies(Collections.singletonList(getProductCompany(dailyMedProduct)));
        pv.setProductDocumentations(Collections.singletonList(getProductDocumentation(dailyMedProduct)));
        product.setProductProvenances(Collections.singletonList(pv));
        product.setProductManufactureItems(Collections.singletonList(getProductManufactureItem(dailyMedProduct)));
        ObjectMapper mapper = new ObjectMapper();
        log.trace("done building product {}", mapper.valueToTree(product).toPrettyString());
        return product;
    }

    public static ProductIngredient getProductIngredient(ImportIngredient ingredient) {
        ProductIngredient productIngredient = new ProductIngredient();
        productIngredient.setApplicantIngredName(ingredient.getSubstanceName());
        productIngredient.setIngredientType(ingredient.getClassCode());
        productIngredient.setSubstanceKey(ingredient.getUniiCode());
        productIngredient.setSubstanceKeyType(KEY_TYPE_UNII);
        productIngredient.setOriginalNumeratorNumber(ingredient.getNumerator());
        productIngredient.setOriginalNumeratorUnit(ingredient.getNumeratorUnit());
        productIngredient.setOriginalDenominatorNumber(ingredient.getDenominator());
        productIngredient.setOriginalDenominatorUnit(ingredient.getDenominatorUnit());
        productIngredient.setBasisOfStrengthSubstanceKey(ingredient.getUniiCode());
        productIngredient.setBasisOfStrengthSubstanceKeyType(KEY_TYPE_UNII);
        return productIngredient;
    }

    public static Product getProduct(ImportProduct importProduct) {
        Product product = new Product();
        product.setManufacturerName(importProduct.getManufacturerName());
        product.setManufacturerCode(importProduct.getManufacturerCode());
        product.setManufacturerCodeType(CODE_TYPE);
        product.setCountryCode(COUNTRY_NAME);
        product.setLanguage(DEFAULT_LANGUAGE);
        product.setRouteAdmin(importProduct.getRouteCode());
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

        pv.setProductNames(getProductNames(dailyMedProduct));

        pv.setProductCodes(getProductCodes(dailyMedProduct));
        return pv;
    }

    private static List<ProductName> getProductNames(ImportProduct dailyMedProduct) {
        List<ProductName> productNames = new ArrayList<>();
        ProductName productName1 = new ProductName();
        productName1.setProductName(dailyMedProduct.getProductName());
        productName1.setProductNameType(PRODUCT_NAME_TYPE);
        productNames.add(productName1);
        ProductName productName2 = new ProductName();
        productName2.setProductName(dailyMedProduct.getGenericName());
        productName2.setProductNameType(PRODUCT_GENERIC_NAME_TYPE);
        productNames.add(productName2);
        return productNames;
    }

    private static List<ProductCode> getProductCodes(ImportProduct dailyMedProduct) {
        List<ProductCode> productCodes = new ArrayList<>();
        ProductCode productCode = new ProductCode();
        productCode.setProductCode(dailyMedProduct.getNdcCode());
        productCode.setProductCodeType(PRODUCT_DRUG_CODE);
        productCodes.add(productCode);
        return productCodes;
    }

    private static ProductCompany getProductCompany(ImportProduct importProduct) {
        ProductCompany productCompany = new ProductCompany();
        List<ProductCompanyCode> productCompanyCodes = productCompany.getProductCompanyCodes();
        ProductCompanyCode productCompanyCode= new ProductCompanyCode();
        productCompanyCode.setCompanyCode(importProduct.getManufacturerCode());
        productCompanyCode.setCompanyCodeType(STANDARD_COMPANY_CODE_TYPE);
        productCompanyCodes.add(productCompanyCode);
        productCompany.setProductCompanyCodes(productCompanyCodes);
        return productCompany;
    }

    private static ProductDocumentation getProductDocumentation(ImportProduct importProduct) {
        ProductDocumentation productDocumentation = new ProductDocumentation();
        productDocumentation.setDocumentId(importProduct.getSetId());
        productDocumentation.setDocumentType(STANDARD_PRODUCT_DOCUMENTATION_TYPE);
        return productDocumentation;
    }

    private static ProductManufactureItem getProductManufactureItem( ImportProduct importProduct) {
        ProductManufactureItem productManufactureItem = new ProductManufactureItem();
        ProductLot productLot = new ProductLot();
        List<ProductIngredient> productIngredients = new ArrayList<>();
        for (ImportIngredient ingredient: importProduct.getIngredients().values()) {
            ProductIngredient productIngredient = getProductIngredient(ingredient);
            log.trace("got ingredient {}", productIngredient.applicantIngredName);
            productIngredients.add(productIngredient);
        }
        productLot.setProductIngredients(productIngredients);

        productManufactureItem.setProductLots(Collections.singletonList(productLot));
        productManufactureItem.setDosageForm(importProduct.getRouteCode());
        productManufactureItem.setCharColor(importProduct.getSplColorValue() );
        productManufactureItem.setCharShape(importProduct.getSplShapeValue());
        productManufactureItem.setCharSize(importProduct.getSplSizeValue());
        return productManufactureItem;
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