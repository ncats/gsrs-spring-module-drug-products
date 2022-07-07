package gov.hhs.gsrs.products.productall;

import ix.core.FieldNameDecorator;

import java.util.HashMap;
import java.util.Map;

public class ProductFieldNameDecorator implements FieldNameDecorator{

	private static final Map<String,String> displayNames;

	static{
			Map<String, String> m = new HashMap<>();

		    m.put("root_productNDC" , "Product NDC");
			m.put("root_productType" , "Product Type");
		    m.put("root_status" , "Status");
		    m.put("root_nonProprietaryName", "Non Proprietary Name");
		    m.put("root_productCompanyAllList_labelerName" , "Labeler Name");
		    m.put("root_productNameAllList_productName" , "Product Name");
		    m.put("root_sponsorName" , "Sponsor Name");
		    m.put("Ingredient Name" , "Ingredient Name");
		    m.put("root_appTypeNumber", "Application Type Number");
		    m.put("root_productIngredientAllList_dosageFormName" , "Dosage Form Name");
			m.put("root_productCompanyAllList_countryWithoutCode" , "Country");
		    m.put("root_productIngredientAllList_ingredientType" , "Ingredient Type");
		    m.put("root_productIngredientAllList_activeMoietyName", "Active Moiety Name");
            m.put("root_productIngredientAllList_substanceName", "Ingredient Name");

		    displayNames = m;
	}

	@Override
	public String getDisplayName(String field) {
		String fdisp = displayNames.get(field);
		if (fdisp == null) {
			return field;
		}
		return fdisp;
	}

}
