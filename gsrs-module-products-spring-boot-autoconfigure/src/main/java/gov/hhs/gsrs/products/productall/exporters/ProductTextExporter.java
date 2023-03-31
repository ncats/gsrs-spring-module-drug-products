package gov.hhs.gsrs.products.productall.exporters;

import gov.hhs.gsrs.products.productall.models.*;

import ix.ginas.exporters.*;

import java.io.IOException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

enum ProdDefaultColumns implements Column {
    PRODUCT_ID,
    PRODUCT_NAME,
    NON_PROPRIETARY_NAME,
    STATUS,
    ROUTE_OF_ADMINISTRATOR,
    PROVENANCE,
    IS_LISTED,
    LABELER_NAME,
    LABELER_DUNS,
    LABELER_ADDRESS,
    LABELER_CITY,
    LABELER_STATE,
    LABELER_ZIP,
    LABELER_COUNTRY,
    ACTIVE_MOIETY_NAME,
    ACTIVE_MOIETY_UNII,
    INGREDIENT_TYPE,
    APPLICATION_NUMBER,
    DOSAGE_FORM_NAME,
    MARKETING_CATEGORY_NAME,
    PRODUCT_TYPE,
    INGREDIENT_NUMBER,
    SUBSTANCE_NAME,
    APPROVAL_ID,
    SUBSTANCE_KEY
}

@Slf4j
public class ProductTextExporter implements Exporter<ProductMainAll> {

    private final BufferedWriter bw;

    private int row = 1;
    private static int ingredientNumber = 0;

    public ProductTextExporter(OutputStream os, ExporterFactory.Parameters params) throws IOException {
        bw = new BufferedWriter(new OutputStreamWriter(os));
        StringBuilder sb = new StringBuilder();

        sb.append("INGREDIENT_NUMBER").append("\t")
                .append("SUBSTANCE_NAME").append("\t")
                .append("APPROVAL_ID").append("\t")
                .append("SUBSTANCE_KEY").append("\t")
                .append("INGREDIENT_TYPE").append("\t")
                .append("ACTIVE_MOIETY_NAME").append("\t")
                .append("ACTIVE_MOIETY_UNII").append("\t")
                .append("PRODUCT_ID").append("\t")
                .append("PRODUCT_NAME").append("\t")
                .append("NON_PROPRIETARY_NAME").append("\t")
                .append("STATUS").append("\t")
                .append("PRODUCT_TYPE").append("\t")
                .append("ROUTE_OF_ADMINISTRATOR").append("\t")
                .append("DOSAGE_FORM_NAME").append("\t")
                .append("MARKETING_CATEGORY_NAME").append("\t")
                .append("APPLICATION_NUMBER").append("\t")
                .append("IS_LISTED").append("\t")
                .append("LABELER_NAME").append("\t")
                .append("LABELER_DUNS").append("\t")
                .append("LABELER_ADDRESS").append("\t")
                .append("LABELER_CITY").append("\t")
                .append("LABELER_STATE").append("\t")
                .append("LABELER_ZIP").append("\t")
                .append("LABELER_COUNTRY").append("\t")
                .append("PROVENANCE").append("\t");

        bw.write(sb.toString());
        bw.newLine();
    }

    @Override
    public void export(ProductMainAll s) throws IOException {
        /*****************************************************************************/
        // Export Product records and also display all the ingredients in each row
        /****************************************************************************/
        try {
            // Add one more column called "Ingredient Number" at the beginning.  Have it increment by one.
            // Each of these ingredients be new rows. Can duplicate the other product columns on each row.
            if (s.productIngredientAllList.size() > 0) {
                for (int i = 0; i < s.productIngredientAllList.size(); i++) {

                    this.ingredientNumber = i;
                    int ingredNum = ingredientNumber + 1;

                    StringBuilder sb = new StringBuilder();

                    sb.append(ingredNum).append("\t");  // INGREDIENT_NUMBER

                    StringBuilder sbName = getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_NAME);
                    sb.append(sbName.toString()).append("\t");  // SUBSTANCE_NAME

                    StringBuilder sbApprovalId = getIngredientDetails(s,ProdDefaultColumns.APPROVAL_ID);
                    sb.append(sbApprovalId.toString()).append("\t");  // APPROVAL_ID

                    StringBuilder sbSubKey = getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_KEY);
                    sb.append(sbSubKey.toString()).append("\t");  // SUBSTANCE_KEY

                    StringBuilder sbIngType = getIngredientDetails(s,ProdDefaultColumns.INGREDIENT_TYPE);
                    sb.append(sbIngType.toString()).append("\t");  // INGREDIENT_TYPE

                    StringBuilder sbActMoietyName = getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_NAME);
                    sb.append(sbActMoietyName.toString()).append("\t");  // ACTIVE_MOIETY_NAME

                    StringBuilder sbActMoietyUnii = getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_UNII);
                    sb.append(sbActMoietyUnii.toString()).append("\t");  // ACTIVE_MOIETY_UNII

                    sb.append((s.productNDC != null) ? s.productNDC : "").append("\t"); // PRODUCT_ID

                    StringBuilder sbProdName = getProductNameDetails(s,ProdDefaultColumns.PRODUCT_NAME);
                    sb.append(sbProdName.toString()).append("\t");  // PRODUCT_NAME

                    sb.append((s.nonProprietaryName != null) ? s.nonProprietaryName : "").append("\t");  // NON_PROPRIETARY_NAME

                    sb.append((s.status != null) ? s.status : "").append("\t"); // STATUS

                    sb.append((s.productType != null) ? s.productType : "").append("\t");  // PRODUCT_TYPE

                    sb.append((s.routeName != null) ? s.routeName : "").append("\t");  // ROUTE_OF_ADMINISTRATOR

                    StringBuilder sbDosageForm = getIngredientDetails(s,ProdDefaultColumns.DOSAGE_FORM_NAME);
                    sb.append(sbDosageForm.toString()).append("\t");  // DOSAGE_FORM_NAME

                    sb.append((s.marketingCategoryName != null) ? s.marketingCategoryName : "").append("\t");  // MARKETING_CATEGORY_NAME

                    sb.append((s.appTypeNumber != null) ? s.appTypeNumber : "").append("\t");  // APPLICATION_NUMBER

                    sb.append((s.isListed != null) ? s.isListed : "").append("\t");  // IS_LISTED

                    StringBuilder sbLabelerName = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_NAME);
                    sb.append(sbLabelerName.toString()).append("\t");  // LABELER_NAME

                    StringBuilder sbLabelerDuns = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_DUNS);
                    sb.append(sbLabelerDuns.toString()).append("\t");  // LABELER_DUNS

                    StringBuilder sbLabelerAdd = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ADDRESS);
                    sb.append(sbLabelerAdd.toString()).append("\t");  // LABELER_ADDRESS

                    StringBuilder sbLabelerCity = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_CITY);
                    sb.append(sbLabelerCity.toString()).append("\t");  // LABELER_CITY

                    StringBuilder sbLabelerState = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_STATE);
                    sb.append(sbLabelerState.toString()).append("\t");  // LABELER_STATE

                    StringBuilder sbLabelerZip = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ZIP);
                    sb.append(sbLabelerZip.toString()).append("\t");  // LABELER_ZIP

                    StringBuilder sbLabelerCountry = getProductCompanyDetails(s,ProdDefaultColumns.LABELER_COUNTRY);
                    sb.append(sbLabelerCountry.toString()).append("\t");  // LABELER_COUNTRY

                    sb.append((s.provenance != null) ? s.provenance : "").append("\t");  // PROVENANCE

                    bw.write(sb.toString());
                    bw.newLine();

                    /*
                    Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);
                    int col = 0;
                    this.ingredientNumber = i;

                    for (ColumnValueRecipe<ProductMainAll> recipe : recipeMap) {
                        col += recipe.writeValuesFor(row, col, s);
                    } */
                } // for ProductIngredient
            } // Ingredient size > 0
        } // try
        catch (
                Exception ex) {
            log.error("Error exporting Product record in Substance for Product ID: " + s.productId, ex);
        }
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }

    /*
    private static Map<Column, ColumnValueRecipe<ProductMainAll>> DEFAULT_RECIPE_MAP;

        static {

        DEFAULT_RECIPE_MAP=new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.INGREDIENT_NUMBER,SingleColumnValueRecipe.create(ProdDefaultColumns.INGREDIENT_NUMBER,(s,cell)->{
        int ingredNum=ingredientNumber+1;
        cell.writeInteger((ingredNum));
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_NAME,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_NAME);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPROVAL_ID,SingleColumnValueRecipe.create(ProdDefaultColumns.APPROVAL_ID,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.APPROVAL_ID);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.SUBSTANCE_KEY,SingleColumnValueRecipe.create(ProdDefaultColumns.SUBSTANCE_KEY,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.SUBSTANCE_KEY);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.INGREDIENT_TYPE,SingleColumnValueRecipe.create(ProdDefaultColumns.INGREDIENT_TYPE,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.INGREDIENT_TYPE);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_NAME,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_NAME);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ACTIVE_MOIETY_UNII,SingleColumnValueRecipe.create(ProdDefaultColumns.ACTIVE_MOIETY_UNII,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.ACTIVE_MOIETY_UNII);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_ID,SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_ID,(s,cell)->cell.writeString(s.productNDC)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_NAME,(s,cell)->{
        StringBuilder sb=getProductNameDetails(s,ProdDefaultColumns.PRODUCT_NAME);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.NON_PROPRIETARY_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.NON_PROPRIETARY_NAME,(s,cell)->cell.writeString(s.nonProprietaryName)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.STATUS,SingleColumnValueRecipe.create(ProdDefaultColumns.STATUS,(s,cell)->cell.writeString(s.status)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PRODUCT_TYPE,SingleColumnValueRecipe.create(ProdDefaultColumns.PRODUCT_TYPE,(s,cell)->cell.writeString(s.productType)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR,SingleColumnValueRecipe.create(ProdDefaultColumns.ROUTE_OF_ADMINISTRATOR,(s,cell)->cell.writeString(s.routeName)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.DOSAGE_FORM_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.DOSAGE_FORM_NAME,(s,cell)->{
        StringBuilder sb=getIngredientDetails(s,ProdDefaultColumns.DOSAGE_FORM_NAME);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.MARKETING_CATEGORY_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.MARKETING_CATEGORY_NAME,(s,cell)->cell.writeString(s.marketingCategoryName)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.APPLICATION_NUMBER,SingleColumnValueRecipe.create(ProdDefaultColumns.APPLICATION_NUMBER,(s,cell)->cell.writeString(s.appTypeNumber)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.IS_LISTED,SingleColumnValueRecipe.create(ProdDefaultColumns.IS_LISTED,(s,cell)->cell.writeString(s.isListed)));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_NAME,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_NAME,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_NAME);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_DUNS,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_DUNS,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_DUNS);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ADDRESS,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ADDRESS,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ADDRESS);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_CITY,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_CITY,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_CITY);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_STATE,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_STATE,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_STATE);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_ZIP,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_ZIP,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_ZIP);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.LABELER_COUNTRY,SingleColumnValueRecipe.create(ProdDefaultColumns.LABELER_COUNTRY,(s,cell)->{
        StringBuilder sb=getProductCompanyDetails(s,ProdDefaultColumns.LABELER_COUNTRY);
        cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdDefaultColumns.PROVENANCE,SingleColumnValueRecipe.create(ProdDefaultColumns.PROVENANCE,(s,cell)->cell.writeString(s.provenance)));

        }
        */

    private static StringBuilder getProductNameDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (s.productNameAllList.size() > 0) {

            for (ProductNameAll prodName : s.productNameAllList) {
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
            }
        }
        return sb;
    }

    private static StringBuilder getProductCompanyDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if (s.productCompanyAllList.size() > 0) {

            for (ProductCompanyAll prodComp : s.productCompanyAllList) {
                if (sb.length() != 0) {
                    sb.append("|");
                }
                switch (fieldName) {
                    case LABELER_NAME:
                        sb.append((prodComp.labelerName != null) ? prodComp.labelerName : "");
                        break;
                    case LABELER_DUNS:
                        sb.append((prodComp.labelerDuns != null) ? prodComp.labelerDuns : "");
                        break;
                    case LABELER_ADDRESS:
                        sb.append((prodComp.address != null) ? prodComp.address : "");
                        break;
                    case LABELER_CITY:
                        sb.append((prodComp.city != null) ? prodComp.city : "");
                        break;
                    case LABELER_STATE:
                        sb.append((prodComp.state != null) ? prodComp.state : "");
                        break;
                    case LABELER_ZIP:
                        sb.append((prodComp.zip != null) ? prodComp.zip : "");
                        break;
                    case LABELER_COUNTRY:
                        sb.append((s.countryWithoutCode != null) ? s.countryWithoutCode : "");
                        break;

                    default:
                        break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getIngredientDetails(ProductMainAll s, ProdDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        try {
            if (s.productIngredientAllList.size() > 0) {

                ProductIngredientAll ingred = s.productIngredientAllList.get(ingredientNumber);

                switch (fieldName) {
                    case SUBSTANCE_NAME:
                        sb.append((ingred.substanceName != null) ? ingred.substanceName : "");
                        break;
                    case APPROVAL_ID:
                        sb.append((ingred.substanceApprovalId != null) ? ingred.substanceApprovalId : "");
                        break;
                    case SUBSTANCE_KEY:
                        sb.append((ingred.substanceKey != null) ? ingred.substanceKey : "");
                        break;
                    case INGREDIENT_TYPE:
                        sb.append((ingred.ingredientType != null) ? ingred.ingredientType : "");
                        break;
                    case ACTIVE_MOIETY_NAME:
                        sb.append((ingred.activeMoietyName != null) ? ingred.activeMoietyName : "");
                        break;
                    case ACTIVE_MOIETY_UNII:
                        sb.append((ingred.activeMoietyUnii != null) ? ingred.activeMoietyUnii : "");
                        break;
                    case DOSAGE_FORM_NAME:
                        sb.append((ingred.dosageFormName != null) ? ingred.dosageFormName : "");
                        break;
                    default:
                        break;
                }
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }

        return sb;
    }

        /*
/**
 * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
 * but these may be modified using the add/remove column methods.
 */
/*
public static class Builder {
    private final List<ColumnValueRecipe<ProductMainAll>> columns = new ArrayList<>();
    private final Spreadsheet spreadsheet;

    private boolean publicOnly = false;

    public Builder(Spreadsheet spreadSheet) {
        Objects.requireNonNull(spreadSheet);
        this.spreadsheet = spreadSheet;

        for (Map.Entry<Column, ColumnValueRecipe<ProductMainAll>> entry : DEFAULT_RECIPE_MAP.entrySet()) {
            columns.add(entry.getValue());
        }
    }

    public Builder addColumn(Column column, ColumnValueRecipe<ProductMainAll> recipe) {
        return addColumn(column.name(), recipe);
    }

    public Builder addColumn(String columnName, ColumnValueRecipe<ProductMainAll> recipe) {
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
        ListIterator<ColumnValueRecipe<ProductMainAll>> iter = columns.listIterator();
        while (iter.hasNext()) {

            ColumnValueRecipe<ProductMainAll> oldValue = iter.next();
            ColumnValueRecipe<ProductMainAll> newValue = oldValue.replaceColumnName(oldName, newName);
            if (oldValue != newValue) {
                iter.set(newValue);
            }
        }
        return this;
    }

    public ProductTextExporter build() {
        return new ProductTextExporter(this);
    }

    public Builder includePublicDataOnly(boolean publicOnly) {
        this.publicOnly = publicOnly;
        return this;
    }

}
*/
}