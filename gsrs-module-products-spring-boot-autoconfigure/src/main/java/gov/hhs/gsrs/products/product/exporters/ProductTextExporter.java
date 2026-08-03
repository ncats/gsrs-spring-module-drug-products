package gov.hhs.gsrs.products.product.exporters;

import gov.hhs.gsrs.products.product.models.*;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;

import ix.core.EntityFetcher;
import ix.ginas.exporters.*;
import ix.ginas.models.v1.Substance;
import ix.ginas.models.v1.Relationship;
import ix.ginas.models.v1.SubstanceReference;

import java.util.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

enum ProdTextDefaultColumns implements Column {
    INGREDIENT_NUMBER,
    SUBSTANCE_NAME,
    APPROVAL_ID,
    SUBSTANCE_KEY,
    SUBSTANCE_KEY_TYPE,
    INGREDIENT_TYPE,
    ACTIVE_MOIETY_NAME,
    ACTIVE_MOIETY_APPROVAL_ID,
    AVERAGE,
    LOW,
    HIGH,
    UNIT,
    ORIGINAL_NUMERATOR_NUMBER,
    ORIGINAL_NUMERATOR_UNIT,
    ORIGINAL_DENOMINATOR_NUMBER,
    ORIGINAL_DENOMINATOR_UNIT,
    PROVENANCE,
    PRODUCT_ID_CODE,
    PRODUCT_CODE_TYPE,
    PRODUCT_NAME,
    PRODUCT_NAME_TYPE,
    PRODUCT_STATUS,
    PRODUCT_TYPE,
    APPLICATION_TYPE_NUMBER,
    MARKETING_CATEGORY_NAME,
    IS_LISTED,
    PUBLIC_DOMAIN,
    ROUTE_OF_ADMINISTRATOR,
    DOSAGE_FORM_NAME,
    LABELER_NAME,
    LABELER_CODE,
    LABELER_CODE_TYPE,
    LABELER_ADDRESS,
    LABELER_CITY,
    LABELER_STATE,
    LABELER_ZIP,
    LABELER_COUNTRY
}

@Slf4j
public class ProductTextExporter implements Exporter<Product> {

    private static SubstanceApiService substanceApiService;

    private final BufferedWriter bw;

    private int row = 1;
    private static int ingredientNumber = 0;
    private static int manufactureItemIndex = 0;
    private static int lotIndex = 0;
    private static int ingredientIndex = 0;

    public ProductTextExporter(OutputStream os, ExporterFactory.Parameters params, SubstanceApiService substanceApiService) throws IOException {

        // Substance API Service
        this.substanceApiService = substanceApiService;

        bw = new BufferedWriter(new OutputStreamWriter(os));
        StringBuilder sb = new StringBuilder();

        sb.append("INGREDIENT_NUMBER").append("\t")
                .append("SUBSTANCE_NAME").append("\t")
                .append("APPROVAL_ID").append("\t")
                .append("SUBSTANCE_KEY").append("\t")
                .append("SUBSTANCE_KEY_TYPE").append("\t")
                .append("INGREDIENT_TYPE").append("\t")
                .append("ACTIVE_MOIETY_NAME").append("\t")
                .append("ACTIVE_MOIETY_APPROVAL_ID").append("\t")
                .append("AVERAGE").append("\t")
                .append("LOW").append("\t")
                .append("HIGH").append("\t")
                .append("UNIT").append("\t")
                .append("ORIGINAL_NUMERATOR_NUMBER").append("\t")
                .append("ORIGINAL_NUMERATOR_UNIT").append("\t")
                .append("ORIGINAL_DENOMINATOR_NUMBER").append("\t")
                .append("ORIGINAL_DENOMINATOR_UNIT").append("\t")
                .append("PRODUCT_ID_CODE").append("\t")
                .append("PRODUCT_CODE_TYPE").append("\t")
                .append("PRODUCT_NAME").append("\t")
                .append("PRODUCT_NAME_TYPE").append("\t")
                .append("PROVENANCE").append("\t")
                .append("PRODUCT_STATUS").append("\t")
                .append("PRODUCT_TYPE").append("\t")
                .append("APPLICATION_TYPE_NUMBER").append("\t")
                .append("MARKETING_CATEGORY_NAME").append("\t")
                .append("IS_LISTED").append("\t")
                .append("PUBLIC_DOMAIN").append("\t")
                .append("ROUTE_OF_ADMINISTRATOR").append("\t")
                .append("DOSAGE_FORM_NAME").append("\t")
                .append("LABELER_NAME").append("\t")
                .append("LABELER_CODE").append("\t")
                .append("LABELER_CODE_TYPE").append("\t")
                .append("LABELER_ADDRESS").append("\t")
                .append("LABELER_CITY").append("\t")
                .append("LABELER_STATE").append("\t")
                .append("LABELER_ZIP").append("\t")
                .append("LABELER_COUNTRY").append("\t");

        bw.write(sb.toString());
        bw.newLine();
    }

    @Override
    public void export(Product p) throws IOException {

        /*****************************************************************************/
        // Export Product records and also display all the ingredients in each row
        /****************************************************************************/
        try {
            // Add one more column called "Ingredient Number" at the beginning.  Have it increment by one.
            // Each of these ingredients be new rows. Can duplicate the other product columns on each row.

            int col = 0;

            this.manufactureItemIndex = 0;
            this.ingredientIndex = 0;
            this.ingredientNumber = 1;

            if (p.productManufactureItems.size() > 0) {
                this.ingredientNumber = 0;
                for (int i = 0; i < p.productManufactureItems.size(); i++) {
                    ProductManufactureItem prodManuItem = p.productManufactureItems.get(i);
                    this.manufactureItemIndex = i;

                    for (int j = 0; j < prodManuItem.productLots.size(); j++) {
                        ProductLot prodLot = prodManuItem.productLots.get(j);
                        this.lotIndex = j;

                        // This Product has Ingredients
                        if (prodLot.productIngredients.size() > 0) {

                            for (int k = 0; k < prodLot.productIngredients.size(); k++) {
                                // Insert Ingredient data into the file
                                // This is the number of Ingredient in each Product.
                                // For each Product, insert one Substance data, on each row.

                                this.ingredientIndex = k;

                                this.ingredientNumber++;

                                writeDataInFile(p);

                            } // loop ProductIngredient
                        } // if Ingredient size > 0
                        else {
                            // if there is no Ingredient record
                            this.ingredientNumber++;

                            writeDataInFile(p);
                        }
                    } // loop productLots
                } // loop productManufactureItems
            } // if productManufactureItems.size() > 0
            else {
                writeDataInFile(p);
            }
        } // try
        catch (Exception ex) {
            log.error("Error exporting Product record for Product ID: " + p.id, ex);
        }
    }

    public void writeDataInFile(Product p) throws IOException {

        try {
            StringBuilder sb = new StringBuilder();

            sb.append(ingredientNumber).append("\t");  // INGREDIENT_NUMBER

            // Get Substance Name, Approval ID (UNII), Active Moiety, Substance Key, Substance Key Type, Ingredient Type
            getSubstanceKeyDetails(p, sb);

            StringBuilder sbProdCodeNdc = getProductCodeDetails(p, ProdTextDefaultColumns.PRODUCT_ID_CODE);
            sb.append(sbProdCodeNdc.toString()).append("\t");  // PRODUCT_ID/PRODUCT NDC

            StringBuilder sbProdCodeType = getProductCodeDetails(p, ProdTextDefaultColumns.PRODUCT_CODE_TYPE);
            sb.append(sbProdCodeType.toString()).append("\t");  // PRODUCT_CODE_TYPE

            StringBuilder sbProdName = getProductNameDetails(p, ProdTextDefaultColumns.PRODUCT_NAME);
            sb.append(sbProdName.toString()).append("\t");  // PRODUCT_NAME

            StringBuilder sbProdNameType = getProductNameDetails(p, ProdTextDefaultColumns.PRODUCT_NAME_TYPE);
            sb.append(sbProdNameType.toString()).append("\t");  // PRODUCT_NAME_TYPE

            StringBuilder sbProvenance = getProductProvenanceDetails(p, ProdTextDefaultColumns.PROVENANCE);
            sb.append(sbProvenance.toString()).append("\t");  // PROVENANCE

            StringBuilder sbProdStatus = getProductProvenanceDetails(p, ProdTextDefaultColumns.PRODUCT_STATUS);
            sb.append(sbProdStatus.toString()).append("\t");  // PRODUCT STATUS

            StringBuilder sbProdType = getProductProvenanceDetails(p, ProdTextDefaultColumns.PRODUCT_TYPE);
            sb.append(sbProdType.toString()).append("\t");  // PRODUCT_TYPE

            StringBuilder sbAppTypNum = getProductProvenanceDetails(p, ProdTextDefaultColumns.APPLICATION_TYPE_NUMBER);
            sb.append(sbAppTypNum.toString()).append("\t");  // APPLICATION_TYPE_NUMBER

            StringBuilder sbMarkCatName = getProductProvenanceDetails(p, ProdTextDefaultColumns.MARKETING_CATEGORY_NAME);
            sb.append(sbMarkCatName.toString()).append("\t");  // MARKETING_CATEGORY_NAME

            StringBuilder sbIsListed = getProductProvenanceDetails(p, ProdTextDefaultColumns.IS_LISTED);
            sb.append(sbIsListed.toString()).append("\t");  // IS_LISTED

            StringBuilder sbPublicDomain = getProductProvenanceDetails(p, ProdTextDefaultColumns.PUBLIC_DOMAIN);
            sb.append(sbPublicDomain.toString()).append("\t");  // PUBLIC_DOMAIN

            sb.append((p.routeAdmin != null) ? p.routeAdmin : "").append("\t");  // ROUTE_OF_ADMINISTRATOR

            StringBuilder sbDosageForm = getManufactureItemDetails(p, ProdTextDefaultColumns.DOSAGE_FORM_NAME);
            sb.append(sbDosageForm.toString()).append("\t");  // DOSAGE_FORM_NAME

            StringBuilder sbLabelerName = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_NAME);
            sb.append(sbLabelerName.toString()).append("\t");  // LABELER_NAME

            StringBuilder sbLabelerCode = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_CODE);
            sb.append(sbLabelerCode.toString()).append("\t");  // LABELER_CODE

            StringBuilder sbLabelerCodeType = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_CODE_TYPE);
            sb.append(sbLabelerCodeType.toString()).append("\t");  // LABELER_CODE_TYPE

            StringBuilder sbLabelerAdd = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_ADDRESS);
            sb.append(sbLabelerAdd.toString()).append("\t");  // LABELER_ADDRESS

            StringBuilder sbLabelerCity = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_CITY);
            sb.append(sbLabelerCity.toString()).append("\t");  // LABELER_CITY

            StringBuilder sbLabelerState = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_STATE);
            sb.append(sbLabelerState.toString()).append("\t");  // LABELER_STATE

            StringBuilder sbLabelerZip = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_ZIP);
            sb.append(sbLabelerZip.toString()).append("\t");  // LABELER_ZIP

            StringBuilder sbLabelerCountry = getProductCompanyDetails(p, ProdTextDefaultColumns.LABELER_COUNTRY);
            sb.append(sbLabelerCountry.toString()).append("\t");  // LABELER_COUNTRY

            bw.write(sb.toString());
            bw.newLine();
        } catch (Exception ex) {
            log.error("Error exporting Product record for : " + p.id, ex);
        }
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }

    private static StringBuilder getProductProvenanceDetails(Product p, ProdTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (p.productProvenances.size() > 0) {
            for (int i = 0; i < p.productProvenances.size(); i++) {
                ProductProvenance prodProv = p.productProvenances.get(i);

                if (i > 0) {
                    if (sb.length() != 0) {
                        sb.append("|");
                    }
                }

                switch (fieldName) {
                    case PROVENANCE:
                        sb.append((prodProv.provenance != null) ? prodProv.provenance : "");
                        break;
                    case PRODUCT_STATUS:
                        sb.append((prodProv.productStatus != null) ? prodProv.productStatus : "");
                        break;
                    case PRODUCT_TYPE:
                        sb.append((prodProv.productType != null) ? prodProv.productType : "");
                        break;
                    case MARKETING_CATEGORY_NAME:
                        sb.append((prodProv.marketingCategoryName != null) ? prodProv.marketingCategoryName : "");
                        break;
                    case IS_LISTED:
                        sb.append((prodProv.isListed != null) ? prodProv.isListed : "");
                        break;
                    case PUBLIC_DOMAIN:
                        sb.append((prodProv.publicDomain != null) ? prodProv.publicDomain : "");
                        break;
                    case APPLICATION_TYPE_NUMBER:
                        sb.append((prodProv.applicationType != null) ? prodProv.applicationType + " " : "");
                        sb.append((prodProv.applicationNumber != null) ? prodProv.applicationNumber : "");
                        break;
                    default:
                        break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getProductNameDetails(Product p, ProdTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (p.productProvenances.size() > 0) {
            for (ProductProvenance prodProv : p.productProvenances) {
                for (ProductName prodName : prodProv.productNames) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    switch (fieldName) {
                        case PRODUCT_NAME:
                            sb.append((prodName.productName != null) ? prodName.productName : "(No Product Name)");
                            break;
                        case PRODUCT_NAME_TYPE:
                            sb.append((prodName.productNameType != null) ? prodName.productNameType : "(No Product Name Type)");
                            break;
                        default:
                            break;
                    }
                } // Product Names
            } // Product Provenance
        }
        return sb;
    }

    private static StringBuilder getProductCodeDetails(Product p, ProdTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (p.productProvenances.size() > 0) {
            for (ProductProvenance prodProv : p.productProvenances) {
                for (ProductCode prodCode : prodProv.productCodes) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    switch (fieldName) {
                        case PRODUCT_ID_CODE:
                            sb.append((prodCode.productCode != null) ? prodCode.productCode : "(No Product Code)");
                            break;
                        case PRODUCT_CODE_TYPE:
                            sb.append((prodCode.productCodeType != null) ? prodCode.productCodeType : "(No Product Code Type)");
                            break;
                        default:
                            break;
                    } // switch
                }
            }
        }
        return sb;
    }

    private static StringBuilder getProductCompanyDetails(Product p, ProdTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (p.productProvenances.size() > 0) {
            for (ProductProvenance prodProv : p.productProvenances) {
                for (ProductCompany prodComp : prodProv.productCompanies) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    switch (fieldName) {
                        case LABELER_NAME:
                            sb.append((prodComp.companyName != null) ? prodComp.companyName : "(No Labeler Name)");
                            break;
                        case LABELER_ADDRESS:
                            sb.append((prodComp.companyAddress != null) ? prodComp.companyAddress : "(No Labeler Address)");
                            break;
                        case LABELER_CITY:
                            sb.append((prodComp.companyCity != null) ? prodComp.companyCity : "(No Labeler City)");
                            break;
                        case LABELER_STATE:
                            sb.append((prodComp.companyState != null) ? prodComp.companyState : "(No Labeler State)");
                            break;
                        case LABELER_ZIP:
                            sb.append((prodComp.companyZip != null) ? prodComp.companyZip : "(No Labeler Zipcode)");
                            break;
                        case LABELER_COUNTRY:
                            sb.append((prodComp.companyCountry != null) ? prodComp.companyCountry : "(No Labeler Country)");
                            break;
                        case LABELER_CODE:
                            for (ProductCompanyCode prodCompCode : prodComp.productCompanyCodes) {
                                sb.append((prodCompCode.companyCode != null) ? prodCompCode.companyCode : "(No Labeler Code)");
                            }
                            break;
                        case LABELER_CODE_TYPE:
                            for (ProductCompanyCode prodCompCode : prodComp.productCompanyCodes) {
                                sb.append((prodCompCode.companyCodeType != null) ? prodCompCode.companyCodeType : "(No Labeler Code Type)");
                            }
                            break;
                        default:
                            break;
                    } // switch
                } // loop Product Companies
            } // loop Product Provenance
        }

        return sb;
    }

    private static StringBuilder getManufactureItemDetails(Product p, ProdTextDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        try {
            if (p.productManufactureItems.size() > 0) {
                for (ProductManufactureItem prodManuItem : p.productManufactureItems) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    switch (fieldName) {
                        case DOSAGE_FORM_NAME:
                            sb.append((prodManuItem.dosageForm != null) ? prodManuItem.dosageForm : "(No Dosage Form)");
                            break;
                        default:
                            break;
                    } // switch
                }
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

        return sb;
    }

    private static void getSubstanceKeyDetails(Product p, StringBuilder sb) {

        StringBuilder substanceKeySb = new StringBuilder();
        StringBuilder substanceKeyTypeSb = new StringBuilder();
        StringBuilder ingredientTypeSb = new StringBuilder();
        StringBuilder substanceNameSb = new StringBuilder();
        StringBuilder substanceApprovalIdSb = new StringBuilder();
        StringBuilder substanceActiveMoietySb = new StringBuilder();
        StringBuilder substanceActiveMoietyApprovalIdSb = new StringBuilder();

        StringBuilder substanceAverageSb = new StringBuilder();
        StringBuilder substanceLowSb = new StringBuilder();
        StringBuilder substanceHighSb = new StringBuilder();
        StringBuilder substanceUnitSb = new StringBuilder();
        StringBuilder substanceOrgNumeratorNumSb = new StringBuilder();
        StringBuilder substanceOrgNumeratorUnitSb = new StringBuilder();
        StringBuilder substanceOrgDenominatorNumSb = new StringBuilder();
        StringBuilder substanceOrgDenominatorUnitSb = new StringBuilder();

        substanceKeySb.setLength(0);
        substanceKeyTypeSb.setLength(0);
        ingredientTypeSb.setLength(0);
        substanceNameSb.setLength(0);
        substanceApprovalIdSb.setLength(0);
        substanceActiveMoietySb.setLength(0);
        substanceActiveMoietyApprovalIdSb.setLength(0);
        substanceAverageSb.setLength(0);
        substanceLowSb.setLength(0);
        substanceHighSb.setLength(0);
        substanceUnitSb.setLength(0);
        substanceOrgNumeratorNumSb.setLength(0);
        substanceOrgNumeratorUnitSb.setLength(0);
        substanceOrgDenominatorNumSb.setLength(0);
        substanceOrgDenominatorUnitSb.setLength(0);

        try {
            if (p.productManufactureItems.size() > 0) {

                ProductManufactureItem prodManuItem = p.productManufactureItems.get(manufactureItemIndex);

                if (prodManuItem != null) {
                    if (prodManuItem.productLots.size() > 0) {
                        ProductLot prodLot = prodManuItem.productLots.get(lotIndex);

                        if (prodLot != null) {
                            if (prodLot.productIngredients.size() > 0) {
                                ProductIngredient ingred = prodLot.productIngredients.get(ingredientIndex);

                                if (ingred != null) {
                                    // Get Substance Key, Substance Key Type, Ingredient Type
                                    substanceKeySb.append((ingred.substanceKey != null) ? ingred.substanceKey : "");
                                    substanceKeyTypeSb.append((ingred.substanceKeyType != null) ? ingred.substanceKeyType : "");
                                    ingredientTypeSb.append((ingred.ingredientType != null) ? ingred.ingredientType : "");

                                    // Strength Details
                                    substanceAverageSb.append((ingred.average != null) ? ingred.average : "");
                                    substanceLowSb.append((ingred.low != null) ? ingred.low : "");
                                    substanceHighSb.append((ingred.high != null) ? ingred.high : "");
                                    substanceUnitSb.append((ingred.unit != null) ? ingred.unit : "");

                                    substanceOrgNumeratorNumSb.append((ingred.originalNumeratorNumber != null) ? ingred.originalNumeratorNumber : "");
                                    substanceOrgNumeratorUnitSb.append((ingred.originalNumeratorUnit != null) ? ingred.originalNumeratorUnit : "");
                                    substanceOrgDenominatorNumSb.append((ingred.originalDenominatorNumber != null) ? ingred.originalDenominatorNumber : "");
                                    substanceOrgDenominatorUnitSb.append((ingred.originalDenominatorUnit != null) ? ingred.originalDenominatorUnit : "");

                                    // Get Substance Details - Substance Name, Approval ID, Active Moiety, and Active Moiety Approval ID
                                    if ((ingred.substanceKey != null) && (ingred.substanceKeyType != null)) {

                                        String subName = "";
                                        String approvalId = "";
                                        String activeMoiety = "";
                                        String activeMoietyApprovalId = "";

                                        // ENTITY MANAGER Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, UNII, BDNUM, Other keys
                                        Optional<Substance> sub = substanceApiService.getEntityManagerSubstanceBySubstanceKeyResolver(ingred.substanceKey, ingred.substanceKeyType);

                                        if (sub.isPresent()) {
                                            if (sub.get() != null) {

                                                // Get Substance Name from Substance
                                                subName = ((Substance) EntityFetcher.of(sub.get().fetchKey()).call()).getName();

                                                // SUBSTANCE NAME: Add Substance/Ingredient Name in the String Builder
                                                substanceNameSb.append((subName != null) ? subName : "");

                                                // APPROVAL ID: Storing in static variable so do not have to call the same Substance API twice just to get
                                                // approval Id.
                                                substanceApprovalIdSb.append((sub.get().approvalID != null) ? sub.get().approvalID : "");

                                                // Get Active Moiety and Active Moiety Approval ID from Substance
                                                List<Relationship> relationship = ((Substance) EntityFetcher.of(sub.get().fetchKey()).call()).getActiveMoieties();

                                                for (int z = 0; z < relationship.size(); z++) {
                                                    Relationship rel = relationship.get(z);
                                                    if (rel != null) {
                                                        if (rel.relatedSubstance != null) {
                                                            activeMoiety = rel.relatedSubstance.refPname;
                                                            activeMoietyApprovalId = rel.relatedSubstance.approvalID;
                                                        }
                                                    }
                                                }

                                                substanceActiveMoietySb.append((activeMoiety != null) ? activeMoiety : "");
                                                substanceActiveMoietyApprovalIdSb.append((activeMoietyApprovalId != null) ? activeMoietyApprovalId : "");

                                            } // if Substance is not null
                                        }

                                    } else {   // else No Substance Key and Substance Key Type exist
                                        substanceKeySb.append("");
                                        substanceKeyTypeSb.append("");
                                        ingredientTypeSb.append("");
                                        substanceNameSb.append("");
                                        substanceApprovalIdSb.append("");
                                        substanceActiveMoietySb.append("");
                                        substanceActiveMoietyApprovalIdSb.append("");
                                        substanceAverageSb.append("");
                                        substanceLowSb.append("");
                                        substanceHighSb.append("");
                                        substanceUnitSb.append("");
                                        substanceOrgNumeratorNumSb.append("");
                                        substanceOrgNumeratorUnitSb.append("");
                                        substanceOrgDenominatorNumSb.append("");
                                        substanceOrgDenominatorUnitSb.append("");
                                    }
                                }
                            } // if productIngredients.size() > 0
                        }  // prodLot exists
                    } // productLots.size() > 0
                } // if prodManuItem exists
            } // productManufactureItems.size() > 0
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sb.append(substanceNameSb.toString()).append("\t");  // SUBSTANCE_NAME

        sb.append(substanceApprovalIdSb.toString()).append("\t");  // APPROVAL_ID

        sb.append(substanceKeySb.toString()).append("\t");  // SUBSTANCE_KEY

        sb.append(substanceKeyTypeSb.toString()).append("\t");  // SUBSTANCE_KEY_TYPE

        sb.append(ingredientTypeSb.toString()).append("\t");  // INGREDIENT_TYPE

        sb.append(substanceActiveMoietySb.toString()).append("\t");  // ACTIVE_MOIETY_NAME

        sb.append(substanceActiveMoietyApprovalIdSb.toString()).append("\t");  // ACTIVE_MOIETY_APPROVAL_ID

        sb.append(substanceAverageSb.toString()).append("\t");  // AVERAGE

        sb.append(substanceLowSb.toString()).append("\t");  // LOW

        sb.append(substanceHighSb.toString()).append("\t");  // HIGH

        sb.append(substanceUnitSb.toString()).append("\t");  // UNIT

        sb.append(substanceOrgNumeratorNumSb.toString()).append("\t");  // ORIGINAL NUMERATOR NUMBER

        sb.append(substanceOrgNumeratorUnitSb.toString()).append("\t");  // ORIGINAL NUMERATOR UNIT

        sb.append(substanceOrgDenominatorNumSb.toString()).append("\t");  // ORIGINAL DENOMINATOR NUMBER

        sb.append(substanceOrgDenominatorUnitSb.toString()).append("\t");  // ORIGINAL DENOMINATOR UNIT

    }

}