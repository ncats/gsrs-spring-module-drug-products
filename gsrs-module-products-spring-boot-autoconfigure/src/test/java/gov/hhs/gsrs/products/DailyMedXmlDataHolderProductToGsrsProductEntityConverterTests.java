package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.processor.DailyMedXmlDataHolderProductToGsrsProductEntityConverter;
import gov.hhs.gsrs.products.processor.model.ImportIngredient;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.models.ProductIngredient;
import gov.hhs.gsrs.products.product.models.ProductProvenance;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DailyMedXmlDataHolderProductToGsrsProductEntityConverterTests {

    @Test
    void testGetProductIngredientNumerator() {
        String numerator1 = "91";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setNumerator(numerator1);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(numerator1, ingredient.getOriginalNumeratorNumber());
    }

    @Test
    void testGetProductIngredientNumeratorPercent() {
        String numeratorUnits = "mg";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setNumeratorUnit(numeratorUnits);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(numeratorUnits, ingredient.getOriginalNumeratorUnit());
    }

    @Test
    void testGetProductIngredientDenominator() {
        String denominator = "54921";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setDenominator(denominator);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(denominator, ingredient.getOriginalDenominatorNumber());
    }


    @Test
    void testGetProductIngredientDenominatorPercent() {
        String denominatorUnits = "micrograms";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setDenominatorUnit(denominatorUnits);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(denominatorUnits, ingredient.getOriginalDenominatorUnit());
    }

    @Test
    void testGetProductIngredientName() {
        String ingredientName = "Aqua";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setSubstanceName(ingredientName);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(ingredientName, ingredient.getApplicantIngredName());
    }

    @Test
    void testGetProductIngredientKey() {
        String ingredientUnii = "30KYC7MIAI";
        ImportIngredient ingredientToImport = new ImportIngredient();
        ingredientToImport.setUniiCode(ingredientUnii);
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(ingredientUnii, ingredient.getSubstanceKey());
    }

    @Test
    void testGetProductIngredientKeyType() {
        ImportIngredient ingredientToImport = new ImportIngredient();
        ProductIngredient ingredient= DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductIngredient(ingredientToImport);
        Assertions.assertEquals(SubstanceApiService.SUBSTANCE_KEY_TYPE_APPROVAL_ID, ingredient.getSubstanceKeyType());
    }

    @Test
    void testGetProductManufacturerName() {
        String manufacturerName = "Albemarle";
        ImportProduct productToImport = new ImportProduct();
        productToImport.setManufacturerName(manufacturerName);
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals(manufacturerName, product.getManufacturerName());
    }

    @Test
    void testGetProductManufacturerCode() {
        String manufacturerCode = "A19199";
        ImportProduct productToImport = new ImportProduct();
        productToImport.setManufacturerCode(manufacturerCode);
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals(manufacturerCode, product.getManufacturerCode());
    }

    @Test
    void testGetProductManufacturerCodeType() {
        ImportProduct productToImport = new ImportProduct();
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals("DUNS NUMBER", product.getManufacturerCodeType());
    }

    @Test
    void testGetProductCountryCode() {
        ImportProduct productToImport = new ImportProduct();
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals("USA", product.getCountryCode());
    }

    @Test
    void testGetProductLanguage() {
        ImportProduct productToImport = new ImportProduct();
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals("English", product.getLanguage());
    }

    @Test
    void testGetProductRoute() {
        String route = "oral";
        ImportProduct productToImport = new ImportProduct();
        productToImport.setRouteCode(route);
        Product product = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProduct(productToImport);
        Assertions.assertEquals(route, product.getRouteAdmin());
    }

    @Test
    void testGetProductProvenanceStatus() {
        String status ="Ready to sell";
        ImportProduct productToImport = new ImportProduct();
        productToImport.setProductStatus(status);
        ProductProvenance productProvenance = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductProvenance(productToImport);
        Assertions.assertEquals(status, productProvenance.getProductStatus());
    }

    @Test
    void testGetProductProvenanceType() {
        String type ="Ready to sell";
        ImportProduct productToImport = new ImportProduct();
        productToImport.setProductType(type);
        ProductProvenance productProvenance = DailyMedXmlDataHolderProductToGsrsProductEntityConverter.getProductProvenance(productToImport);
        Assertions.assertEquals(type, productProvenance.getProductType());
    }
}
