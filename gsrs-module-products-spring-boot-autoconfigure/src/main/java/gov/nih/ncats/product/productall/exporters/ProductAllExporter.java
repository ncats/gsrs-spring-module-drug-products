package gov.nih.ncats.product.productall.exporters;

import gov.nih.ncats.product.productall.models.*;
import ix.ginas.exporters.*;

import java.io.IOException;
import java.util.*;

enum ProdAllDefaultColumns implements Column {
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
    SUBSTANCE_NAME,
    APPROVAL_ID,
    SUBSTANCE_KEY
}

public class ProductAllExporter implements Exporter<ProductMainAll> {

    private final Spreadsheet spreadsheet;

    private int row=1;

    private final List<ColumnValueRecipe<ProductMainAll>> recipeMap;

    private ProductAllExporter(Builder builder){
        this.spreadsheet = builder.spreadsheet;
        this.recipeMap = builder.columns;
        
        int j=0;
        Spreadsheet.SpreadsheetRow header = spreadsheet.getRow(0);
        for(ColumnValueRecipe<ProductMainAll> col : recipeMap){
            j+= col.writeHeaderValues(header, j);
       }
    } 
    @Override
    public void export(ProductMainAll s) throws IOException {
        Spreadsheet.SpreadsheetRow row = spreadsheet.getRow( this.row++);

        int j=0;
        for(ColumnValueRecipe<ProductMainAll> recipe : recipeMap){
            j+= recipe.writeValuesFor(row, j, s);
        }
    }

    @Override
    public void close() throws IOException {
        spreadsheet.close();
    }

    private static Map<Column, ColumnValueRecipe<ProductMainAll>> DEFAULT_RECIPE_MAP;

    static{
    		
        DEFAULT_RECIPE_MAP = new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.SUBSTANCE_NAME, SingleColumnValueRecipe.create( ProdAllDefaultColumns.SUBSTANCE_NAME ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.SUBSTANCE_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.APPROVAL_ID, SingleColumnValueRecipe.create( ProdAllDefaultColumns.APPROVAL_ID ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.APPROVAL_ID);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.SUBSTANCE_KEY, SingleColumnValueRecipe.create( ProdAllDefaultColumns.SUBSTANCE_KEY ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.SUBSTANCE_KEY);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.INGREDIENT_TYPE, SingleColumnValueRecipe.create( ProdAllDefaultColumns.INGREDIENT_TYPE ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.INGREDIENT_TYPE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.ACTIVE_MOIETY_NAME, SingleColumnValueRecipe.create( ProdAllDefaultColumns.ACTIVE_MOIETY_NAME ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.ACTIVE_MOIETY_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.ACTIVE_MOIETY_UNII, SingleColumnValueRecipe.create( ProdAllDefaultColumns.ACTIVE_MOIETY_UNII ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.ACTIVE_MOIETY_UNII);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.PRODUCT_ID, SingleColumnValueRecipe.create(ProdAllDefaultColumns.PRODUCT_ID ,(s, cell) -> cell.writeString(s.productNDC)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.PRODUCT_NAME, SingleColumnValueRecipe.create( ProdAllDefaultColumns.PRODUCT_NAME ,(s, cell) ->{
            StringBuilder sb = getProductNameDetails(s, ProdAllDefaultColumns.PRODUCT_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.NON_PROPRIETARY_NAME, SingleColumnValueRecipe.create(ProdAllDefaultColumns.NON_PROPRIETARY_NAME ,(s, cell) -> cell.writeString(s.nonProprietaryName)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.STATUS, SingleColumnValueRecipe.create(ProdAllDefaultColumns.STATUS ,(s, cell) -> cell.writeString(s.status)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.PRODUCT_TYPE, SingleColumnValueRecipe.create(ProdAllDefaultColumns.PRODUCT_TYPE ,(s, cell) -> cell.writeString(s.productType)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.ROUTE_OF_ADMINISTRATOR, SingleColumnValueRecipe.create(ProdAllDefaultColumns.ROUTE_OF_ADMINISTRATOR ,(s, cell) -> cell.writeString(s.routeName)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.DOSAGE_FORM_NAME, SingleColumnValueRecipe.create( ProdAllDefaultColumns.DOSAGE_FORM_NAME ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ProdAllDefaultColumns.DOSAGE_FORM_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.MARKETING_CATEGORY_NAME, SingleColumnValueRecipe.create(ProdAllDefaultColumns.MARKETING_CATEGORY_NAME ,(s, cell) -> cell.writeString(s.marketingCategoryName)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.APPLICATION_NUMBER, SingleColumnValueRecipe.create(ProdAllDefaultColumns.APPLICATION_NUMBER ,(s, cell) -> cell.writeString(s.appTypeNumber)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.IS_LISTED, SingleColumnValueRecipe.create(ProdAllDefaultColumns.IS_LISTED ,(s, cell) -> cell.writeString(s.isListed)));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_NAME, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_NAME ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_DUNS, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_DUNS ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_DUNS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_ADDRESS, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_ADDRESS ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_ADDRESS);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_CITY, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_CITY ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_CITY);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_STATE, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_STATE ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_STATE);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_ZIP, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_ZIP ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_ZIP);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.LABELER_COUNTRY, SingleColumnValueRecipe.create( ProdAllDefaultColumns.LABELER_COUNTRY ,(s, cell) ->{
            StringBuilder sb = getProductCompanyDetails(s, ProdAllDefaultColumns.LABELER_COUNTRY);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ProdAllDefaultColumns.PROVENANCE, SingleColumnValueRecipe.create(ProdAllDefaultColumns.PROVENANCE ,(s, cell) -> cell.writeString(s.provenance)));

    }

    private static StringBuilder getProductNameDetails(ProductMainAll s, ProdAllDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if(s.productNameAllList.size() > 0){

            for(ProductNameAll prodName : s.productNameAllList){
                if(sb.length()!=0) {
                    sb.append("|");
                }
                switch (fieldName) {
                    case PRODUCT_NAME:
                        sb.append((prodName.productName != null) ? prodName.productName : "(No Product Name)");
                        break;
                        default: break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getProductCompanyDetails(ProductMainAll s, ProdAllDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        if(s.productCompanyAllList.size() > 0){

            for(ProductCompanyAll prodComp : s.productCompanyAllList){
                if(sb.length()!=0) {
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

                    default: break;
                }
            }
        }
        return sb;
    }

    private static StringBuilder getIngredientDetails(ProductMainAll s, ProdAllDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        try {
            if (s.productIngredientAllList.size() > 0) {

                for (ProductIngredientAll ingred : s.productIngredientAllList) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    switch (fieldName) {
                        case SUBSTANCE_NAME:
                            sb.append((ingred.substanceName != null) ? ingred.substanceName : "(No Ingredient Name)");
                            break;
                        case APPROVAL_ID:
                            sb.append((ingred.substanceApprovalId != null) ? ingred.substanceApprovalId : "(No Approval ID)");
                            break;
                        case SUBSTANCE_KEY:
                            sb.append((ingred.substanceKey != null) ? ingred.substanceKey : "(No Substance Key)");
                            break;
                        case INGREDIENT_TYPE:
                            sb.append((ingred.ingredientType != null) ? ingred.ingredientType : "(No Ingredient Type)");
                            break;
                        case ACTIVE_MOIETY_NAME:
                            if (ingred.activeMoietyName != null) {
                                sb.append((ingred.activeMoietyName != null) ? ingred.activeMoietyName : "(No Active Moiety Name)");
                            } else {
                                sb.append("(No Active Moiety Name)");
                            }
                            break;
                        case ACTIVE_MOIETY_UNII:
                            if (ingred.activeMoietyUnii != null) {
                                sb.append((ingred.activeMoietyUnii != null) ? ingred.activeMoietyUnii : "(No Active Moiety Unii)");
                            } else {
                                sb.append("(No Active Moiety Unii)");
                            }
                            break;
                        case DOSAGE_FORM_NAME:
                            if (ingred.dosageFormName != null) {
                                sb.append(ingred.dosageFormName);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sb;
    }
    /**
     * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
     * but these may be modified using the add/remove column methods.
     *
     */
    public static class Builder{
        private final List<ColumnValueRecipe<ProductMainAll>> columns = new ArrayList<>();
        private final Spreadsheet spreadsheet;

        private boolean publicOnly = false;

        /**
         * Create a new Builder that uses the given Spreadsheet to write to.
         * @param spreadSheet the {@link Spreadsheet} object that will be written to by this exporter. can not be null.
         *
         * @throws NullPointerException if spreadsheet is null.
         */
        public Builder(Spreadsheet spreadSheet){
            Objects.requireNonNull(spreadSheet);
            this.spreadsheet = spreadSheet;

            for(Map.Entry<Column, ColumnValueRecipe<ProductMainAll>> entry : DEFAULT_RECIPE_MAP.entrySet()){
                columns.add(entry.getValue());
            }
        }

        public Builder addColumn(Column column, ColumnValueRecipe<ProductMainAll> recipe){
            return addColumn(column.name(), recipe);
        }

        public Builder addColumn(String columnName, ColumnValueRecipe<ProductMainAll> recipe){
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(recipe);
            columns.add(recipe);

            return this;
        }

        public Builder renameColumn(Column oldColumn, String newName){
            return renameColumn(oldColumn.name(), newName);
        }
        
        public Builder renameColumn(String oldName, String newName){
            //use iterator to preserve order
            ListIterator<ColumnValueRecipe<ProductMainAll>> iter = columns.listIterator();
            while(iter.hasNext()){

                ColumnValueRecipe<ProductMainAll> oldValue = iter.next();
                ColumnValueRecipe<ProductMainAll> newValue = oldValue.replaceColumnName(oldName, newName);
                if(oldValue != newValue){
                   iter.set(newValue);
                }
            }
            return this;
        }

        public ProductAllExporter build(){
            return new ProductAllExporter(this);
        }

        public Builder includePublicDataOnly(boolean publicOnly){
            this.publicOnly = publicOnly;
            return this;
        }

    }
}