package gov.hhs.gsrs.products.product.exporters;

import gov.hhs.gsrs.products.product.models.*;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;

import ix.core.EntityFetcher;
import ix.ginas.exporters.*;
import ix.ginas.models.v1.Substance;
import ix.ginas.models.v1.Relationship;
import ix.ginas.models.v1.SubstanceReference;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;

enum ProdDefaultColumns implements Column {
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
public class ProductExporter implements Exporter<Product> {

    private static SubstanceApiService substanceApiService;

    private final Spreadsheet spreadsheet;

    private int row = 1;
    private static int ingredientNumber = 0;
    private static int manufactureItemIndex = 0;
    private static int lotIndex = 0;
    private static int ingredientIndex = 0;

    private final List<ColumnValueRecipe<Product>> recipeMap;

    private ProductExporter(Builder builder, SubstanceApiService substanceApiService) {

        // Substance API Service
        this.substanceApiService = substanceApiService;

        this.spreadsheet = builder.spreadsheet;
        this.recipeMap = builder.columns;

        int j = 0;
        Spreadsheet.SpreadsheetRow header = spreadsheet.getRow(0);
        for (ColumnValueRecipe<Product> col : recipeMap) {
            j += col.writeHeaderValues(header, j);
        }
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
            this.lotIndex = 0;
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

                                Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);
                                this.ingredientIndex = k;

                                this.ingredientNumber++;

                                writeDataInFile(p, row, col);

                            } // loop productIngredients
                        } // productIngredients.size() > 0
                        else {
                            // if there is no Ingredient record
                            Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);

                            this.ingredientNumber++;

                            writeDataInFile(p, row, col);
                        }
                    } // loop productLots
                } // loop productManufactureItems
            } // if productManufactureItems.size() > 0
            else {
                Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);
                writeDataInFile(p, row, col);
            }

        } // try
        catch (Exception ex) {
            log.error("Error exporting Product record for Product ID: " + p.id, ex);
        }
    }

    public void writeDataInFile(Product p, Spreadsheet.SpreadsheetRow row, int col) throws IOException {
        try {
            for (ColumnValueRecipe<Product> recipe : recipeMap) {
                col += recipe.writeValuesFor(row, col, p);
            }
        } catch (Exception ex) {
            log.error("Error writing Data in File when importing Product record for Product ID: " + p.id, ex);
        }
    }

    @Override
    public void close() throws IOException {
        spreadsheet.close();
    }

    private static Map<Column, ColumnValueRecipe<Product>> DEFAULT_RECIPE_MAP;

    static {

        DEFAULT_RECIPE_MAP = new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.INGREDIENT_NUMBER, SingleColumnValueRecipe.create(ProdDefaultColumns.INGREDIENT_NUMBER, (p, cell) -> {
            int ingredNum = ingredientNumber;
            cell.writeInteger((ingredNum));
        }));

        // Get Substance Name, Approval ID (UNII), Active Moiety, Substance Key, Substance Key Type, Ingredient Type
        getSubstanceKeyDetails();

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_ID_CODE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_ID_CODE, (p, cell) -> {
            StringBuilder sb = getProductCodeDetails(p, ProdDefaultColumns.PRODUCT_ID_CODE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_CODE_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_CODE_TYPE, (p, cell) -> {
            StringBuilder sb = getProductCodeDetails(p, ProdDefaultColumns.PRODUCT_CODE_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_NAME, (p, cell) -> {
            StringBuilder sb = getProductNameDetails(p, ProdDefaultColumns.PRODUCT_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_NAME_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_NAME_TYPE, (p, cell) -> {
            StringBuilder sb = getProductNameDetails(p, ProdDefaultColumns.PRODUCT_NAME_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PROVENANCE, SingleColumnValueRecipe.create(ProdDefaultColumns.PROVENANCE, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.PROVENANCE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_STATUS, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_STATUS, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.PRODUCT_STATUS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_TYPE, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.PRODUCT_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPLICATION_TYPE_NUMBER, SingleColumnValueRecipe.create(ProdDefaultColumns.APPLICATION_TYPE_NUMBER, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.APPLICATION_TYPE_NUMBER);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.MARKETING_CATEGORY_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.MARKETING_CATEGORY_NAME, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.MARKETING_CATEGORY_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.IS_LISTED, SingleColumnValueRecipe.create(ProdDefaultColumns.IS_LISTED, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.IS_LISTED);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PUBLIC_DOMAIN, SingleColumnValueRecipe.create(ProdDefaultColumns.PUBLIC_DOMAIN, (p, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(p, ProdDefaultColumns.PUBLIC_DOMAIN);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR, SingleColumnValueRecipe.create(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR, (p, cell) -> cell.writeString(p.routeAdmin)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.DOSAGE_FORM_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.DOSAGE_FORM_NAME, (p, cell) -> {
            StringBuilder sb = getManufactureItemDetails(p, ProdDefaultColumns.DOSAGE_FORM_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_NAME, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CODE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CODE, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_CODE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CODE_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CODE_TYPE, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_CODE_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ADDRESS, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ADDRESS, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_ADDRESS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CITY, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CITY, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_CITY);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_STATE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_STATE, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_STATE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ZIP, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ZIP, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_ZIP);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_COUNTRY, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_COUNTRY, (p, cell) -> {
            StringBuilder sb = getProductCompanyDetails(p, ProdDefaultColumns.LABELER_COUNTRY);
            cell.writeString(sb.toString());
        }));
    } // static

    private static StringBuilder getProductProvenanceDetails(Product p, ProdDefaultColumns fieldName) {
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

    private static StringBuilder getProductNameDetails(Product p, ProdDefaultColumns fieldName) {
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

    private static StringBuilder getProductCodeDetails(Product p, ProdDefaultColumns fieldName) {
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

    private static StringBuilder getProductCompanyDetails(Product p, ProdDefaultColumns fieldName) {
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

    private static StringBuilder getManufactureItemDetails(Product p, ProdDefaultColumns fieldName) {
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

    private static void getSubstanceKeyDetails() {

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

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_NAME, (p, cell) -> {

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

                                            // ENTITY MANAGER Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, BDNUM, Other keys
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

                                                } // if Substance found in Substance Module and is not null
                                        }
                                        } // if substance key exists

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
                                } // if productIngredients.size() > 0
                            }  // prodLot exists
                        } // productLots.size() > 0
                    } // if prodManuItem exists
                } // productManufactureItems.size() > 0
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Store Substance Name in Excel
            cell.writeString(substanceNameSb.toString());

        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPROVAL_ID, SingleColumnValueRecipe.create(ProdDefaultColumns.APPROVAL_ID, (p, cell) -> {
            cell.writeString(substanceApprovalIdSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_KEY, SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_KEY, (p, cell) -> {
            cell.writeString(substanceKeySb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_KEY_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_KEY_TYPE, (p, cell) -> {
            cell.writeString(substanceKeyTypeSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.INGREDIENT_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.INGREDIENT_TYPE, (p, cell) -> {
            cell.writeString(ingredientTypeSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_NAME, (p, cell) -> {
            cell.writeString(substanceActiveMoietySb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_APPROVAL_ID, SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_APPROVAL_ID, (p, cell) -> {
            cell.writeString(substanceActiveMoietyApprovalIdSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.AVERAGE, SingleColumnValueRecipe.create(ProdDefaultColumns.AVERAGE, (p, cell) -> {
            cell.writeString(substanceAverageSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LOW, SingleColumnValueRecipe.create(ProdDefaultColumns.LOW, (p, cell) -> {
            cell.writeString(substanceLowSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.HIGH, SingleColumnValueRecipe.create(ProdDefaultColumns.HIGH, (p, cell) -> {
            cell.writeString(substanceHighSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.UNIT, SingleColumnValueRecipe.create(ProdDefaultColumns.UNIT, (p, cell) -> {
            cell.writeString(substanceUnitSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ORIGINAL_NUMERATOR_NUMBER, SingleColumnValueRecipe.create(ProdDefaultColumns.ORIGINAL_NUMERATOR_NUMBER, (p, cell) -> {
            cell.writeString(substanceOrgNumeratorNumSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ORIGINAL_NUMERATOR_UNIT, SingleColumnValueRecipe.create(ProdDefaultColumns.ORIGINAL_NUMERATOR_UNIT, (p, cell) -> {
            cell.writeString(substanceOrgNumeratorUnitSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ORIGINAL_DENOMINATOR_NUMBER, SingleColumnValueRecipe.create(ProdDefaultColumns.ORIGINAL_DENOMINATOR_NUMBER, (p, cell) -> {
            cell.writeString(substanceOrgDenominatorNumSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ORIGINAL_DENOMINATOR_UNIT, SingleColumnValueRecipe.create(ProdDefaultColumns.ORIGINAL_DENOMINATOR_UNIT, (p, cell) -> {
            cell.writeString(substanceOrgDenominatorUnitSb.toString());
        }));
    }

    /**
     * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
     * but these may be modified using the add/remove column methods.
     */
    public static class Builder {
        private final List<ColumnValueRecipe<Product>> columns = new ArrayList<>();
        private final Spreadsheet spreadsheet;

        private boolean publicOnly = false;

        /**
         * Create a new Builder that uses the given Spreadsheet to write to.
         *
         * @param spreadSheet the {@link Spreadsheet} object that will be written to by this exporter. can not be null.
         * @throws NullPointerException if spreadsheet is null.
         */
        public Builder(Spreadsheet spreadSheet) {
            Objects.requireNonNull(spreadSheet);
            this.spreadsheet = spreadSheet;

            for (Map.Entry<Column, ColumnValueRecipe<Product>> entry : DEFAULT_RECIPE_MAP.entrySet()) {
                columns.add(entry.getValue());
            }
        }

        public Builder addColumn(Column column, ColumnValueRecipe<Product> recipe) {
            return addColumn(column.name(), recipe);
        }

        public Builder addColumn(String columnName, ColumnValueRecipe<Product> recipe) {
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(recipe);
            columns.add(recipe);

            return this;
        }

        public Builder renameColumn(Column oldColumn, String newName) {
            return renameColumn(oldColumn.name(), newName);
        }

        public Builder renameColumn(String oldName, String newName) {
            //use iterator to preserve order
            ListIterator<ColumnValueRecipe<Product>> iter = columns.listIterator();
            while (iter.hasNext()) {

                ColumnValueRecipe<Product> oldValue = iter.next();
                ColumnValueRecipe<Product> newValue = oldValue.replaceColumnName(oldName, newName);
                if (oldValue != newValue) {
                    iter.set(newValue);
                }
            }
            return this;
        }

        public ProductExporter build(SubstanceApiService substanceApiService) {
            return new ProductExporter(this, substanceApiService);
        }

        public Builder includePublicDataOnly(boolean publicOnly) {
            this.publicOnly = publicOnly;
            return this;
        }

    }
}