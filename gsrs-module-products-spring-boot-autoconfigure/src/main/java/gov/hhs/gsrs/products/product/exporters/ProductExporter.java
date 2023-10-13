package gov.hhs.gsrs.products.product.exporters;

import gov.hhs.gsrs.products.product.models.*;

import ix.core.EntityFetcher;
import ix.ginas.exporters.*;
import ix.ginas.models.v1.Substance;

import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.Query;

enum ProdDefaultColumns implements Column {
    INGREDIENT_NUMBER,
    SUBSTANCE_NAME,
    APPROVAL_ID,
    SUBSTANCE_KEY,
    INGREDIENT_TYPE,
    ACTIVE_MOIETY_NAME,
    ACTIVE_MOIETY_UNII,
    PROVENANCE,
    PRODUCT_ID,
    PRODUCT_CODE_TYPE,
    PRODUCT_NAME,
    PRODUCT_STATUS,
    PRODUCT_TYPE,
    ROUTE_OF_ADMINISTRATOR,
    DOSAGE_FORM_NAME,
    MARKETING_CATEGORY_NAME,
    APPLICATION_TYPE_NUMBER,
    IS_LISTED,
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

    static final String CONST_ACTIVE_MOIETY = "ACTIVE MOIETY";

    private final Spreadsheet spreadsheet;
    private static EntityManager entityManager;

    private int row = 1;
    private static int ingredientNumber = 0;

    private final List<ColumnValueRecipe<Product>> recipeMap;

    private ProductExporter(Builder builder, EntityManager entityManager) {

        this.entityManager = entityManager;

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

            if (p.productManufactureItems.size() > 0) {
                for (ProductManufactureItem prodManuItem : p.productManufactureItems) {
                    for (ProductLot prodLot : prodManuItem.productLots) {
                        //     for (productIngredient prodIngred : prodLot.productIngredients) {

                        //        if (s.productIngredientAllList.size() > 0) {
                        for (int i = 0; i < prodLot.productIngredients.size(); i++) {

                            Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);
                            int col = 0;
                            this.ingredientNumber = i;

                            for (ColumnValueRecipe<Product> recipe : recipeMap) {
                                col += recipe.writeValuesFor(row, col, p);
                            }

                        } // loop ProductIngredient
                    }  // loop ProductLot
                } // loop ProductManufacutureItem
            } // Ingredient size > 0
        } // try
        catch (Exception ex) {
            log.error("Error exporting Product record for Product ID: " + p.id, ex);
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
            int ingredNum = ingredientNumber + 1;
            cell.writeInteger((ingredNum));
        }));

        // Get Substance Name, Approval ID (UNII), Active Moiety, Substance Key, Ingredient Type
        getSubstanceKeyDetails();

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PROVENANCE, SingleColumnValueRecipe.create(ProdDefaultColumns.PROVENANCE, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.PROVENANCE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_ID, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_ID, (p, cell) -> {
            StringBuilder sb = getProductCodeDetails(p, ProdDefaultColumns.PRODUCT_ID);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_CODE_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_CODE_TYPE, (p, cell) -> {
            StringBuilder sb = getProductCodeDetails(p, ProdDefaultColumns.PRODUCT_CODE_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_NAME, (s, cell) -> {
            StringBuilder sb = getProductNameDetails(s, ProdDefaultColumns.PRODUCT_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_STATUS, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_STATUS, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.PRODUCT_STATUS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_TYPE, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.PRODUCT_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR, SingleColumnValueRecipe.create(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR, (p, cell) -> cell.writeString(p.routeAdmin)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.DOSAGE_FORM_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.DOSAGE_FORM_NAME, (s, cell) -> {
            StringBuilder sb = getManufactureItemDetails(s, ProdDefaultColumns.DOSAGE_FORM_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.MARKETING_CATEGORY_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.MARKETING_CATEGORY_NAME, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.MARKETING_CATEGORY_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPLICATION_TYPE_NUMBER, SingleColumnValueRecipe.create(ProdDefaultColumns.APPLICATION_TYPE_NUMBER, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.APPLICATION_TYPE_NUMBER);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.IS_LISTED, SingleColumnValueRecipe.create(ProdDefaultColumns.IS_LISTED, (s, cell) -> {
            StringBuilder sb = getProductProvenanceDetails(s, ProdDefaultColumns.IS_LISTED);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_NAME, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CODE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CODE, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_CODE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CODE_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CODE_TYPE, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_CODE_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ADDRESS, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ADDRESS, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_ADDRESS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CITY, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CITY, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_CITY);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_STATE, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_STATE, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_STATE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ZIP, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ZIP, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_ZIP);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_COUNTRY, SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_COUNTRY, (s, cell) -> {
            StringBuilder sb = getProductCompanyDetails(s, ProdDefaultColumns.LABELER_COUNTRY);
            cell.writeString(sb.toString());
        }));
    }

    private static StringBuilder getProductProvenanceDetails(Product p, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (p.productProvenances.size() > 0) {
            for (ProductProvenance prodProv : p.productProvenances) {

                if (sb.length() != 0) {
                    sb.append("|");
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
                    case APPLICATION_TYPE_NUMBER:
                        sb.append((prodProv.applicationType != null) ? prodProv.applicationType + " ": "");
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
                        case PRODUCT_ID:
                            sb.append((prodCode.productCode != null) ? prodCode.productCode : "");
                            break;
                        case PRODUCT_CODE_TYPE:
                            sb.append((prodCode.productCodeType != null) ? prodCode.productCodeType : "");
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
                            sb.append((prodComp.companyName != null) ? prodComp.companyName : "");
                            break;
                        case LABELER_ADDRESS:
                            sb.append((prodComp.companyAddress != null) ? prodComp.companyAddress : "");
                            break;
                        case LABELER_CITY:
                            sb.append((prodComp.companyCity != null) ? prodComp.companyCity : "");
                            break;
                        case LABELER_STATE:
                            sb.append((prodComp.companyState != null) ? prodComp.companyState : "");
                            break;
                        case LABELER_ZIP:
                            sb.append((prodComp.companyZip != null) ? prodComp.companyZip : "");
                            break;
                        case LABELER_COUNTRY:
                            sb.append((prodComp.companyCountry != null) ? prodComp.companyCountry : "");
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
                    switch (fieldName) {
                        case DOSAGE_FORM_NAME:
                            sb.append((prodManuItem.dosageForm != null) ? prodManuItem.dosageForm : "");
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

        StringBuilder nameSb = new StringBuilder();
        StringBuilder approvalIdSb = new StringBuilder();
        StringBuilder substanceKeySb = new StringBuilder();
        StringBuilder ingredientTypeSb = new StringBuilder();
        StringBuilder substanceNameSb = new StringBuilder();
        StringBuilder substanceApprovalIdSb = new StringBuilder();
        StringBuilder substanceActiveMoietySb = new StringBuilder();
        StringBuilder substanceActiveMoietyApprovalIdSb = new StringBuilder();

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_NAME, (p, cell) -> {

            nameSb.setLength(0);
            approvalIdSb.setLength(0);
            substanceKeySb.setLength(0);
            ingredientTypeSb.setLength(0);
            substanceNameSb.setLength(0);
            substanceApprovalIdSb.setLength(0);
            substanceActiveMoietySb.setLength(0);

        try {
            if (p.productManufactureItems.size() > 0) {
                for (int i = 0; i < p.productManufactureItems.size(); i++) {
                    ProductManufactureItem prodManuItem = p.productManufactureItems.get(i);
                    for (int j = 0; j < prodManuItem.productLots.size(); j++) {
                        ProductLot prodLot = prodManuItem.productLots.get(j);

                        ProductIngredient ingred = prodLot.productIngredients.get(ingredientNumber);

                        // Get Substance Details
                        if (ingred.substanceKey != null) {

                            substanceKeySb.append((ingred.substanceKey != null) ? ingred.substanceKey : "");
                            ingredientTypeSb.append((ingred.ingredientType != null) ? ingred.ingredientType : "");

                            //TODO: replace with SubstanceKeyResolver for this later
                            //Get Substance Object by Substance Key
                          //  Substance s = substanceKeyResolver.substanceEMResolver(substanckey, substanceKeyType);
                            //or else null
                            Query query = entityManager.createQuery("SELECT s FROM Substance s JOIN s.codes c WHERE c.type = 'PRIMARY' and c.code=:subKey");
                            query.setParameter("subKey", ingred.substanceKey);
                            Substance sub = (Substance) query.getSingleResult();

                            if (sub != null) {

                                nameSb.append(((Substance) EntityFetcher.of(sub.fetchKey()).call()).getName());
                                approvalIdSb.append(sub.approvalID);

                                // Get Substance Name
                                substanceNameSb.append((nameSb.toString() != null) ? nameSb.toString() : "");

                                // Storing in static variable so do not have to call the same Substance API twice just to get
                                // approval Id.
                                substanceApprovalIdSb.append((approvalIdSb.toString() != null) ? approvalIdSb.toString() : "");

                                if (sub.getActiveMoieties().size() > 0) {
                                    sub.getActiveMoieties().forEach(relationship -> {
                                        if ((relationship.type != null) && (relationship.type.equalsIgnoreCase(CONST_ACTIVE_MOIETY))) {
                                            if (relationship.relatedSubstance != null) {
                                                substanceActiveMoietySb.append(relationship.relatedSubstance.refPname != null ? relationship.relatedSubstance.refPname : "");
                                                substanceActiveMoietyApprovalIdSb.append(relationship.relatedSubstance.approvalID != null ? relationship.relatedSubstance.approvalID : "");
                                            }
                                        }
                                    });
                                }
                            }

                        } else {   // No Substance Key
                            substanceKeySb.append("");
                            substanceNameSb.append("");
                            substanceApprovalIdSb.append("");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

            cell.writeString(substanceNameSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPROVAL_ID, SingleColumnValueRecipe.create(ProdDefaultColumns.APPROVAL_ID, (p, cell) -> {
            cell.writeString(substanceApprovalIdSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_KEY, SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_KEY, (p, cell) -> {
            cell.writeString(substanceKeySb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.INGREDIENT_TYPE, SingleColumnValueRecipe.create(ProdDefaultColumns.INGREDIENT_TYPE, (p, cell) -> {
            cell.writeString(ingredientTypeSb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_NAME, SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_NAME, (s, cell) -> {
            cell.writeString(substanceActiveMoietySb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_UNII, SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_UNII, (s, cell) -> {
            cell.writeString(substanceActiveMoietyApprovalIdSb.toString());
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

        public ProductExporter build(EntityManager entityManager) {
            return new ProductExporter(this, entityManager);
        }

        public Builder includePublicDataOnly(boolean publicOnly) {
            this.publicOnly = publicOnly;
            return this;
        }

    }
}